package com.example.timofey.myapplication;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by timofey on 22.02.2017.
 */

public class LocationGetter {

    Context context;

    public LocationGetter(Context context){
        this.context = context;
    }

    public Location getCurrentLocation() {
        LocationManager locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        Location location = null;
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        } else if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }
        if (location == null) {
            location = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
        }
        return location;
    }
}
