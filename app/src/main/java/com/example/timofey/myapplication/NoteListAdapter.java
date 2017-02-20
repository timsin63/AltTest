package com.example.timofey.myapplication;

import android.app.Activity;
import android.app.DialogFragment;

import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;


import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.timofey.myapplication.database.DaoSession;
import com.example.timofey.myapplication.database.Note;

import java.util.ArrayList;

import static com.example.timofey.myapplication.NoteListFragment.noteDao;

/**
 * Created by timofey on 17.02.2017.
 */

public class NoteListAdapter extends RecyclerView.Adapter<NoteListAdapter.NoteListViewHolder> {

    public static final String EXTRA_POSITION = "EXTRA_POSITION";
    public static final String INTENT_FILTER = "ACTION_DELETE";

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

        public NoteListViewHolder(View itemView) {
            super(itemView);

            itemTitle = (TextView) itemView.findViewById(R.id.item_title);
        }
    }
}
