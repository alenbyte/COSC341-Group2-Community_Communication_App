package com.example.COSC341Task3.Task5;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton; // Import ImageButton
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.COSC341Task3.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.tasks.CancellationTokenSource;
import com.google.android.gms.tasks.Task;

import java.util.Locale;

public class LocationDetailActivity extends AppCompatActivity {

    public static final String EXTRA_LOCATION = "extra_location";
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private FusedLocationProviderClient fusedLocationClient;
    private Location selectedLocation;

    private TextView locationName, locationDescription, locationDistance;
    private Button btnOpenMaps, btnOpenWebsite;
    private ImageButton btnClose; // Add ImageButton variable
    private ImageView locationImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_detail);

        // REMOVE THE ACTIONBAR LOGIC
        // if (getSupportActionBar() != null) {
        //     getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // }

        // Initialize views
        locationName = findViewById(R.id.detailLocationName);
        locationDescription = findViewById(R.id.detailLocationDescription);
        locationDistance = findViewById(R.id.detailLocationDistance);
        btnOpenMaps = findViewById(R.id.btnOpenMaps);
        btnOpenWebsite = findViewById(R.id.btnOpenWebsite);
        locationImage = findViewById(R.id.detailLocationImage);
        btnClose = findViewById(R.id.btnClose); // Find the close button

        selectedLocation = (Location) getIntent().getSerializableExtra(EXTRA_LOCATION);

        if (selectedLocation == null) {
            Toast.makeText(this, "Error: Location data not found.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        populateViews();
        setupClickListeners(); // This will now set up the close button listener

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        fetchDeviceLocation();
    }

    // REMOVE onSupportNavigateUp() METHOD
    // @Override
    // public boolean onSupportNavigateUp() {
    //     onBackPressed();
    //     return true;
    // }

    private void populateViews() {
        locationName.setText(selectedLocation.getName());
        locationDescription.setText(selectedLocation.getDescription());

        if (selectedLocation.hasWebsite()) {
            btnOpenWebsite.setVisibility(View.VISIBLE);
        } else {
            btnOpenWebsite.setVisibility(View.GONE);
        }
    }


    private void setupClickListeners() {
        // ADD A NULL CHECK FOR THE CLOSE BUTTON
        if (btnClose != null) {
            btnClose.setOnClickListener(v -> {
                finish(); // Simply close the activity
            });
        }

        // The rest of the click listeners
        if (btnOpenMaps != null) {
            btnOpenMaps.setOnClickListener(v -> {
                Uri gmmIntentUri = Uri.parse(String.format(Locale.ENGLISH, "geo:%f,%f?q=%s",
                        selectedLocation.getLatitude(), selectedLocation.getLongitude(), Uri.encode(selectedLocation.getName())));
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                if (mapIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(mapIntent);
                } else {
                    Toast.makeText(this, "Google Maps is not installed.", Toast.LENGTH_SHORT).show();
                }
            });
        }

        if (selectedLocation != null && selectedLocation.hasWebsite()) {
            if (btnOpenWebsite != null) {
                btnOpenWebsite.setOnClickListener(v -> {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(selectedLocation.getWebsite()));
                    startActivity(browserIntent);
                });
            }
        }
    }


    private void fetchDeviceLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
            return;
        }

        // --- Use HIGH ACCURACY location request here as well ---
        CancellationTokenSource cancellationTokenSource = new CancellationTokenSource();
        Task<android.location.Location> locationTask = fusedLocationClient.getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY,
                cancellationTokenSource.getToken()
        );

        locationTask.addOnSuccessListener(this, location -> {
            if (location != null) {
                float[] results = new float[1];
                android.location.Location.distanceBetween(
                        location.getLatitude(), location.getLongitude(),
                        selectedLocation.getLatitude(), selectedLocation.getLongitude(),
                        results);
                float distanceInMeters = results[0];

                if (distanceInMeters > 1000) {
                    locationDistance.setText(String.format(Locale.getDefault(), "%.2f km away", distanceInMeters / 1000));
                } else {
                    locationDistance.setText(String.format(Locale.getDefault(), "%.0f m away", distanceInMeters));
                }
            } else {
                locationDistance.setText("Could not determine distance.");
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fetchDeviceLocation();
            } else {
                locationDistance.setText("Distance calculation requires location permission.");
            }
        }
    }
}
