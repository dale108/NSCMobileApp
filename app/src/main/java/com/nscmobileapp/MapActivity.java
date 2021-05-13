package com.nscmobileapp;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MapActivity extends AppCompatActivity
        implements OnMapReadyCallback {

    private final LatLng SEATTLE = new LatLng(47.620853417739966, -122.34936323068771);


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        ArrayList<String> positionList =  getIntent().getStringArrayListExtra("positionList");
        List<LatLng> collect = positionList.stream().map(location -> location.split(" "))
                .map(splitLocation ->
                        new LatLng(Double.parseDouble(splitLocation[0]), Double.parseDouble(splitLocation[1])))
                .collect(Collectors.toList());
        mapFragment.getMapAsync(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .position(SEATTLE)
                .title("Seattle Marker"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(SEATTLE));
        addCameraMarkers(googleMap);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void addCameraMarkers(GoogleMap googleMap) {
        ArrayList<String> positionList =  getIntent().getStringArrayListExtra("positionList");
        positionList.stream().map(location -> location.split(" "))
                .map(splitLocation ->
                        new LatLng(Double.parseDouble(splitLocation[0]), Double.parseDouble(splitLocation[1])))
                .map(latLng -> {
                    boolean k = true;
                    return googleMap.addMarker(new MarkerOptions().position(latLng));
                });
    }
}

