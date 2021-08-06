/*
* Name: Husandeep Singh Brar
* Student Number: 149046195
* Midterm Test
* Date: 24 June, 2021
* */

package com.example.husandeep_friends;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;

import android.content.DialogInterface;
import android.location.Location;
import android.os.Bundle;

import com.example.husandeep_friends.helpers.LocationHelper;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.husandeep_friends.databinding.ActivityMapsBinding;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private LatLng friendLocation;
    private LatLng currentLocation;
    private LocationHelper locationHelper;
    private LocationCallback locationCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        this.friendLocation = new LatLng(this.getIntent().getDoubleExtra("EXTRA_LAT", 0.0),this.getIntent().getDoubleExtra("EXTRA_LNG",0.0));
        this.currentLocation = new LatLng(this.getIntent().getDoubleExtra("EXTRA_CURRENT_LAT", 0.0),this.getIntent().getDoubleExtra("EXTRA_CURRENT_LNG",0.0));

        if(this.friendLocation == null){
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
            alertBuilder.setTitle("Oops!");
            alertBuilder.setMessage("Couldn't fetch location for " + new String(this.getIntent().getStringExtra("EXTRA_NAME")) + "'s Place");
            alertBuilder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    goBack();
                }
            });
            alertBuilder.show();
        }
    }

    private void goBack(){
        this.finishAffinity();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        mMap.addMarker(new MarkerOptions().position(this.currentLocation).title("Your Place"));
        mMap.addMarker(new MarkerOptions().position(this.friendLocation).title(new String(this.getIntent().getStringExtra("EXTRA_NAME")) + "'s Place"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(this.friendLocation, 2.0f));

        mMap.setBuildingsEnabled(true);
        mMap.setTrafficEnabled(true);
        mMap.setIndoorEnabled(false);
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        UiSettings mySettings = googleMap.getUiSettings();
        mySettings.setZoomControlsEnabled(true);
        mySettings.setZoomGesturesEnabled(true);
        mySettings.setCompassEnabled(true);
    }
}