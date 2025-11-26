package com.example.COSC341Task3.Task5;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.COSC341Task3.R;
import com.example.COSC341Task3.MarketplaceActivity;

public class MainActivitySearch extends AppCompatActivity {

    private LinearLayout navHome, navSearch, navForSale;
    private ImageView iconHome, iconSearch, iconForSale;
    private TextView labelHome, labelSearch, labelForSale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_search);

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
        highlightTab(navSearch);

        // Set click listeners
        navHome.setOnClickListener(v -> {
            Toast.makeText(this, "Home clicked", Toast.LENGTH_SHORT).show();
            highlightTab(navHome);
        });

        navSearch.setOnClickListener(v -> {
            // Already on Search, but you can re-highlight if needed
            highlightTab(navSearch);
        });

        navForSale.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivitySearch.this, MarketplaceActivity.class);
            startActivity(intent);
        });
    }

    private void highlightTab(LinearLayout activeTab) {
        // Reset all tabs first
        iconHome.setColorFilter(getColor(R.color.text_secondary));
        labelHome.setTextColor(getColor(R.color.text_secondary));

        iconSearch.setColorFilter(getColor(R.color.text_secondary));
        labelSearch.setTextColor(getColor(R.color.text_secondary));

        iconForSale.setColorFilter(getColor(R.color.text_secondary));
        labelForSale.setTextColor(getColor(R.color.text_secondary));

        // Highlight the active tab
        if (activeTab == navHome) {
            iconHome.setColorFilter(getColor(R.color.primary));
            labelHome.setTextColor(getColor(R.color.primary));
        } else if (activeTab == navSearch) {
            iconSearch.setColorFilter(getColor(R.color.primary));
            labelSearch.setTextColor(getColor(R.color.primary));
        } else if (activeTab == navForSale) {
            iconForSale.setColorFilter(getColor(R.color.primary));
            labelForSale.setTextColor(getColor(R.color.primary));
        }
    }
}
