package com.example.COSC341Task3.Task5;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.COSC341Task3.Homepage;
import com.example.COSC341Task3.MarketplaceActivity;
import com.example.COSC341Task3.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.CancellationTokenSource;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivitySearch extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, GoogleMap.OnInfoWindowClickListener {

    // --- UI VIEW VARIABLES ---
    private SearchView searchView;
    private LinearLayout navHome, navShop, navSearch, navSettings, navPost;
    private ImageView iconHome, iconShop, iconSearch, iconSettings, iconPost;
    private TextView labelHome, labelShop, labelSearch, labelSettings, labelPost;
    private TextView btnMap, btnList;
    private MapView mapView;
    private RecyclerView listView;
    private Button btnFood, btnPark, btnEvents, btnGrocery;
    private List<Button> filterButtons;

    // --- DATA & LOGIC VARIABLES ---
    private GoogleMap googleMap;
    private LocationAdapter locationAdapter;
    private final List<Location> fullLocationList = new ArrayList<>();
    private final List<Location> filteredLocationList = new ArrayList<>();
    private FusedLocationProviderClient fusedLocationClient;
    private final Map<String, Location> markerMap = new HashMap<>();
    private String currentCategoryFilter = "All";

    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_search);

        initializeViews();
        setupRecyclerView();
        populateLocationData();
        setupClickListeners();
        filterLocations("");

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        fetchDeviceLocationAndCalculateDistances();

        Bundle mapViewBundle = savedInstanceState != null ? savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY) : null;
        if (mapView != null) {
            mapView.onCreate(mapViewBundle);
            mapView.getMapAsync(this);
        }

        updateToggleState(false);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setCompassEnabled(true);
        googleMap.getUiSettings().setMapToolbarEnabled(false);
        googleMap.setOnMarkerClickListener(this);
        googleMap.setOnInfoWindowClickListener(this);
        setupMapMarkers();
        highlightBottomNavTab(navSearch);
    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        return false;
    }

    @Override
    public void onInfoWindowClick(@NonNull Marker marker) {
        String locationId = (String) marker.getTag();
        if (locationId != null) {
            Location clickedLocation = markerMap.get(locationId);
            if (clickedLocation != null) {
                Intent intent = new Intent(this, LocationDetailActivity.class);
                intent.putExtra(LocationDetailActivity.EXTRA_LOCATION, clickedLocation);
                startActivity(intent);
            }
        }
    }

    private void initializeViews() {
        View searchBarLayout = findViewById(R.id.search_searchBar);
        View filterBarLayout = findViewById(R.id.filterBar);
        View bottomNavLayout = findViewById(R.id.bottomNavContainer);

        if (searchBarLayout != null) {
            searchView = searchBarLayout.findViewById(R.id.searchView);
        }

        mapView = findViewById(R.id.mapView);
        listView = findViewById(R.id.listView);

        if (filterBarLayout != null) {
            btnMap = filterBarLayout.findViewById(R.id.btnMap);
            btnList = filterBarLayout.findViewById(R.id.btnList);
            btnFood = filterBarLayout.findViewById(R.id.btnFood);
            btnPark = filterBarLayout.findViewById(R.id.btnPark);
            btnEvents = filterBarLayout.findViewById(R.id.btnEvents);
            btnGrocery = filterBarLayout.findViewById(R.id.btnGrocery);
            filterButtons = Arrays.asList(btnFood, btnPark, btnEvents, btnGrocery);
        }

        if (bottomNavLayout != null) {
            navHome = bottomNavLayout.findViewById(R.id.navHome);
            navShop = bottomNavLayout.findViewById(R.id.navShop);
            navSearch = bottomNavLayout.findViewById(R.id.navSearch);
            navSettings = bottomNavLayout.findViewById(R.id.navSettings);
            navPost = bottomNavLayout.findViewById(R.id.navPost);

            iconHome = bottomNavLayout.findViewById(R.id.iconHome);
            iconShop = bottomNavLayout.findViewById(R.id.iconShop);
            iconSearch = bottomNavLayout.findViewById(R.id.iconSearch);
            iconSettings = bottomNavLayout.findViewById(R.id.iconSettings);
            iconPost = bottomNavLayout.findViewById(R.id.iconPost);

            labelHome = bottomNavLayout.findViewById(R.id.labelHome);
            labelShop = bottomNavLayout.findViewById(R.id.labelShop);
            labelSearch = bottomNavLayout.findViewById(R.id.labelSearch);
            labelSettings = bottomNavLayout.findViewById(R.id.labelSettings);
            labelPost = bottomNavLayout.findViewById(R.id.labelPost);

            // Hide the "Post" button on this activity
            if (navPost != null) {
                navPost.setVisibility(View.GONE);
            }
        }
    }

    private void setupClickListeners() {
        if (searchView != null) {
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    filterLocations(query);
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    filterLocations(newText);
                    return true;
                }
            });
        }

        View.OnClickListener filterClickListener = view -> {
            Button clickedButton = (Button) view;
            String clickedCategory = clickedButton.getText().toString();

            if (currentCategoryFilter.equalsIgnoreCase(clickedCategory)) {
                currentCategoryFilter = "All";
            } else {
                currentCategoryFilter = clickedCategory;
            }
            updateFilterButtonsUI();
            filterLocations(searchView.getQuery().toString());
        };

        for (Button button : filterButtons) {
            if (button != null) {
                button.setOnClickListener(filterClickListener);
            }
        }

        if (btnMap != null) {
            btnMap.setOnClickListener(v -> updateToggleState(false));
        }
        if (btnList != null) {
            btnList.setOnClickListener(v -> updateToggleState(true));
        }

        if (navHome != null) {
            navHome.setOnClickListener(v -> startActivity(new Intent(MainActivitySearch.this, Homepage.class)));
        }
        if (navShop != null) {
            navShop.setOnClickListener(v -> startActivity(new Intent(MainActivitySearch.this, MarketplaceActivity.class)));
        }
        if (navSearch != null) {
            navSearch.setOnClickListener(v -> highlightBottomNavTab(navSearch)); // Already on search
        }
        // ADDED: Click listeners for Settings and Post
        if (navSettings != null) {
            navSettings.setOnClickListener(v -> {
                // TODO: Replace with your SettingsActivity.class
                // Example: startActivity(new Intent(MainActivitySearch.this, SettingsActivity.class));
                Toast.makeText(MainActivitySearch.this, "Settings clicked", Toast.LENGTH_SHORT).show();
            });
        }
        if (navPost != null) {
            navPost.setOnClickListener(v -> {
                // TODO: Replace with your CreatePostActivity.class
                // Example: startActivity(new Intent(MainActivitySearch.this, CreatePostActivity.class));
                Toast.makeText(MainActivitySearch.this, "Post clicked", Toast.LENGTH_SHORT).show();
            });
        }
    }

    private void filterLocations(String query) {
        filteredLocationList.clear();
        String lowerCaseQuery = query.toLowerCase();

        for (Location location : fullLocationList) {
            boolean categoryMatches = "All".equalsIgnoreCase(currentCategoryFilter) ||
                    location.getCategory().equalsIgnoreCase(currentCategoryFilter);

            if (categoryMatches) {
                if (lowerCaseQuery.isEmpty() ||
                        location.getName().toLowerCase().contains(lowerCaseQuery) ||
                        location.getDescription().toLowerCase().contains(lowerCaseQuery)) {
                    filteredLocationList.add(location);
                }
            }
        }

        if (locationAdapter != null) {
            locationAdapter.notifyDataSetChanged();
        }
        setupMapMarkers();
    }

    private void updateFilterButtonsUI() {
        for (Button button : filterButtons) {
            if (button != null) {
                if (button.getText().toString().equalsIgnoreCase(currentCategoryFilter)) {
                    button.setBackgroundResource(R.drawable.filter_button_background_active);
                    button.setTextColor(ContextCompat.getColor(this, android.R.color.white));
                    button.setTypeface(null, Typeface.BOLD);
                } else {
                    button.setBackgroundResource(R.drawable.filter_button_background);
                    button.setTextColor(ContextCompat.getColor(this, R.color.text_primary));
                    button.setTypeface(null, Typeface.NORMAL);
                }
            }
        }
    }

    private void setupRecyclerView() {
        locationAdapter = new LocationAdapter(filteredLocationList);
        listView.setLayoutManager(new LinearLayoutManager(this));
        listView.setAdapter(locationAdapter);
        listView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    private void fetchDeviceLocationAndCalculateDistances() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
            return;
        }

        CancellationTokenSource cancellationTokenSource = new CancellationTokenSource();
        Task<android.location.Location> locationTask = fusedLocationClient.getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY,
                cancellationTokenSource.getToken()
        );

        locationTask.addOnSuccessListener(this, deviceLocation -> {
            if (deviceLocation != null) {
                for (Location loc : fullLocationList) {
                    float[] results = new float[1];
                    android.location.Location.distanceBetween(
                            deviceLocation.getLatitude(), deviceLocation.getLongitude(),
                            loc.getLatitude(), loc.getLongitude(),
                            results);
                    loc.setDistanceInMeters(results[0]);
                }
                if (locationAdapter != null) {
                    locationAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void populateLocationData() {
        fullLocationList.clear();
        fullLocationList.add(new Location("BNA Brewing Co. & Eatery", "Brewpub with bowling", "Food", 49.8869, -119.4953, "https://www.bnabrewing.com/"));
        fullLocationList.add(new Location("RauDZ Regional Table", "Upscale farm-to-table dining", "Food", 49.8854, -119.4969, "https://www.raudz.com/"));
        fullLocationList.add(new Location("The Bohemian Cafe", "Popular spot for breakfast & brunch", "Food", 49.8845, -119.4954, "https://www.bohemiancater.com/"));
        fullLocationList.add(new Location("Safeway Downtown", "Full-service grocery store", "Grocery", 49.8839, -119.4894));
        fullLocationList.add(new Location("Waterfront Park", "Downtown park by the lake with walking paths", "Park", 49.8913, -119.4984));
        fullLocationList.add(new Location("Stuart Park", "Outdoor plaza with public art and ice skating in winter", "Park", 49.8895, -119.4988));
        fullLocationList.add(new Location("City Park", "Large park with beach, sports fields, and waterpark", "Park", 49.8821, -119.5005));
        fullLocationList.add(new Location("Prospera Place", "Arena for concerts and hockey games", "Event", 49.8925, -119.4950, "https://www.prosperaplace.com/"));
        fullLocationList.add(new Location("Kelowna Community Theatre", "Live performances and shows", "Event", 49.8893, -119.4939, "https://theatre.kelowna.ca/"));
    }

    private void setupMapMarkers() {
        if (googleMap == null) return;
        googleMap.clear();
        markerMap.clear();

        LatLng downtownKelowna = new LatLng(49.8880, -119.4960);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(downtownKelowna, 15f));

        for (Location location : filteredLocationList) {
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(new LatLng(location.getLatitude(), location.getLongitude()))
                    .title(location.getName());

            Marker marker = googleMap.addMarker(markerOptions);
            if (marker != null) {
                marker.setTag(location.getId());
                markerMap.put(location.getId(), location);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fetchDeviceLocationAndCalculateDistances();
            } else {
                Toast.makeText(this, "Location permission is required to show distances.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void updateToggleState(boolean isListSelected) {
        if (btnMap == null || btnList == null || mapView == null || listView == null) return;

        if (isListSelected) {
            btnList.setBackgroundResource(R.drawable.toggle_bg_on);
            btnList.setTextColor(ContextCompat.getColor(this, android.R.color.white));
            btnMap.setBackground(null);
            btnMap.setTextColor(ContextCompat.getColor(this, R.color.text_primary));
            mapView.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
        } else {
            btnMap.setBackgroundResource(R.drawable.toggle_bg_on);
            btnMap.setTextColor(ContextCompat.getColor(this, android.R.color.white));
            btnList.setBackground(null);
            btnList.setTextColor(ContextCompat.getColor(this, R.color.text_primary));
            mapView.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        }
    }

    private void highlightBottomNavTab(LinearLayout activeTab) {
        List<ImageView> allIcons = Arrays.asList(iconHome, iconShop, iconSearch, iconSettings, iconPost);
        List<TextView> allLabels = Arrays.asList(labelHome, labelShop, labelSearch, labelSettings, labelPost);

        for (ImageView icon : allIcons) {
            if (icon != null) icon.setColorFilter(ContextCompat.getColor(this, R.color.text_secondary));
        }
        for (TextView label : allLabels) {
            if (label != null) label.setTextColor(ContextCompat.getColor(this, R.color.text_secondary));
        }

        if (activeTab != null) {
            int activeId = activeTab.getId();
            if (activeId == R.id.navHome) {
                if (iconHome != null) iconHome.setColorFilter(ContextCompat.getColor(this, R.color.primary));
                if (labelHome != null) labelHome.setTextColor(ContextCompat.getColor(this, R.color.primary));
            } else if (activeId == R.id.navShop) {
                if (iconShop != null) iconShop.setColorFilter(ContextCompat.getColor(this, R.color.primary));
                if (labelShop != null) labelShop.setTextColor(ContextCompat.getColor(this, R.color.primary));
            } else if (activeId == R.id.navSearch) {
                if (iconSearch != null) iconSearch.setColorFilter(ContextCompat.getColor(this, R.color.primary));
                if (labelSearch != null) labelSearch.setTextColor(ContextCompat.getColor(this, R.color.primary));
            } else if (activeId == R.id.navSettings) {
                if (iconSettings != null) iconSettings.setColorFilter(ContextCompat.getColor(this, R.color.primary));
                if (labelSettings != null) labelSettings.setTextColor(ContextCompat.getColor(this, R.color.primary));
            } else if (activeId == R.id.navPost) {
                if (iconPost != null) iconPost.setColorFilter(ContextCompat.getColor(this, R.color.primary));
                if (labelPost != null) labelPost.setTextColor(ContextCompat.getColor(this, R.color.primary));
            }
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
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }
        if (mapView != null) {
            mapView.onSaveInstanceState(mapViewBundle);
        }
    }
}
