package com.example.COSC341Task3;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ListingDetailsActivity extends AppCompatActivity {

    private static final String TAG = "ListingDetailsActivity";

    private ImageButton btnClose, btnFavorite, btnShare, btnSend;
    private ImageView detailPlaceholderIcon;
    private TextView detailTitle, detailPrice, detailDescription;
    private TextView detailLocation, detailCategory, detailTimestamp;
    private TextView sellerName, sellerDistance, mapLocationText;
    private EditText messageInput;
    private LinearLayout shareChat, shareCopyLink, shareEmail, shareFacebook, shareMore;
    private FrameLayout mapPreview;

    private boolean isFavorited = false;
    private String listingTitle = "";
    private String listingPrice = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            Log.d(TAG, "onCreate: Starting ListingDetailsActivity");
            setContentView(R.layout.activity_listing_details);

            if (!initializeViews()) {
                Log.e(TAG, "Failed to initialize views");
                showErrorAndFinish("Failed to load listing details");
                return;
            }

            if (!loadListingDetails()) {
                Log.e(TAG, "Failed to load listing data");
                showErrorAndFinish("No listing data found");
                return;
            }

            setupClickListeners();

            Log.d(TAG, "onCreate: ListingDetailsActivity loaded successfully");

        } catch (Exception e) {
            Log.e(TAG, "Error in onCreate: " + e.getMessage(), e);
            showErrorAndFinish("Error loading listing details");
        }
    }

    private boolean initializeViews() {
        try {
            btnClose = findViewById(R.id.btnClose);
            btnFavorite = findViewById(R.id.btnFavorite);
            btnShare = findViewById(R.id.btnShare);
            btnSend = findViewById(R.id.btnSend);

            detailPlaceholderIcon = findViewById(R.id.detailPlaceholderIcon);
            detailTitle = findViewById(R.id.detailTitle);
            detailPrice = findViewById(R.id.detailPrice);
            detailDescription = findViewById(R.id.detailDescription);
            detailLocation = findViewById(R.id.detailLocation);
            detailCategory = findViewById(R.id.detailCategory);
            detailTimestamp = findViewById(R.id.detailTimestamp);

            sellerName = findViewById(R.id.sellerName);
            sellerDistance = findViewById(R.id.sellerDistance);
            mapLocationText = findViewById(R.id.mapLocationText);

            messageInput = findViewById(R.id.messageInput);

            shareChat = findViewById(R.id.shareChat);
            shareCopyLink = findViewById(R.id.shareCopyLink);
            shareEmail = findViewById(R.id.shareEmail);
            shareFacebook = findViewById(R.id.shareMore);
            shareMore = findViewById(R.id.shareMore);
            mapPreview = findViewById(R.id.mapPreview);

            if (btnClose == null || detailTitle == null || detailPrice == null) {
                Log.e(TAG, "Critical views are null");
                return false;
            }

            return true;
        } catch (Exception e) {
            Log.e(TAG, "Error initializing views: " + e.getMessage(), e);
            return false;
        }
    }

    private boolean loadListingDetails() {
        try {
            Intent intent = getIntent();
            if (intent == null) {
                Log.e(TAG, "Intent is null");
                return false;
            }

            listingTitle = intent.getStringExtra("listing_title");
            listingPrice = intent.getStringExtra("listing_price");
            String description = intent.getStringExtra("listing_description");
            String location = intent.getStringExtra("listing_location");
            String category = intent.getStringExtra("listing_category");

            Log.d(TAG, "Loading listing: " + listingTitle);

            if (listingTitle != null && !listingTitle.isEmpty()) {
                detailTitle.setText(listingTitle);
            } else {
                detailTitle.setText("Untitled");
                listingTitle = "Untitled";
            }

            if (listingPrice != null && !listingPrice.isEmpty()) {
                detailPrice.setText(listingPrice);
            } else {
                detailPrice.setText("$0.00");
                listingPrice = "$0.00";
            }

            if (description != null && !description.isEmpty()) {
                detailDescription.setText(description);
            } else {
                detailDescription.setText("No description provided");
            }

            if (location != null && !location.isEmpty()) {
                detailLocation.setText(location);
                mapLocationText.setText(location);
            } else {
                detailLocation.setText("Unknown");
                mapLocationText.setText("Unknown Location");
            }

            if (category != null && !category.isEmpty()) {
                detailCategory.setText(category);
            } else {
                detailCategory.setText("Other");
            }

            detailTimestamp.setText("21 hrs ago");
            sellerName.setText("John Smith");
            sellerDistance.setText("2.5 km away");

            if (detailPlaceholderIcon != null) {
                detailPlaceholderIcon.setVisibility(View.VISIBLE);
            }

            return true;
        } catch (Exception e) {
            Log.e(TAG, "Error loading listing details: " + e.getMessage(), e);
            return false;
        }
    }

    private void setupClickListeners() {
        try {
            // Close button - goes back to marketplace
            if (btnClose != null) {
                btnClose.setOnClickListener(v -> {
                    Log.d(TAG, "btnClose: Click detected");
                    Toast.makeText(ListingDetailsActivity.this, "Going back...", Toast.LENGTH_SHORT).show();
                    finish();
                });
            } else {
                Log.e(TAG, "setupClickListeners: btnClose is null");
            }

            // Favorite button
            if (btnFavorite != null) {
                btnFavorite.setClickable(true);
                btnFavorite.setFocusable(true);
                btnFavorite.setOnClickListener(v -> {
                    isFavorited = !isFavorited;
                    if (isFavorited) {
                        btnFavorite.setImageResource(R.drawable.ic_favorite_filled);
                        showToast("Added to favorites");
                    } else {
                        btnFavorite.setImageResource(R.drawable.ic_favorite);
                        showToast("Removed from favorites");
                    }
                });
            }

            // Share button
            if (btnShare != null) {
                btnShare.setClickable(true);
                btnShare.setFocusable(true);
                btnShare.setOnClickListener(v -> shareViaIntent());
            }

            // Send button
            if (btnSend != null) {
                btnSend.setClickable(true);
                btnSend.setFocusable(true);
                btnSend.setOnClickListener(v -> {
                    if (messageInput != null) {
                        String message = messageInput.getText().toString().trim();
                        if (!message.isEmpty()) {
                            showToast("Message sent: " + message);
                            messageInput.setText("");
                        } else {
                            showToast("Please enter a message");
                        }
                    }
                });
            }

            // Share options
            if (shareChat != null) {
                shareChat.setClickable(true);
                shareChat.setFocusable(true);
                shareChat.setOnClickListener(v -> showToast("Opening chat..."));
            }

            if (shareCopyLink != null) {
                shareCopyLink.setClickable(true);
                shareCopyLink.setFocusable(true);
                shareCopyLink.setOnClickListener(v -> copyLinkToClipboard());
            }

            if (shareEmail != null) {
                shareEmail.setClickable(true);
                shareEmail.setFocusable(true);
                shareEmail.setOnClickListener(v -> shareViaEmail());
            }

            if (shareFacebook != null) {
                shareFacebook.setClickable(true);
                shareFacebook.setFocusable(true);
                shareFacebook.setOnClickListener(v -> showToast("Share to Facebook"));
            }

            if (shareMore != null) {
                shareMore.setClickable(true);
                shareMore.setFocusable(true);
                shareMore.setOnClickListener(v -> shareViaIntent());
            }

            // Map preview
            if (mapPreview != null) {
                mapPreview.setClickable(true);
                mapPreview.setFocusable(true);
                mapPreview.setOnClickListener(v -> showToast("Opening full map view..."));
            }

        } catch (Exception e) {
            Log.e(TAG, "Error setting up click listeners: " + e.getMessage(), e);
        }
    }

    private void copyLinkToClipboard() {
        try {
            String listingLink = "https://marketplace.app/listing/" +
                    getIntent().getIntExtra("listing_id", 0);

            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            if (clipboard != null) {
                ClipData clip = ClipData.newPlainText("Listing Link", listingLink);
                clipboard.setPrimaryClip(clip);
                showToast(getString(R.string.link_copied));
            }
        } catch (Exception e) {
            Log.e(TAG, "Error copying link: " + e.getMessage(), e);
            showToast("Failed to copy link");
        }
    }

    private void shareViaEmail() {
        try {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_SUBJECT, "Check out this listing: " + listingTitle);
            intent.putExtra(Intent.EXTRA_TEXT,
                    "I found this item on Marketplace:\n\n" +
                            listingTitle + "\n" +
                            "Price: " + listingPrice + "\n\n" +
                            "Check it out!");

            startActivity(Intent.createChooser(intent, "Share via Email"));
        } catch (Exception e) {
            Log.e(TAG, "Error sharing via email: " + e.getMessage(), e);
            showToast("No email app found");
        }
    }

    private void shareViaIntent() {
        try {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT,
                    "Check out this listing: " + listingTitle + "\n" +
                            "Price: " + listingPrice);

            startActivity(Intent.createChooser(intent, "Share listing"));
        } catch (Exception e) {
            Log.e(TAG, "Error sharing: " + e.getMessage(), e);
            showToast("No apps available to share");
        }
    }

    private void showToast(String message) {
        try {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e(TAG, "Error showing toast: " + e.getMessage(), e);
        }
    }

    private void showErrorAndFinish(String message) {
        showToast(message);
        finish();
    }
}