/*
 * Name: Husandeep Singh Brar
 * Student Number: 149046195
 * Midterm Test
 * Date: 24 June, 2021
 * */

package com.example.husandeep_friends.helpers;

import android.Manifest;
import android.app.Activity;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Looper;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.List;
import java.util.Locale;


public class LocationHelper {
    public boolean isPermissionGranted = false;
    public final int REQUEST_LOCATION_CODE = 101;
    private FusedLocationProviderClient fusedLocationProviderClient = null;
    private LocationRequest locationRequest;
    MutableLiveData<Location> mLocation = new MutableLiveData<>();
    private static final LocationHelper singleInstance = new LocationHelper();
    public static LocationHelper getInstance(){
        return singleInstance;
    }
    private LocationHelper(){
        this.locationRequest = new LocationRequest();
        this.locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        this.locationRequest.setInterval(10000);
    }
    public void checkPermission(Context context){
        this.isPermissionGranted = (PackageManager.PERMISSION_GRANTED == (ContextCompat.checkSelfPermission(context.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)));

        if(!this.isPermissionGranted){
            requestLocationPermission(context);
        }
    }
    public void requestLocationPermission(Context context){
        ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},this.REQUEST_LOCATION_CODE);
    }

    public FusedLocationProviderClient getFusedLocationProviderClient(Context context){
        if(this.fusedLocationProviderClient == null){
            this.fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
        }
        return this.fusedLocationProviderClient;
    }

    @SuppressLint("MissingPermission")
//    public Location getLastLocation(Context context){
    public MutableLiveData<Location> getLastLocation(Context context){
        if (this.isPermissionGranted){
            try{
                this.getFusedLocationProviderClient(context).getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                if (location != null){
                                    mLocation.setValue(location);
                                }
                            }
                        });
            }catch(Exception ex){
                return null;
            }
            return this.mLocation;
        }else {
            return null;
        }
    }

    @SuppressLint("MissingPermission")
    public void requestLocationUpdates(Context context, LocationCallback locationCallback){
        if (this.isPermissionGranted){
            try{
                this.getFusedLocationProviderClient(context).requestLocationUpdates(this.locationRequest, locationCallback, Looper.getMainLooper());
            }catch(Exception ex){
                Log.e("LocationHelper", "requestLocationUpdates: Exception while getting location updates " + ex.getLocalizedMessage() );
            }
        }
    }

    public void stopLocationUpdates(Context context, LocationCallback locationCallback){
        try{
            this.getFusedLocationProviderClient(context).removeLocationUpdates(locationCallback);
        }catch(Exception ex){
            Log.e("LocationHelper", "stopLocationUpdates: Exception while stopping location updates " + ex.getLocalizedMessage() );
        }
    }

    //convert location coordinates(lat & lng) to postal address
    public Address performForwardGeocoding(Context context, Location loc){
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> addressList;

        try{

            addressList = geocoder.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);

            if (addressList.size() > 0){
                Address addressObj = addressList.get(0);
                Log.d("LocationHelper", "performForwardGeocoding: Address obtained from forward geocoding " + addressObj.getAddressLine(0));
                return addressObj;
            }

        }catch(Exception ex){
            Log.e("LocationHelper", "performForwardGeocoding: Couldn't get address for the given location" + ex.getLocalizedMessage() );
        }

        return null;
    }

    //convert postal address to location coordinates (lat & lng)
    public LatLng performReverseGeocoding(Context context, String address){
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());

        try{
            List<Address> locationList = geocoder.getFromLocationName(address, 1);
            LatLng obtainedLocation = new LatLng(locationList.get(0).getLatitude(), locationList.get(0).getLongitude());
            Log.d("LocationHelper", "performReverseGeocoding: ObtainedLocation : " + obtainedLocation.toString());
            return obtainedLocation;

        }catch (Exception ex){
            Log.e("LocationHelper", "performReverseGeocoding: Couldn't get the coordinates for given address " + ex.getLocalizedMessage() );
        }

        return null;
    }
}
