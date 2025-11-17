package com.example.COSC341Task3;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import com.google.android.material.textfield.TextInputEditText;

public class NewListingStep3Activity extends AppCompatActivity {

    private ImageButton btnClose;
    private Button btnPost, btnBack;
    private ImageView previewImage, previewPlaceholder;
    private TextView previewTitle, previewPrice, previewDescription, previewCategory, previewLocation;
    private TextInputEditText inputWhySelling;
    
    private ListingData listingData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Enable edge-to-edge display
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        
        setContentView(R.layout.activity_new_listing_step3);

        // Get listing data from previous step
        listingData = (ListingData) getIntent().getSerializableExtra("listingData");
        if (listingData == null) {
            Toast.makeText(this, "Error loading listing data", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Initialize views
        initializeViews();

        // Setup click listeners
        setupClickListeners();

        // Display preview
        displayPreview();
    }

    private void initializeViews() {
        btnClose = findViewById(R.id.btnClose);
        btnPost = findViewById(R.id.btnPost);
        btnBack = findViewById(R.id.btnBack);
        previewImage = findViewById(R.id.previewImage);
        previewPlaceholder = findViewById(R.id.previewPlaceholder);
        previewTitle = findViewById(R.id.previewTitle);
        previewPrice = findViewById(R.id.previewPrice);
        previewDescription = findViewById(R.id.previewDescription);
        previewCategory = findViewById(R.id.previewCategory);
        previewLocation = findViewById(R.id.previewLocation);
        inputWhySelling = findViewById(R.id.inputWhySelling);
    }

    private void setupClickListeners() {
        btnClose.setOnClickListener(v -> {
            finish();
            // Close the entire flow
            Intent intent = new Intent(this, MarketplaceActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });

        btnBack.setOnClickListener(v -> finish());

        btnPost.setOnClickListener(v -> postListing());
    }

    private void displayPreview() {
        // Set title
        previewTitle.setText(listingData.getTitle());

        // Set price
        if (listingData.isFree()) {
            previewPrice.setText("FREE");
        } else {
            previewPrice.setText(String.format("$%.2f", listingData.getPrice()));
        }

        // Set description
        if (listingData.getDescription() != null && !listingData.getDescription().isEmpty()) {
            previewDescription.setText(listingData.getDescription());
        } else {
            previewDescription.setText("No description provided");
        }

        // Set category
        previewCategory.setText(listingData.getCategory());

        // Set location
        previewLocation.setText(listingData.getLocation());

        // Show placeholder since we're using mock photos
        previewImage.setVisibility(android.view.View.GONE);
        previewPlaceholder.setVisibility(android.view.View.VISIBLE);
    }

    private void postListing() {
        // Save optional "why selling" text
        String whySelling = inputWhySelling.getText() != null ? 
            inputWhySelling.getText().toString().trim() : "";
        listingData.setWhySelling(whySelling);

        // Create a new Listing object
        int newId = ListingStorage.getInstance().getNextId();
        Listing newListing = new Listing(
            newId,
            listingData.getTitle(),
            listingData.getPrice(),
            listingData.isFree(),
            "", // Image URL (empty for mock)
            listingData.getDescription(),
            listingData.getLocation(),
            listingData.getCategory()
        );

        // Add to storage
        ListingStorage.getInstance().addListing(newListing);

        // Show success message
        Toast.makeText(this, R.string.listing_posted, Toast.LENGTH_LONG).show();

        // Navigate back to marketplace with refresh flag
        Intent intent = new Intent(this, MarketplaceActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("refresh", true);
        startActivity(intent);
        finish();
    }
}

