package com.nscmobileapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class MapActivity extends AppCompatActivity
        implements OnMapReadyCallback {
    private static final Gson gson = new Gson();
    List<Camera> cameraList;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        String url = "https://web6.seattle.gov/Travelers/api/Map/Data?zoomId=13&type=2";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    Map<String, Object> map = gson.fromJson(response, Map.class);
                    Optional.ofNullable(map.get("Features"))
                            .map(list -> (List) list)
                            .ifPresent(jsonToParse -> {
                                cameraList = CameraUtils.makeCameraList(jsonToParse);
                                if (getIntent().getBooleanExtra("showMap", false)) {
                                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                                            .findFragmentById(R.id.map);
                                    mapFragment.getMapAsync(this);
                                    }
                            });
                }, error ->
                Log.getStackTraceString(new Exception()));
        SingletonRequestQueue.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void focusCurrentLocation(GoogleMap googleMap) {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
        if (location != null) {
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14.f));
        } else {
            setMyLastLocation(googleMap);
        }
    }

    private void setMyLastLocation(GoogleMap googleMap) {
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
            if (location != null) {
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                googleMap.addMarker(new MarkerOptions()
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                        .position(latLng)
                        .title(String.format("lat:%s, lon:%s", latLng.latitude, latLng.longitude)));
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude())));
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        focusCurrentLocation(googleMap);
        addCameraMarkers(googleMap);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void addCameraMarkers(GoogleMap googleMap) {
        ArrayList<String> positionList = cameraList.stream()
                .map(cam -> cam.getFormattedLocationData()).collect(Collectors.toCollection(ArrayList::new));
        Map<String, LatLng> positionData = new HashMap<>();
        for (String s : positionList) {
            String[] split = s.split(",");
            positionData.put(split[2], new LatLng(Double.parseDouble(split[0]), Double.parseDouble(split[1])));
        }

        positionData.forEach((k, v) -> {
            googleMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE))
                    .position(v)
                    .title(k));
        });
    }
}

