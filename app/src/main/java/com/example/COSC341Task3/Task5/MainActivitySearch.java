package com.example.COSC341Task3.Task5;

import android.Manifest;import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button; // <-- Import Button
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
    private LinearLayout navHome, navSearch, navForSale;
    private ImageView iconHome, iconSearch, iconForSale;
    private TextView labelHome, labelSearch, labelForSale;
    private TextView btnMap, btnList;
    private MapView mapView;
    private RecyclerView listView;
    // Filter Buttons
    private Button btnFood, btnPark, btnEvents, btnGrocery;
    private List<Button> filterButtons;


    // --- DATA & LOGIC VARIABLES ---
    private GoogleMap googleMap;
    private LocationAdapter locationAdapter;
    private final List<Location> fullLocationList = new ArrayList<>(); // Master list
    private final List<Location> filteredLocationList = new ArrayList<>(); // List for the adapter
    private FusedLocationProviderClient fusedLocationClient;
    private final Map<String, Location> markerMap = new HashMap<>(); // To link markers to locations
    private String currentCategoryFilter = "All"; // To track the selected filter, "All" is default

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

            // Find the filter buttons from the correct layout
            btnFood = filterBarLayout.findViewById(R.id.btnFood);
            btnPark = filterBarLayout.findViewById(R.id.btnPark);
            btnEvents = filterBarLayout.findViewById(R.id.btnEvents);
            btnGrocery = filterBarLayout.findViewById(R.id.btnGrocery);
            filterButtons = Arrays.asList(btnFood, btnPark, btnEvents, btnGrocery);
        }

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
                // If the currently active filter is clicked again, deactivate it
                currentCategoryFilter = "All";
            } else {
                // Otherwise, activate the new filter
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
            btnMap.setOnClickListener(v -> {
                updateToggleState(false);
                Toast.makeText(this, "Switched to Map View", Toast.LENGTH_SHORT).show();
            });
        }
        if (btnList != null) {
            btnList.setOnClickListener(v -> {
                updateToggleState(true);
                Toast.makeText(this, "Switched to List View", Toast.LENGTH_SHORT).show();
            });
        }
        if (navHome != null) {
            navHome.setOnClickListener(v -> startActivity(new Intent(MainActivitySearch.this, Homepage.class)));
        }
        if (navSearch != null) {
            navSearch.setOnClickListener(v -> highlightBottomNavTab(navSearch));
        }
        if (navForSale != null) {
            navForSale.setOnClickListener(v -> startActivity(new Intent(MainActivitySearch.this, MarketplaceActivity.class)));
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
                    // This is the active button
                    button.setBackgroundResource(R.drawable.filter_button_background_active);
                    button.setTextColor(ContextCompat.getColor(this, android.R.color.white));
                    button.setTypeface(null, Typeface.BOLD);
                } else {
                    // This is an inactive button
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
                Toast.makeText(this, "Accurate location found!", Toast.LENGTH_SHORT).show();
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
            } else {
                Toast.makeText(this, "Could not get current location to calculate distances.", Toast.LENGTH_LONG).show();
            }
        });

        locationTask.addOnFailureListener(this, e -> {
            Toast.makeText(this, "Failed to get location: " + e.getMessage(), Toast.LENGTH_LONG).show();
        });
    }

    private void populateLocationData() {
        fullLocationList.clear();

        // --- ACCURATE DOWNTOWN KELOWNA LOCATIONS ---

        // Food & Drink
        fullLocationList.add(new Location("BNA Brewing Co. & Eatery", "Brewpub with bowling", "Food", 49.8869, -119.4953, "https://www.bnabrewing.com/"));
        fullLocationList.add(new Location("RauDZ Regional Table", "Upscale farm-to-table dining", "Food", 49.8854, -119.4969, "https://www.raudz.com/"));
        fullLocationList.add(new Location("The Bohemian Cafe", "Popular spot for breakfast & brunch", "Food", 49.8845, -119.4954, "https://www.bohemiancater.com/"));

        // Groceries
        fullLocationList.add(new Location("Safeway Downtown", "Full-service grocery store", "Grocery", 49.8839, -119.4894));

        // Parks & Recreation
        fullLocationList.add(new Location("Waterfront Park", "Downtown park by the lake with walking paths", "Park", 49.8913, -119.4984));
        fullLocationList.add(new Location("Stuart Park", "Outdoor plaza with public art and ice skating in winter", "Park", 49.8895, -119.4988));
        fullLocationList.add(new Location("City Park", "Large park with beach, sports fields, and waterpark", "Park", 49.8821, -119.5005));

        // Events
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
