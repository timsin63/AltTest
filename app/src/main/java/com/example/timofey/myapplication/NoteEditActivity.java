package com.example.timofey.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.timofey.myapplication.database.Note;
import com.example.timofey.myapplication.database.NoteDao;

public class NoteEditActivity extends AppCompatActivity {

    NoteDao noteDao;
    EditText editTitle;
    EditText editContent;
    Note note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_edit);

        note = (Note) getIntent().getSerializableExtra(Note.TAG);

        editTitle = (EditText) findViewById(R.id.edit_title);
        editContent = (EditText) findViewById(R.id.edit_content);
        Button btnCancel = (Button) findViewById(R.id.note_edit_cancel);
        Button btnOk = (Button) findViewById(R.id.note_edit_ok);

        noteDao = NoteListFragment.noteDao;

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
        }

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
        noteDao.insert(newNote);

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
        noteDao.update(note);
    }
}
