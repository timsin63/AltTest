package com.example.timofey.myapplication;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.timofey.myapplication.database.Note;
import com.squareup.picasso.Picasso;

import java.io.File;

/**
 * Created by timofey on 21.02.2017.
 */

public class ImageShowDialog extends DialogFragment {

    public static final String TAG = "IMAGE_SHOW_DIALOG";

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dilog_image, container);

        ImageView imageView = (ImageView) view.findViewById(R.id.image_fullscreen);

        Picasso.with(getActivity().getApplicationContext())
                .load(new File(getArguments().getString(NoteInfoDialog.IMAGE_TAG)))
                .into(imageView);

        return view;
    }
}
