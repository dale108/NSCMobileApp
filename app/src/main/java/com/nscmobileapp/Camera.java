package com.nscmobileapp;

import com.google.android.gms.maps.model.LatLng;

public class Camera {
    private Double latitude;
    private Double longitude;
    private String id;
    private String description;
    private String imageUrl;
    private String type;

    public Camera(Double latitude, Double longitude, String id, String description, String imageUrl, String type) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.id = id;
        this.description = description;
        this.imageUrl = imageUrl;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public String getFormattedLocationData() {
        return String.format("%s,%s,%s", this.latitude, this.longitude, this.description);
    }
}
