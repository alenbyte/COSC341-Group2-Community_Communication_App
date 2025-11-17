package com.example.COSC341Task3;

import java.util.ArrayList;
import java.util.List;

public class MockData {
    
    public static List<Listing> getMockListings() {
        List<Listing> listings = new ArrayList<>();
        
        listings.add(new Listing(1, "Vintage Bicycle", 150.00, false, "", 
            "Classic vintage bicycle in great condition. Perfect for city riding.", 
            "Downtown", "Vehicles", 2.5));
        
        listings.add(new Listing(2, "Coffee Table", 80.00, false, "", 
            "Modern wooden coffee table with glass top. Excellent condition.", 
            "West Side", "Furniture", 5.2));
        
        listings.add(new Listing(3, "Free Books", 0, true, "", 
            "Collection of classic novels. Must pick up.", 
            "North End", "Books", 3.8));
        
        listings.add(new Listing(4, "Gaming Chair", 200.00, false, "", 
            "Ergonomic gaming chair with lumbar support. Like new.", 
            "East District", "Furniture", 7.1));
        
        listings.add(new Listing(5, "Plant Pots", 0, true, "", 
            "Various terracotta plant pots. Different sizes available.", 
            "South Park", "Garden", 4.3));
        
        listings.add(new Listing(6, "Laptop Stand", 25.00, false, "", 
            "Adjustable aluminum laptop stand. Barely used.", 
            "Downtown", "Electronics", 1.9));
        
        listings.add(new Listing(7, "Yoga Mat", 15.00, false, "", 
            "Non-slip yoga mat with carrying strap.", 
            "West Side", "Sports", 6.5));
        
        listings.add(new Listing(8, "Kitchen Utensils", 0, true, "", 
            "Set of kitchen utensils. Moving sale.", 
            "North End", "Kitchen", 8.2));
        
        listings.add(new Listing(9, "Monitor 24\"", 180.00, false, "", 
            "24-inch Full HD monitor. Works perfectly.", 
            "East District", "Electronics", 9.7));
        
        listings.add(new Listing(10, "Wall Art", 45.00, false, "", 
            "Abstract canvas wall art. Set of 3 pieces.", 
            "South Park", "Home Decor", 12.4));
        
        return listings;
    }
}

