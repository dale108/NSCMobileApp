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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
        ArrayList<String> positionList = getIntent().getStringArrayListExtra("positionList");
        Map<String, LatLng> positionData = new HashMap<>();
        for (String s : positionList) {
            String[] split = s.split(",");
            positionData.put(split[2], new LatLng(Double.parseDouble(split[0]), Double.parseDouble(split[1])));
        }

        positionData.forEach((k, v) -> {
            boolean ok = true;
            googleMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE))
                    .position(v)
                    .title(k));
        });
    }
}

