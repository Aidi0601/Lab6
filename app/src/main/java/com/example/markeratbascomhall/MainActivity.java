package com.example.markeratbascomhall;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class MainActivity extends FragmentActivity {

    private GoogleMap mMap;
    //    43.07596897166213, -89.40437117464812
    private final LatLng mDestinationLatLng = new LatLng(43.07596897166213, -89.40437117464812);
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_map);
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        mapFragment.getMapAsync(googleMap -> {
            mMap = googleMap;
            //code to display marker
            googleMap.addMarker(new MarkerOptions()
                    .position(mDestinationLatLng)
                    .title("Destination"));
            displayMyLocation();

        });



    }

    private void displayMyLocation() {
        // check if permission granted
        int permission = ActivityCompat.checkSelfPermission(this.getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION);
        // if not, ask for it
        if (permission == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
        // if permission granted, display marker at current location
        else {
            mFusedLocationProviderClient.getLastLocation().addOnCompleteListener(this, task -> {
                Location mLastKnownLocation = task.getResult();
                if (task.isSuccessful() && mLastKnownLocation != null){
                    mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(mLastKnownLocation.getLatitude(),mLastKnownLocation.getLongitude()))
                            .title("ME"));
                    mMap.addPolyline(new PolylineOptions().add(
                            new LatLng(mLastKnownLocation.getLatitude(),mLastKnownLocation.getLongitude()), mDestinationLatLng));
                }
            });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            // if request is cancelled, the result arrays are empty
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                displayMyLocation();
            }
        }
    }

}