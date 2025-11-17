package com.example.COSC341Task3;

public class Listing {
    private int id;
    private String title;
    private double price;
    private boolean isFree;
    private String imageUrl;
    private String description;
    private String location;
    private String category;
    private double distanceKm;

    public Listing(int id, String title, double price, boolean isFree, String imageUrl, String description, String location, String category) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.isFree = isFree;
        this.imageUrl = imageUrl;
        this.description = description;
        this.location = location;
        this.category = category;
        this.distanceKm = 0.0;
    }
    
    public Listing(int id, String title, double price, boolean isFree, String imageUrl, String description, String location, String category, double distanceKm) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.isFree = isFree;
        this.imageUrl = imageUrl;
        this.description = description;
        this.location = location;
        this.category = category;
        this.distanceKm = distanceKm;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public double getPrice() {
        return price;
    }

    public boolean isFree() {
        return isFree;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public String getCategory() {
        return category;
    }

    public double getDistanceKm() {
        return distanceKm;
    }

    public void setDistanceKm(double distanceKm) {
        this.distanceKm = distanceKm;
    }

    public String getFormattedPrice() {
        if (isFree) {
            return "FREE";
        }
        return String.format("$%.2f", price);
    }
}

