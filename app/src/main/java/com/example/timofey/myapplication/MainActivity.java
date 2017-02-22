package com.example.timofey.myapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

import com.example.timofey.myapplication.database.DaoSession;

import java.security.Provider;

public class MainActivity extends AppCompatActivity {

    public static final String[] EXTERNAL_PERMISSIONS = {"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"};
    public static final int READ_WRITE_EXTERNAL_REQUEST_CODE = 9372;
    public static final int LOCATION_REQUEST_CODE = 572;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        checkPermissions();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), NoteEditActivity.class);
                startActivity(intent);
            }
        });

        ImageButton mapButton = (ImageButton) findViewById(R.id.map_btn);

        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager();

                MapFragment mapFragment = (MapFragment) fragmentManager.findFragmentByTag(MapFragment.TAG);

                if (mapFragment == null) {
                    mapFragment = new MapFragment();
                }

                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.fragment, mapFragment, MapFragment.TAG);
                fragmentTransaction.commit();

            }
        });
    }


    public void checkPermissions(){
        if (!(ActivityCompat.checkSelfPermission(this.getApplicationContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) ||
                !(ActivityCompat.checkSelfPermission(this.getApplicationContext(),
                        Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, MainActivity.EXTERNAL_PERMISSIONS,
                    MainActivity.READ_WRITE_EXTERNAL_REQUEST_CODE);
        }

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
            }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case READ_WRITE_EXTERNAL_REQUEST_CODE:
                boolean readGranted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean writeGranted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                if (!writeGranted || !readGranted){
                    finish();
                    startActivity(getIntent());
                }
                break;
            case LOCATION_REQUEST_CODE:
                boolean locationGranted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                if (!locationGranted){
                    finish();
                    startActivity(getIntent());
                }
                break;
        }
    }
}

