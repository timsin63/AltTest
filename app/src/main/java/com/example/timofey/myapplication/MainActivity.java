package com.example.timofey.myapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    public static final String[] EXTERNAL_PERMISSIONS = {"android.permission.READ_EXTERNAL_STORAGE","android.permission.WRITE_EXTERNAL_STORAGE"};
    public static final int READ_WRITE_EXTERNAL_REQUEST_CODE = 9372;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!(ActivityCompat.checkSelfPermission(this.getApplicationContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) ||
                !(ActivityCompat.checkSelfPermission(this.getApplicationContext(),
                        Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, MainActivity.EXTERNAL_PERMISSIONS,
                    MainActivity.READ_WRITE_EXTERNAL_REQUEST_CODE);
        }



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), NoteEditActivity.class);
                startActivity(intent);
            }
        });
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
        }
    }
}

