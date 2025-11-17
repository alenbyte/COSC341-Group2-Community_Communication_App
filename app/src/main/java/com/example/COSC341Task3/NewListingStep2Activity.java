package com.example.COSC341Task3;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.WindowCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class NewListingStep2Activity extends AppCompatActivity {

    private ImageButton btnClose;
    private Button btnNext, btnBack;
    private TextInputEditText inputTitle, inputDescription, inputPrice;
    private AutoCompleteTextView spinnerCategory;
    private CheckBox checkboxFree;
    private CardView locationCard;
    private TextView locationText;
    private TextInputLayout priceInputLayout;
    
    private ListingData listingData;
    private String selectedLocation = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Enable edge-to-edge display
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        
        setContentView(R.layout.activity_new_listing_step2);

        // Get listing data from previous step
        listingData = (ListingData) getIntent().getSerializableExtra("listingData");
        if (listingData == null) {
            listingData = new ListingData();
        }

        // Initialize views
        initializeViews();

        // Setup click listeners
        setupClickListeners();

        // Setup category dropdown
        setupCategoryDropdown();
    }

    private void initializeViews() {
        btnClose = findViewById(R.id.btnClose);
        btnNext = findViewById(R.id.btnNext);
        btnBack = findViewById(R.id.btnBack);
        inputTitle = findViewById(R.id.inputTitle);
        inputDescription = findViewById(R.id.inputDescription);
        inputPrice = findViewById(R.id.inputPrice);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        checkboxFree = findViewById(R.id.checkboxFree);
        locationCard = findViewById(R.id.locationCard);
        locationText = findViewById(R.id.locationText);
        priceInputLayout = findViewById(R.id.priceInputLayout);
    }

    private void setupCategoryDropdown() {
        String[] categories = getResources().getStringArray(R.array.categories);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, 
            android.R.layout.simple_dropdown_item_1line, categories);
        spinnerCategory.setAdapter(adapter);
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

        checkboxFree.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                inputPrice.setEnabled(false);
                inputPrice.setText("0");
                priceInputLayout.setEnabled(false);
            } else {
                inputPrice.setEnabled(true);
                inputPrice.setText("");
                priceInputLayout.setEnabled(true);
            }
        });

        locationCard.setOnClickListener(v -> {
            // Simulate location selection
            selectedLocation = "Downtown Campus";
            locationText.setText(selectedLocation);
            Toast.makeText(this, "Location selected (simulated)", Toast.LENGTH_SHORT).show();
        });

        btnNext.setOnClickListener(v -> {
            if (validateInputs()) {
                saveDataAndProceed();
            }
        });
    }

    private boolean validateInputs() {
        String title = inputTitle.getText() != null ? inputTitle.getText().toString().trim() : "";
        String category = spinnerCategory.getText().toString().trim();
        String price = inputPrice.getText() != null ? inputPrice.getText().toString().trim() : "";

        if (title.isEmpty()) {
            Toast.makeText(this, R.string.please_add_title, Toast.LENGTH_SHORT).show();
            return false;
        }

        if (category.isEmpty()) {
            Toast.makeText(this, R.string.please_add_category, Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!checkboxFree.isChecked() && price.isEmpty()) {
            Toast.makeText(this, R.string.please_add_price, Toast.LENGTH_SHORT).show();
            return false;
        }

        if (selectedLocation.isEmpty()) {
            Toast.makeText(this, R.string.please_add_location, Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void saveDataAndProceed() {
        // Save data to listing object
        listingData.setTitle(inputTitle.getText().toString().trim());
        listingData.setDescription(inputDescription.getText() != null ? 
            inputDescription.getText().toString().trim() : "");
        listingData.setCategory(spinnerCategory.getText().toString().trim());
        listingData.setFree(checkboxFree.isChecked());
        
        if (!checkboxFree.isChecked()) {
            try {
                listingData.setPrice(Double.parseDouble(inputPrice.getText().toString().trim()));
            } catch (NumberFormatException e) {
                listingData.setPrice(0);
            }
        } else {
            listingData.setPrice(0);
        }
        
        listingData.setLocation(selectedLocation);

        // Navigate to Step 3
        Intent intent = new Intent(NewListingStep2Activity.this, NewListingStep3Activity.class);
        intent.putExtra("listingData", listingData);
        startActivity(intent);
    }
}

