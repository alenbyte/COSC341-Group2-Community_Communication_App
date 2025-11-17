package com.example.COSC341Task3;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.WindowCompat;

public class NewListingStep1Activity extends AppCompatActivity {

    private ImageButton btnClose;
    private Button btnNextSkip;
    private CardView addPhotosCard;
    private TextView photoCount;
    private ListingData listingData;
    private int photoCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Enable edge-to-edge display
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        
        setContentView(R.layout.activity_new_listing_step1);

        // Initialize listing data
        listingData = new ListingData();

        // Initialize views
        initializeViews();

        // Setup click listeners
        setupClickListeners();
    }

    private void initializeViews() {
        btnClose = findViewById(R.id.btnClose);
        btnNextSkip = findViewById(R.id.btnNextSkip);
        addPhotosCard = findViewById(R.id.addPhotosCard);
        photoCount = findViewById(R.id.photoCount);
        
        updatePhotoCount();
    }

    private void setupClickListeners() {
        btnClose.setOnClickListener(v -> finish());

        addPhotosCard.setOnClickListener(v -> {
            // Simulate photo selection
            if (photoCounter < 10) {
                photoCounter++;
                updatePhotoCount();
                Toast.makeText(this, "Photo added (simulated)", Toast.LENGTH_SHORT).show();
                
                // Update button text to "Next" once photos are added
                if (photoCounter > 0) {
                    btnNextSkip.setText(R.string.next);
                }
            } else {
                Toast.makeText(this, "Maximum 10 photos reached", Toast.LENGTH_SHORT).show();
            }
        });

        btnNextSkip.setOnClickListener(v -> {
            // Save photo count to listing data
            for (int i = 0; i < photoCounter; i++) {
                listingData.getPhotoUris().add("photo_" + i);
            }
            
            // Navigate to Step 2
            Intent intent = new Intent(NewListingStep1Activity.this, NewListingStep2Activity.class);
            intent.putExtra("listingData", listingData);
            startActivity(intent);
        });
    }

    private void updatePhotoCount() {
        photoCount.setText(getString(R.string.photos_added, photoCounter));
    }
}

