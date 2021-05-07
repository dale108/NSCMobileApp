package com.nscmobileapp;

public class Camera {
    private String id;
    private String description;
    private String imageUrl;
    private String type;

    public Camera(String id, String description, String imageUrl, String type) {
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
}
