package com.example.COSC341Task3;

import java.util.ArrayList;
import java.util.List;

public class ListingStorage {
    private static ListingStorage instance;
    private List<Listing> listings;
    private int nextId = 100;

    private ListingStorage() {
        listings = new ArrayList<>();
        listings.addAll(MockData.getMockListings());
    }

    public static synchronized ListingStorage getInstance() {
        if (instance == null) {
            instance = new ListingStorage();
        }
        return instance;
    }

    public List<Listing> getListings() {
        return new ArrayList<>(listings);
    }

    public void addListing(Listing listing) {
        listings.add(0, listing);
    }

    public int getNextId() {
        return nextId++;
    }

    public void reset() {
        listings.clear();
        listings.addAll(MockData.getMockListings());
        nextId = 100;
    }
}

