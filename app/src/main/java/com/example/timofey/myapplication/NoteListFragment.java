package com.example.timofey.myapplication;

import android.content.res.Configuration;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.timofey.myapplication.database.DaoSession;
import com.example.timofey.myapplication.database.Note;
import com.example.timofey.myapplication.database.NoteDao;

import java.util.ArrayList;
import java.util.List;


public class NoteListFragment extends Fragment {

    View view;
    RecyclerView noteListView;
    public static NoteDao noteDao;
    ArrayList<Note> list;


    public NoteListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_main, container, false);
        }

        noteListView = (RecyclerView) view.findViewById(R.id.note_recycler_view);
        GridLayoutManager layoutManager;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            layoutManager = new GridLayoutManager(getContext(), 2);
        } else {
            layoutManager = new GridLayoutManager(getContext(), 3);
        }
        noteListView.setLayoutManager(layoutManager);

        DaoSession daoSession = ((App) getActivity().getApplication()).getDaoSession();
        noteDao = daoSession.getNoteDao();

        list = (ArrayList<Note>) noteDao.loadAll();

        if (list.size() == 0){
            Note firstNote = new Note();
            firstNote.setTitle(getString(R.string.empty_title));
            firstNote.setContent(getString(R.string.empty_content));
            list.add(firstNote);
        }

        noteListView.setAdapter(new NoteListAdapter(list, getActivity()));

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        list = (ArrayList<Note>) noteDao.loadAll();

        if (list.size() == 0){
            Note firstNote = new Note();
            firstNote.setTitle(getString(R.string.empty_title));
            firstNote.setContent(getString(R.string.empty_content));
            list.add(firstNote);
        }

        noteListView.setAdapter(new NoteListAdapter(list, getActivity()));
    }
}
