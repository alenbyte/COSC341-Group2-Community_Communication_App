package com.example.COSC341Task3.Task5;

import java.io.Serializable;
import java.util.UUID; // Import UUID

public class Location implements Serializable {
    private final String id; // ADD THIS
    private final String name;
    private final String description;
    private final double latitude;
    private final double longitude;
    private final String website;
    private final String category;
    private float distanceInMeters = -1;

    public Location(String name, String description, String category, double latitude, double longitude, String website) {
        this.id = UUID.randomUUID().toString(); // GENERATE A UNIQUE ID
        this.name = name;
        this.description = description;
        this.category = category;
        this.latitude = latitude;
        this.longitude = longitude;
        this.website = website;
    }

    public Location(String name, String description, String category, double latitude, double longitude) {
        this(name, description, category, latitude, longitude, null);
    }

    // --- Getters ---
    public String getId() { return id; } // ADD GETTER
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getCategory() { return category; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
    public String getWebsite() { return website; }
    public boolean hasWebsite() { return website != null && !website.isEmpty(); }
    public float getDistanceInMeters() { return distanceInMeters; }

    public void setDistanceInMeters(float distanceInMeters) {
        this.distanceInMeters = distanceInMeters;
    }
}
