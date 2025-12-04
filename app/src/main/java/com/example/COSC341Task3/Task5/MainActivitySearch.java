package com.example.COSC341Task3.Task5;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


import androidx.appcompat.app.AppCompatActivity;

import com.example.COSC341Task3.Homepage;
import com.example.COSC341Task3.R;
import com.example.COSC341Task3.MarketplaceActivity;

public class MainActivitySearch extends AppCompatActivity implements OnMapReadyCallback {

    // UI View variables
    private LinearLayout navHome, navSearch, navForSale;
    private ImageView iconHome, iconSearch, iconForSale;
    private TextView labelHome, labelSearch, labelForSale;
    private TextView btnMap, btnList;

    private MapView mapView;
    private GoogleMap googleMap;
    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_search);

        // Safely initialize all UI components
        initializeViews();
        setupClickListeners();

        // --- MapView Initialization ---
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }
        if (mapView != null) {
            mapView.onCreate(mapViewBundle);
            mapView.getMapAsync(this);
        }

        // Set the initial state (e.g., Map is selected by default)
        updateToggleState(false); // false for Map, true for List
    }

    private void initializeViews() {
        // Find included layout containers first to prevent crashes
        View filterBarLayout = findViewById(R.id.filterBar);
        View bottomNavLayout = findViewById(R.id.bottomNavContainer);

        mapView = findViewById(R.id.mapView); // Find the MapView

        // Initialize Map/List buttons from within the filter bar layout
        if (filterBarLayout != null) {
            btnMap = filterBarLayout.findViewById(R.id.btnMap);
            btnList = filterBarLayout.findViewById(R.id.btnList);
        }

        // Initialize bottom navigation from within its container
        if (bottomNavLayout != null) {
            navHome = bottomNavLayout.findViewById(R.id.navHome);
            navSearch = bottomNavLayout.findViewById(R.id.navSearch);
            navForSale = bottomNavLayout.findViewById(R.id.navForSale);
            iconHome = bottomNavLayout.findViewById(R.id.iconHome);
            iconSearch = bottomNavLayout.findViewById(R.id.iconSearch);
            iconForSale = bottomNavLayout.findViewById(R.id.iconForSale);
            labelHome = bottomNavLayout.findViewById(R.id.labelHome);
            labelSearch = bottomNavLayout.findViewById(R.id.labelSearch);
            labelForSale = bottomNavLayout.findViewById(R.id.labelForSale);
        }
    }

    private void setupClickListeners() {
        if (btnMap != null) {
            btnMap.setOnClickListener(v -> {
                updateToggleState(false); // Select Map
                Toast.makeText(this, "Switched to Map View", Toast.LENGTH_SHORT).show();
            });
        }
        if (btnList != null) {
            btnList.setOnClickListener(v -> {
                updateToggleState(true); // Select List
                Toast.makeText(this, "Switched to List View", Toast.LENGTH_SHORT).show();
            });
        }

        if (navHome != null) {
            navHome.setOnClickListener(v -> {
                Toast.makeText(this, "Home clicked", Toast.LENGTH_SHORT).show();
                highlightBottomNavTab(navHome);
            });
        }
        if (navSearch != null) {
            navSearch.setOnClickListener(v -> {
                highlightBottomNavTab(navSearch);
                // Already on search, do nothing else
            });
        }
        if (navForSale != null) {
            navForSale.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivitySearch.this, MarketplaceActivity.class);
                startActivity(intent);
            });
        }
    }

    /**
     * Updates the visual state of the Map/List toggle buttons AND the view visibility.
     * @param isListSelected true if List is the active selection, false if Map is.
     */
    private void updateToggleState(boolean isListSelected) {
        if (btnMap == null || btnList == null || mapView == null) return; // Safety check

        if (isListSelected) {
            // List is selected
            btnList.setBackgroundResource(R.drawable.toggle_bg_on);
            btnList.setTextColor(getColor(R.color.white));
            btnMap.setBackgroundResource(android.R.color.transparent);
            btnMap.setTextColor(getColor(R.color.text_primary));

            // --- HIDE MAP VIEW ---
            mapView.setVisibility(View.GONE);
            // You would show your list RecyclerView here, e.g.:
            // myRecyclerView.setVisibility(View.VISIBLE);

        } else {
            // Map is selected
            btnMap.setBackgroundResource(R.drawable.toggle_bg_on);
            btnMap.setTextColor(getColor(R.color.white));
            btnList.setBackgroundResource(android.R.color.transparent);
            btnList.setTextColor(getColor(R.color.text_primary));

            // --- SHOW MAP VIEW ---
            mapView.setVisibility(View.VISIBLE);
            // You would hide your list RecyclerView here, e.g.:
            // myRecyclerView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;

        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setCompassEnabled(true);
        googleMap.getUiSettings().setMapToolbarEnabled(false);

        setupMapMarkers();
        // Initialize navigation views
        navHome = findViewById(R.id.navHome);
        navSearch = findViewById(R.id.navSearch);
        navForSale = findViewById(R.id.navForSale);

        iconHome = findViewById(R.id.iconHome);
        iconSearch = findViewById(R.id.iconSearch);
        iconForSale = findViewById(R.id.iconForSale);

        labelHome = findViewById(R.id.labelHome);
        labelSearch = findViewById(R.id.labelSearch);
        labelForSale = findViewById(R.id.labelForSale);

        // Highlight the current tab (Search)
        highlightBottomNavTab(navSearch);

        // Set click listeners
        navHome.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivitySearch.this, Homepage.class);
            startActivity(intent);
        });

        navSearch.setOnClickListener(v -> {
            // Already on Search, but you can re-highlight if needed
            highlightBottomNavTab(navSearch);
        });

        navForSale.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivitySearch.this, MarketplaceActivity.class);
            startActivity(intent);
        });
    }

    private void setupMapMarkers() {
        if (googleMap == null) return;

        // --- Center of UBC Okanagan ---
        LatLng ubco = new LatLng(49.9398, -119.3968);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ubco, 15f));

        // --- Food Locations Near UBCO (Approx Coordinates) ---

        // 📍 Tim Hortons (UBCO University Centre)
        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(49.93922, -119.39550))
                .title("Tim Hortons (University Centre)"));

        // 📍 Starbucks (UNC Building – Lower level)
        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(49.93905, -119.39520))
                .title("Starbucks (UNC Building)"));

        // 📍 Pritchard Dining Hall (Fresh Slice, Cafeteria)
        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(49.93865, -119.39710))
                .title("Pritchard Dining Hall"));

        // 📍 Sunshine Cafe (University Centre)
        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(49.93895, -119.39570))
                .title("Sunshine Cafe"));

        // 📍 Comma Café (Library Building)
        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(49.93990, -119.39730))
                .title("Comma Café (Library)"));
    }


    private void highlightBottomNavTab(LinearLayout activeTab) {
        if (navHome == null) return;
        iconHome.setColorFilter(getColor(R.color.text_secondary));
        labelHome.setTextColor(getColor(R.color.text_secondary));
        iconSearch.setColorFilter(getColor(R.color.text_secondary));
        labelSearch.setTextColor(getColor(R.color.text_secondary));
        iconForSale.setColorFilter(getColor(R.color.text_secondary));
        labelForSale.setTextColor(getColor(R.color.text_secondary));

        if (activeTab.getId() == R.id.navHome) {
            iconHome.setColorFilter(getColor(R.color.primary));
            labelHome.setTextColor(getColor(R.color.primary));
        } else if (activeTab.getId() == R.id.navSearch) {
            iconSearch.setColorFilter(getColor(R.color.primary));
            labelSearch.setTextColor(getColor(R.color.primary));
        } else if (activeTab.getId() == R.id.navForSale) {
            iconForSale.setColorFilter(getColor(R.color.primary));
            labelForSale.setTextColor(getColor(R.color.primary));
        }
    }

    // --- MapView Lifecycle Methods ---
    @Override
    public void onResume() {
        super.onResume();
        if (mapView != null) mapView.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mapView != null) mapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mapView != null) mapView.onStop();
    }

    @Override
    public void onPause() {
        if (mapView != null) mapView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        if (mapView != null) mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (mapView != null) mapView.onLowMemory();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }
        if (mapView != null) mapView.onSaveInstanceState(mapViewBundle);
    }
}
