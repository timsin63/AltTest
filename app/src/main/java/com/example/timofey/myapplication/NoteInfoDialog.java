package com.example.timofey.myapplication;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.timofey.myapplication.database.Note;

/**
 * Created by timofey on 19.02.2017.
 */

public class NoteInfoDialog extends DialogFragment {

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

        note = (Note) getArguments().getSerializable(Note.TAG);

        title.setText(note.getTitle());
        content.setText(note.getContent());

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NoteEditActivity.class);
                intent.putExtra(Note.TAG, note);
                dismiss();
                startActivity(intent);
            }
        });

        btnDelete.setOnClickListener(onDeleteClickListener);

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
                    Intent intent = new Intent(NoteListAdapter.INTENT_FILTER);
                    intent.putExtra(NoteListAdapter.EXTRA_POSITION, getArguments().getInt(NoteListAdapter.EXTRA_POSITION));
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
}


