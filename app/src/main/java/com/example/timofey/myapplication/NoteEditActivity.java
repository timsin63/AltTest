package com.example.timofey.myapplication;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.timofey.myapplication.database.Note;
import com.example.timofey.myapplication.database.NoteDao;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.internal.PlaceEntity;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;

import org.greenrobot.greendao.DaoException;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class NoteEditActivity extends AppCompatActivity {

    private static final int IMAGE_PICK_REQUEST_CODE = 4772;
    private static final int PLACE_PICK_REQUEST_CODE = 7772;
    public static final String TAG = "NOTE_EDIT_ACTIVITY";

    private NoteDao noteDao;
    private EditText editTitle;
    private EditText editContent;
    private Spinner spinner;
    Note note;
    private ImageView notePhoto;
    private String photoPath;
    private TextView placeNameView;
    String placeName;
    LatLng placeLatLng;
    boolean isMapOpened;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_edit);

        note = (Note) getIntent().getSerializableExtra(Note.TAG);

        editTitle = (EditText) findViewById(R.id.edit_title);
        editContent = (EditText) findViewById(R.id.edit_content);
        Button btnCancel = (Button) findViewById(R.id.note_edit_cancel);
        Button btnOk = (Button) findViewById(R.id.note_edit_ok);
        spinner = (Spinner) findViewById(R.id.importance_spinner);
        notePhoto = (ImageView) findViewById(R.id.photo_preview_edit);
        Button deleteImgBtn = (Button) findViewById(R.id.delete_img_btn);
        Button placeBtn = (Button) findViewById(R.id.place_btn);
        placeNameView = (TextView) findViewById(R.id.place_name);

        noteDao = NoteListFragment.noteDao;
        isMapOpened = getIntent().getBooleanExtra(MapFragment.IS_MAP_OPENED, false);

        if (note == null){
            btnOk.setText(R.string.add);
            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addNote();

                }
            });
        } else {
            btnOk.setText(R.string.update);
            editTitle.setText(note.getTitle());
            editContent.setText(note.getContent());
            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateNote();
                    finish();
                }
            });
            try {
                spinner.setSelection(note.getImportance());
            } catch (Exception e){}
        }

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        setImageFromDb();

        notePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, IMAGE_PICK_REQUEST_CODE);
            }
        });

        deleteImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoPath = null;
                notePhoto.setImageResource(R.drawable.add_photo_placeholder);
            }
        });

        try {
            placeName = note.getPlaceName();
            placeLatLng = new LatLng(note.getLatitude(), note.getLongitude());
        } catch (NullPointerException e){}

        if (placeName != null){
            placeNameView.setText(placeName);
        }

        placeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(builder.build(NoteEditActivity.this), PLACE_PICK_REQUEST_CODE);
                } catch (Exception e){
                    Log.e(TAG, e.getLocalizedMessage());
                }
            }
        });
    }


    private void setImageFromDb(){
        try {
            photoPath = note.getPhotoPath();
            File file = new File(photoPath);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file), null, options);
            notePhoto.setImageBitmap(Bitmap.createScaledBitmap(bitmap, bitmap.getWidth()/3, bitmap.getHeight()/3, true));
        } catch (Exception e){
            Log.e(TAG, e.getLocalizedMessage());
            notePhoto.setImageResource(R.drawable.add_photo_placeholder);
        }
    }

    public void addNote(){
        Note newNote = new Note();
        String title = editTitle.getText().toString();
        String content = editContent.getText().toString();

        if (TextUtils.isEmpty(title)){
            Toast.makeText(getApplicationContext(), getString(R.string.enter_title), Toast.LENGTH_SHORT).show();
            return;
        }
        newNote.setTitle(title);
        newNote.setContent(content);
        newNote.setImportance(spinner.getSelectedItemPosition());
        newNote.setPhotoPath(photoPath);

        if (placeName != null){
            newNote.setPlaceName(placeName);
            newNote.setLatitude(placeLatLng.latitude);
            newNote.setLongitude(placeLatLng.longitude);
        } else {
            try {
                Location location = new LocationGetter(getApplicationContext()).getCurrentLocation();
                newNote.setLatitude(location.getLatitude());
                newNote.setLongitude(location.getLongitude());
            } catch (Exception e) {
                Log.e(TAG, e.getLocalizedMessage());
            }
        }
        noteDao.insert(newNote);
        if (isMapOpened){
            sendBroadcast(new Intent(MapFragment.INTENT_FILTER));
        }

        finish();
    }


    private void updateNote(){
        String title = editTitle.getText().toString();
        String content = editContent.getText().toString();

        if (TextUtils.isEmpty(title)){
            Toast.makeText(getApplicationContext(), getString(R.string.enter_title), Toast.LENGTH_SHORT).show();
            return;
        }
        note.setTitle(title);
        note.setContent(content);
        note.setImportance(spinner.getSelectedItemPosition());
        note.setPhotoPath(photoPath);

        if (placeName != null){
            note.setPlaceName(placeName);
            note.setLatitude(placeLatLng.latitude);
            note.setLongitude(placeLatLng.longitude);
        } else {
            try {
                Location location = new LocationGetter(getApplicationContext()).getCurrentLocation();
                note.setLatitude(location.getLatitude());
                note.setLongitude(location.getLongitude());
            } catch (Exception e) {
            }
        }
        try {
            noteDao.update(note);
        } catch (DaoException e){
            noteDao.insert(note);
        }

        if (isMapOpened){
            sendBroadcast(new Intent(MapFragment.INTENT_FILTER));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case IMAGE_PICK_REQUEST_CODE:
                try {
                    Uri selectedImg = data.getData();
                    InputStream inputStream = getContentResolver().openInputStream(selectedImg);
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    notePhoto.setImageBitmap(Bitmap.createScaledBitmap(bitmap, bitmap.getWidth()/3, bitmap.getHeight()/3, true));
                    photoPath = getRealPathFromURI(getApplicationContext(), selectedImg);
                } catch (Exception e) {
                    Log.e(TAG, e.getLocalizedMessage());
                }
                break;
            case PLACE_PICK_REQUEST_CODE:
                try {
                    Place place = PlacePicker.getPlace(data, this);
                    placeName = place.getName().toString();
                    placeLatLng = place.getLatLng();
                    placeNameView.setText(placeName);
                } catch (NullPointerException e){}
                break;
        }
    }

    String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

}
