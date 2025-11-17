package com.example.COSC341Task3;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        

        Intent intent = new Intent(this, MarketplaceActivity.class);
        startActivity(intent);
        finish();
    }
}