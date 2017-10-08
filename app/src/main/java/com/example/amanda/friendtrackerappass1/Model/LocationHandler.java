package com.example.amanda.friendtrackerappass1.Model;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by amanda on 7/10/2017.
 */

public class LocationHandler implements LocationListener{
    private String LOG_TAG = this.getClass().getName();
    private String myLocation;

    @Override
    public void onLocationChanged(Location location) {
        Double lat = location.getLatitude();
        Double lon = location.getLongitude();

        myLocation = lat + ":" + lon;
        Log.i(LOG_TAG, myLocation + " my location ");
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
