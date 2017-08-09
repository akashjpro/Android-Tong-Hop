package com.adida.aka.androidgeneral.receiver;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Aka on 8/10/2017.
 */

public class LocationReceiver extends BroadcastReceiver {

    private Context mContext;
    private LocationManager mLocationManager;
    private Location mLocation;
    private Geocoder mGeocoder;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.mContext = context;
        mLocationManager = (LocationManager)mContext.getSystemService(Context.LOCATION_SERVICE);
        mGeocoder = new Geocoder(mContext, Locale.getDefault());

        if (!isLocationEnabled()){
            Toast.makeText(context, "Location off", Toast.LENGTH_SHORT).show();
            return;
        }

        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLocationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER, 60 * 1000, 10, locationListener);
    }


    private final LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            mLocation = location;

            double latitude = mLocation.getLatitude();
            double longitude = mLocation.getLongitude();

            List<Address> listAddress = new ArrayList<>();
            String mAddress = "";
            try {
                listAddress = mGeocoder.getFromLocation(latitude,
                        longitude, 1);
                //get array address size default is 5
                for (int i = 0; i < 5; i++) {
                    String address = listAddress.get(0)
                            .getAddressLine(i);
                    if (address != null) {
                        if (listAddress != null) {
                            mAddress += address + " ";
                        } else {
                            mAddress = address + " ";
                        }
                    }
                }

                Toast.makeText(mContext, "Toa do (" + latitude + ", " + longitude + ")\n"
                        + mAddress, Toast.LENGTH_SHORT).show();


            } catch (IOException e) {
                e.printStackTrace();
            }
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
    };


    private boolean isLocationEnabled() {
        return mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }
}
