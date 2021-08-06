/*
 * Name: Husandeep Singh Brar
 * Student Number: 149046195
 * Midterm Test
 * Date: 24 June, 2021
 * */

package com.example.husandeep_friends;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.husandeep_friends.adapters.FriendAdapter;
import com.example.husandeep_friends.databinding.ActivityFriendBinding;
import com.example.husandeep_friends.helpers.LocationHelper;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class FriendActivity extends AppCompatActivity implements OnFriendClickListener {
    ActivityFriendBinding friendBinding;
    private FriendAdapter friendAdapter;
    private ArrayList<friend> allFriends;
    private LocationHelper locationHelper;
    private Location lastLocation;
    private LocationCallback locationCallback;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.friendBinding = ActivityFriendBinding.inflate(getLayoutInflater());
        View view = friendBinding.getRoot();
        setContentView(view);
        this.locationHelper = LocationHelper.getInstance();
        this.locationHelper.checkPermission(this);
        this.allFriends = new ArrayList<>();
        ArrayList<friend> extraFriends = this.getIntent().getParcelableArrayListExtra("EXTRA_FRIEND_LIST");
        if (extraFriends != null) {
            this.allFriends = extraFriends;
        }
        this.friendAdapter = new FriendAdapter(this, this.allFriends, this);
        this.friendBinding.rvFriends.setLayoutManager(new LinearLayoutManager(this));
        this.friendBinding.rvFriends.setAdapter(friendAdapter);


        if (this.locationHelper.isPermissionGranted) {
            this.locationHelper.getLastLocation(this).observe(this, new Observer<Location>() {
                @Override
                public void onChanged(Location location) {
                    if (location != null) {
                        lastLocation = location;
                        Address obtainedAddress = locationHelper.performForwardGeocoding(getApplicationContext(), lastLocation);
                    }
                }
            });
            this.initiateLocationListener();
        }
    }
    private void initiateLocationListener(){
        this.locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                for (Location loc : locationResult.getLocations()){
                    lastLocation = loc;
                    Address obtainedAddress = locationHelper.performForwardGeocoding(getApplicationContext(), lastLocation);
                }
            }
        };

        this.locationHelper.requestLocationUpdates(this, locationCallback);
    }

    private void openMap(LatLng coordinates, String name){
        Intent mapIntent = new Intent(this, MapsActivity.class);
        mapIntent.putExtra("EXTRA_CURRENT_LAT", this.lastLocation.getLatitude());
        mapIntent.putExtra("EXTRA_CURRENT_LNG", this.lastLocation.getLongitude());
        mapIntent.putExtra("EXTRA_LAT", coordinates.latitude);
        mapIntent.putExtra("EXTRA_LNG", coordinates.longitude);
        mapIntent.putExtra("EXTRA_NAME", name);
        startActivity(mapIntent);
    }

    private LatLng doReverseGeocoding(String givenAddress){
        LatLng obtainedCoordinates = null;
        if (this.locationHelper.isPermissionGranted){
            obtainedCoordinates = this.locationHelper.performReverseGeocoding(this, givenAddress);
        }
        return obtainedCoordinates;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == this.locationHelper.REQUEST_LOCATION_CODE){
            this.locationHelper.isPermissionGranted = (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED);
        }
    }
    @Override
    public void onFriendClicked(friend friend) {
       openMap(doReverseGeocoding(friend.getAddress()),friend.getName());
    }
}