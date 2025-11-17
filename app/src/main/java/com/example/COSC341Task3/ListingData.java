package com.example.COSC341Task3;

import java.io.Serializable;
import java.util.ArrayList;

public class ListingData implements Serializable {
    private ArrayList<String> photoUris;
    private String title;
    private String description;
    private String category;
    private double price;
    private boolean isFree;
    private String location;
    private String whySelling;

    public ListingData() {
        photoUris = new ArrayList<>();
        isFree = false;
    }

    // Getters and Setters
    public ArrayList<String> getPhotoUris() {
        return photoUris;
    }

    public void setPhotoUris(ArrayList<String> photoUris) {
        this.photoUris = photoUris;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isFree() {
        return isFree;
    }

    public void setFree(boolean free) {
        isFree = free;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getWhySelling() {
        return whySelling;
    }

    public void setWhySelling(String whySelling) {
        this.whySelling = whySelling;
    }
}

