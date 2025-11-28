package com.example.COSC341Task3;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.WindowCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MarketplaceActivity extends AppCompatActivity {

    private RecyclerView listingsRecyclerView;
    private ListingsAdapter adapter;
    private List<Listing> allListings;
    private List<Listing> filteredListings;
    private EditText searchEditText;
    private Button btnMyListings, btnAllCategories, btnFree, btnDistance;
    private LinearLayout navHome, navSearch, navForSale;
    private FloatingActionButton fabPost;
    private TextView noResultsText;

    // Filter state
    private String searchQuery = "";
    private String selectedCategory = "";
    private boolean filterFreeOnly = false;
    private double maxDistanceKm = Double.MAX_VALUE;
    private boolean isFreeFilterActive = false;
    private boolean isDistanceFilterActive = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Enable edge-to-edge display
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        
        setContentView(R.layout.activity_marketplace);

        // Initialize views
        initializeViews();

        // Setup RecyclerView with 2-column grid
        setupRecyclerView();

        // Setup search
        setupSearch();

        // Setup click listeners
        setupClickListeners();

        // Load listings
        loadListings();
    }

    private void initializeViews() {
        listingsRecyclerView = findViewById(R.id.listingsRecyclerView);
        searchEditText = findViewById(R.id.searchEditText);
        
        // Filter buttons
        btnMyListings = findViewById(R.id.btnFilters);
        btnAllCategories = findViewById(R.id.btnFood);
        btnFree = findViewById(R.id.btnPark);
        btnDistance = findViewById(R.id.btnEvents);
        
        // Bottom navigation
        navHome = findViewById(R.id.navHome);
        navSearch = findViewById(R.id.navSearch);
        navForSale = findViewById(R.id.navForSale);
        fabPost = findViewById(R.id.fabPost);
        
        // Create "no results" text view programmatically if needed
        noResultsText = new TextView(this);
        noResultsText.setText(R.string.no_results);
        noResultsText.setTextSize(16);
        noResultsText.setTextColor(ContextCompat.getColor(this, R.color.text_secondary));
        noResultsText.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
        noResultsText.setPadding(16, 32, 16, 32);
        noResultsText.setVisibility(TextView.GONE);
    }

    private void setupRecyclerView() {
        // Create 2-column grid layout
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        listingsRecyclerView.setLayoutManager(gridLayoutManager);
    }

    private void setupSearch() {
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchQuery = s.toString().trim().toLowerCase();
                applyFilters();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void setupClickListeners() {
        // My listings button
        btnMyListings.setOnClickListener(v -> {
            Toast.makeText(this, "My Listings filter - Coming soon!", Toast.LENGTH_SHORT).show();
        });

        // All categories button
        btnAllCategories.setOnClickListener(v -> {
            showCategoryDialog();
        });

        // Free button
        btnFree.setOnClickListener(v -> {
            isFreeFilterActive = !isFreeFilterActive;
            updateFilterButtonState(btnFree, isFreeFilterActive);
            applyFilters();
        });

        // Distance button
        btnDistance.setOnClickListener(v -> {
            showDistanceDialog();
        });

        // Bottom navigation
        navHome.setOnClickListener(v -> {
            Intent intent = new Intent(MarketplaceActivity.this, Homepage.class);
            startActivity(intent);
        });

        navSearch.setOnClickListener(v -> {
            Intent intent = new Intent(MarketplaceActivity.this, com.example.COSC341Task3.Task5.MainActivitySearch.class);
            startActivity(intent);
        });

        navForSale.setOnClickListener(v -> {
            // Already on this page
        });

        // Floating action button - open new listing (Step 1)
        fabPost.setOnClickListener(v -> {
            Intent intent = new Intent(MarketplaceActivity.this, NewListingStep1Activity.class);
            startActivity(intent);
        });
    }

    private void showCategoryDialog() {
        String[] categories = getResources().getStringArray(R.array.categories);
        
        // Add "All Categories" option at the beginning
        String[] categoriesWithAll = new String[categories.length + 1];
        categoriesWithAll[0] = getString(R.string.all_categories_option);
        System.arraycopy(categories, 0, categoriesWithAll, 1, categories.length);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.select_category);
        
        builder.setItems(categoriesWithAll, (dialog, which) -> {
            if (which == 0) {
                // All categories
                selectedCategory = "";
                btnAllCategories.setText(R.string.all_categories);
                updateFilterButtonState(btnAllCategories, false);
            } else {
                // Specific category
                selectedCategory = categoriesWithAll[which];
                btnAllCategories.setText(selectedCategory);
                updateFilterButtonState(btnAllCategories, true);
            }
            applyFilters();
        });
        
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void showDistanceDialog() {
        String[] distanceOptions = {"1 km", "5 km", "10 km", "25 km", "50 km", "Any distance"};
        double[] distanceValues = {1.0, 5.0, 10.0, 25.0, 50.0, Double.MAX_VALUE};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Distance");
        
        builder.setItems(distanceOptions, (dialog, which) -> {
            maxDistanceKm = distanceValues[which];
            
            if (which == distanceOptions.length - 1) {
                // Any distance
                btnDistance.setText("Distance");
                isDistanceFilterActive = false;
                updateFilterButtonState(btnDistance, false);
            } else {
                btnDistance.setText(distanceOptions[which]);
                isDistanceFilterActive = true;
                updateFilterButtonState(btnDistance, true);
            }
            applyFilters();
        });
        
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void updateFilterButtonState(Button button, boolean isActive) {
        if (isActive) {
            button.setBackgroundColor(ContextCompat.getColor(this, R.color.filter_button_selected));
            button.setTextColor(ContextCompat.getColor(this, R.color.white));
        } else {
            button.setBackgroundColor(ContextCompat.getColor(this, R.color.filter_button_bg));
            button.setTextColor(ContextCompat.getColor(this, R.color.text_primary));
        }
    }

    private void applyFilters() {
        filteredListings = new ArrayList<>();
        
        for (Listing listing : allListings) {
            // Apply search filter
            if (!searchQuery.isEmpty()) {
                String title = listing.getTitle().toLowerCase();
                String description = listing.getDescription().toLowerCase();
                
                if (!title.contains(searchQuery) && !description.contains(searchQuery)) {
                    continue;
                }
            }
            
            // Apply category filter
            if (!selectedCategory.isEmpty()) {
                if (!listing.getCategory().equals(selectedCategory)) {
                    continue;
                }
            }
            
            // Apply free filter
            if (isFreeFilterActive) {
                if (!listing.isFree()) {
                    continue;
                }
            }
            
            // Apply distance filter
            if (isDistanceFilterActive) {
                if (listing.getDistanceKm() > maxDistanceKm) {
                    continue;
                }
            }
            
            // Listing passed all filters
            filteredListings.add(listing);
        }
        
        updateRecyclerView();
    }

    private void updateRecyclerView() {
        if (adapter == null) {
            adapter = new ListingsAdapter(filteredListings, listing -> {
                // Open listing details when item is clicked
                Intent intent = new Intent(MarketplaceActivity.this, ListingDetailsActivity.class);
                intent.putExtra("listing_id", listing.getId());
                intent.putExtra("listing_title", listing.getTitle());
                intent.putExtra("listing_price", listing.getFormattedPrice());
                intent.putExtra("listing_description", listing.getDescription());
                intent.putExtra("listing_location", listing.getLocation());
                intent.putExtra("listing_category", listing.getCategory());
                startActivity(intent);
            });
            listingsRecyclerView.setAdapter(adapter);
        } else {
            adapter = new ListingsAdapter(filteredListings, listing -> {
                // Open listing details when item is clicked
                Intent intent = new Intent(MarketplaceActivity.this, ListingDetailsActivity.class);
                intent.putExtra("listing_id", listing.getId());
                intent.putExtra("listing_title", listing.getTitle());
                intent.putExtra("listing_price", listing.getFormattedPrice());
                intent.putExtra("listing_description", listing.getDescription());
                intent.putExtra("listing_location", listing.getLocation());
                intent.putExtra("listing_category", listing.getCategory());
                startActivity(intent);
            });
            listingsRecyclerView.setAdapter(adapter);
        }
        
        // Show/hide empty state
        if (filteredListings.isEmpty()) {
            Toast.makeText(this, R.string.no_results, Toast.LENGTH_SHORT).show();
        }
    }

    private void loadListings() {
        // Load from storage (includes both mock and user-created listings)
        allListings = ListingStorage.getInstance().getListings();
        filteredListings = new ArrayList<>(allListings);
        
        updateRecyclerView();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        // Check if we need to refresh
        if (intent.getBooleanExtra("refresh", false)) {
            loadListings();
            applyFilters();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh listings when returning to this activity
        if (allListings != null) {
            List<Listing> updatedListings = ListingStorage.getInstance().getListings();
            if (updatedListings.size() != allListings.size()) {
                loadListings();
                applyFilters();
            }
        }
    }
}
