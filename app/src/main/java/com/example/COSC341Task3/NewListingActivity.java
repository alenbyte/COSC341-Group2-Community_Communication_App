package com.example.COSC341Task3;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

public class NewListingActivity extends AppCompatActivity {

    private TextInputEditText inputTitle, inputPrice, inputCategory, inputLocation, inputDescription;
    private CheckBox checkboxFree;
    private Button btnPostListing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_listing);

        // Initialize views
        initializeViews();

        // Setup click listeners
        setupClickListeners();
    }

    private void initializeViews() {
        inputTitle = findViewById(R.id.inputTitle);
        inputPrice = findViewById(R.id.inputPrice);
        inputCategory = findViewById(R.id.inputCategory);
        inputLocation = findViewById(R.id.inputLocation);
        inputDescription = findViewById(R.id.inputDescription);
        checkboxFree = findViewById(R.id.checkboxFree);
        btnPostListing = findViewById(R.id.btnPostListing);
    }

    private void setupClickListeners() {
        // Toggle price input based on free checkbox
        checkboxFree.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                inputPrice.setEnabled(false);
                inputPrice.setText("0");
            } else {
                inputPrice.setEnabled(true);
                inputPrice.setText("");
            }
        });

        // Post listing button
        btnPostListing.setOnClickListener(v -> {
            if (validateInputs()) {
                // TODO: Implement actual posting functionality
                Toast.makeText(this, "Listing posted successfully!", Toast.LENGTH_SHORT).show();
                finish(); // Return to previous screen
            }
        });
    }

    private boolean validateInputs() {
        String title = inputTitle.getText() != null ? inputTitle.getText().toString().trim() : "";
        String price = inputPrice.getText() != null ? inputPrice.getText().toString().trim() : "";
        String category = inputCategory.getText() != null ? inputCategory.getText().toString().trim() : "";
        String location = inputLocation.getText() != null ? inputLocation.getText().toString().trim() : "";

        if (title.isEmpty()) {
            Toast.makeText(this, "Please enter a title", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!checkboxFree.isChecked() && price.isEmpty()) {
            Toast.makeText(this, "Please enter a price or mark as free", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (category.isEmpty()) {
            Toast.makeText(this, "Please enter a category", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (location.isEmpty()) {
            Toast.makeText(this, "Please enter a location", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}

