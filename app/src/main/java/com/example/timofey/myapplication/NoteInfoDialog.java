package com.example.timofey.myapplication;

import android.Manifest;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.timofey.myapplication.database.Note;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by timofey on 19.02.2017.
 */

public class NoteInfoDialog extends DialogFragment {

    public static final String IMAGE_TAG = "IMAGE_TO_FULLSCREEN";
    public static final String TAG = "NOTE_INFO_DIALOG";
    Note note;

    public NoteInfoDialog() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_note_info, container, false);

        final TextView title = (TextView) view.findViewById(R.id.note_info_title);
        TextView content = (TextView) view.findViewById(R.id.note_info_content);
        ImageButton btnEdit = (ImageButton) view.findViewById(R.id.btn_edit);
        ImageButton btnDelete = (ImageButton) view.findViewById(R.id.btn_delete);
        ImageButton btnExport = (ImageButton) view.findViewById(R.id.btn_export);
        ImageView dialogPreview = (ImageView) view.findViewById(R.id.dialog_preview);
        ImageButton btnShare = (ImageButton) view.findViewById(R.id.btn_share);

        note = (Note) getArguments().getSerializable(Note.TAG);

        title.setText(note.getTitle());
        content.setText(note.getContent());

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NoteEditActivity.class);
                intent.putExtra(Note.TAG, note);
                boolean isMapOpened = getArguments().getBoolean(MapFragment.IS_MAP_OPENED, false);
                intent.putExtra(MapFragment.IS_MAP_OPENED, isMapOpened);
                dismiss();
                startActivity(intent);
            }
        });

        btnDelete.setOnClickListener(onDeleteClickListener);

        btnExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    exportFile();
                } catch (IOException e) {
                    Log.e(TAG, e.getLocalizedMessage());
                }
            }
        });

        LinearLayout header = (LinearLayout) view.findViewById(R.id.dialog_header);

        try {
            view.setBackgroundColor(getResources().getColor(NoteColors.getColor(note.getImportance())));
            header.setBackgroundColor(getResources().getColor(NoteColors.getHeaderColor(note.getImportance())));
        } catch (NullPointerException e){

        }

        if (note.getPhotoPath() != null){
            Picasso.with(getActivity().getApplicationContext()).load(new File(note.getPhotoPath()))
                    .resize(200, 200)
                    .into(dialogPreview);

            dialogPreview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogFragment imageShowDialog = new ImageShowDialog();
                    Bundle args = new Bundle();
                    args.putString(IMAGE_TAG, note.getPhotoPath());
                    imageShowDialog.setArguments(args);
                    imageShowDialog.show(getActivity().getFragmentManager(), ImageShowDialog.TAG);
                }
            });
        }

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("*/*");
                shareIntent.putExtra(Intent.EXTRA_TITLE, note.getTitle());
                shareIntent.putExtra(Intent.EXTRA_TEXT, note.getContent());
                startActivity(Intent.createChooser(shareIntent, getString(R.string.app_name)));
            }
        });

        return view;
    }


    View.OnClickListener onDeleteClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(R.string.deleting_title);
            builder.setMessage(R.string.deleting_message);
            builder.setCancelable(true);
            builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    NoteListFragment.noteDao.delete(note);
                    boolean isMapOpened = getArguments().getBoolean(MapFragment.IS_MAP_OPENED, false);

                    Intent intent = null;
                    if (isMapOpened){
                        intent = new Intent(MapFragment.INTENT_FILTER);
                    } else {
                        intent = new Intent(NoteListAdapter.INTENT_FILTER);
                        intent.putExtra(NoteListAdapter.EXTRA_POSITION, getArguments().getInt(NoteListAdapter.EXTRA_POSITION));
                    }
                    getActivity().sendBroadcast(intent);
                    dismiss();
                }
            });

            builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dismiss();
                }
            });
            builder.show();
        }
    };



    private void exportFile() throws IOException {

        File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/smart_notes");
        if (!dir.exists()){
            dir.mkdir();
        }

        File textFile = new File(dir, note.getTitle() + ".txt");

        if (!textFile.exists()){
            textFile.createNewFile();
        }

        FileOutputStream outputStream = new FileOutputStream(textFile);
        outputStream.write(note.getContent().getBytes());
        outputStream.close();

        Toast.makeText(getActivity().getApplicationContext(), getString(R.string.exported), Toast.LENGTH_SHORT).show();
    }
}


