package edu.depaul.csc472.coffeeshoplocator;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

/**
 * Created by Marco on 11/21/16.
 */

public class CoffeeMapActivity extends FragmentActivity implements OnMapReadyCallback,GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private ArrayList<Place> placesList;
    private double longitude;
    private double latitude;
    private LatLng currentLoc;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_view);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Intent i = getIntent();

        if (i != null) {
            longitude = i.getDoubleExtra("list_long", -1);
            latitude = i.getDoubleExtra("list_lat", -1);
            placesList = i.getParcelableArrayListExtra("placeList");
        }

        currentLoc = new LatLng(latitude,longitude);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        for(Place p:placesList){
            mMap.addMarker(new MarkerOptions()
            .position(new LatLng(p.geometry.location.lat,p.geometry.location.lng))
                    .title(p.name)
                    .snippet(p.vicinity));
        }
//
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLoc,14));
        mMap.setOnMarkerClickListener(this);

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

}
