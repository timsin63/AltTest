package com.example.timofey.myapplication;


import android.app.Activity;
import android.app.DialogFragment;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.timofey.myapplication.database.DaoSession;
import com.example.timofey.myapplication.database.Note;
import com.example.timofey.myapplication.database.NoteDao;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import static com.example.timofey.myapplication.NoteListAdapter.EXTRA_POSITION;


/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback {

    public static final String TAG = "MAPS_FRAGMENT";

    private View view;
    private GoogleMap googleMap;
    ArrayList<Note> list;
    ImageButton mapButton;
    SupportMapFragment supportMapFragment;

    public MapFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        if (view == null){
            view = inflater.inflate(R.layout.fragment_map, container, false);
        }

        mapButton = (ImageButton) getActivity().findViewById(R.id.map_btn);
        mapButton.setVisibility(View.INVISIBLE);

        supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

        supportMapFragment.getMapAsync(this);

        return view;
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {

        this.googleMap = googleMap;

        DaoSession daoSession = ((App) getActivity().getApplicationContext()).getDaoSession();
        NoteDao noteDao = daoSession.getNoteDao();

        list = (ArrayList<Note>) noteDao.loadAll();

        try {
            Location currentLocation = new LocationGetter(getContext()).getCurrentLocation();
            LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 13);
            this.googleMap.animateCamera(cameraUpdate);
        } catch (Exception e){}

        this.googleMap.setMyLocationEnabled(true);

        for (Note note : list){
            try {
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.title(note.getTitle());
                markerOptions.snippet(note.getContent());
                markerOptions.position(new LatLng(note.getLatitude(), note.getLongitude()));
                googleMap.addMarker(markerOptions);
            } catch (Exception e){}
        }

        googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                for (int i = 0; i < list.size(); i++){
                    if (list.get(i).getTitle().equals(marker.getTitle())){
                        DialogFragment infoDialog = new NoteInfoDialog();
                        Bundle args = new Bundle();
                        args.putSerializable(Note.TAG, list.get(i));
                        args.putInt(EXTRA_POSITION, i);
                        infoDialog.setArguments(args);
                        infoDialog.show(((Activity) getActivity()).getFragmentManager(), NoteInfoDialog.TAG);
                        return;
                    }
                }
            }
        });
    }


    @Override
    public void onStop() {
        super.onStop();
        mapButton.setVisibility(View.VISIBLE);
    }
}
