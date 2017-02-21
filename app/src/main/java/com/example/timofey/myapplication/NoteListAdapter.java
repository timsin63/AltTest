package com.example.timofey.myapplication;

import android.app.Activity;
import android.app.DialogFragment;

import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;


import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.timofey.myapplication.database.DaoSession;
import com.example.timofey.myapplication.database.Note;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

import static com.example.timofey.myapplication.NoteListFragment.noteDao;

/**
 * Created by timofey on 17.02.2017.
 */

public class NoteListAdapter extends RecyclerView.Adapter<NoteListAdapter.NoteListViewHolder> {

    public static final String EXTRA_POSITION = "EXTRA_POSITION";
    public static final String INTENT_FILTER = "ACTION_DELETE";
    public static final String TAG = "NOTE_LIST_ADAPTER";

    ArrayList<Note> list;
    Context context;

    public NoteListAdapter(ArrayList<Note> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public NoteListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note_list, parent, false);
        NoteListViewHolder viewHolder = new NoteListViewHolder(view);

        context.registerReceiver(onItemDeleteReceiver, new IntentFilter(INTENT_FILTER));

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(NoteListViewHolder holder, final int position) {

        holder.itemTitle.setText(list.get(position).getTitle());

        try {
            Picasso.with(context).load(new File(list.get(position).getPhotoPath()))
                    .resize(120, 120)
                    .into(holder.imagePreview);
        } catch (Exception e){
            Log.e(TAG, e.getLocalizedMessage());
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment infoDialog = new NoteInfoDialog();
                Bundle args = new Bundle();
                args.putSerializable(Note.TAG, list.get(position));
                args.putInt(EXTRA_POSITION, position);
                infoDialog.setArguments(args);
                infoDialog.show(((Activity) context).getFragmentManager(), NoteInfoDialog.TAG);
            }
        });

        try {
            holder.itemTitle.setBackgroundColor(context.getResources()
                    .getColor(NoteColors.getColor(list.get(position).getImportance())));
            holder.itemView.setBackgroundColor(context.getResources()
                    .getColor(NoteColors.getColor(list.get(position).getImportance())));
        } catch (NullPointerException e){}

    }

    BroadcastReceiver onItemDeleteReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("broadcast", "reseived");
            int pos = intent.getIntExtra(EXTRA_POSITION, -1);
            notifyItemRemoved(pos);
            notifyItemRangeChanged(pos, list.size());

            list = (ArrayList<Note>) noteDao.loadAll();
        }
    };

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class NoteListViewHolder extends RecyclerView.ViewHolder{

        TextView itemTitle;
        ImageView imagePreview;

        public NoteListViewHolder(View itemView) {
            super(itemView);

            itemTitle = (TextView) itemView.findViewById(R.id.item_title);
            imagePreview = (ImageView) itemView.findViewById(R.id.list_preview);
        }
    }
}
