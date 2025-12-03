package com.example.COSC341Task3;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.COSC341Task3.Task2.Post;
import com.example.COSC341Task3.Task2.PostAdapter;
import com.example.COSC341Task3.task4.adminReportPage;


import java.util.ArrayList;
import java.util.List;

public class Homepage extends AppCompatActivity {

    private boolean isPostArmed = false;

    // keep references so we can reset from anywhere
    private LinearLayout navPost;
    private ImageView iconPost;
    private TextView labelPost;

    //report from user
    private String lastReportedUser;
    private String lastReportedPost;
    private String lastReportedReason;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        //to read user report
        Intent fromReport = getIntent();
        if (fromReport != null) {
            lastReportedUser   = fromReport.getStringExtra("username");
            lastReportedPost   = fromReport.getStringExtra("postText");
            lastReportedReason = fromReport.getStringExtra("reason");
        }

        RecyclerView rv = findViewById(R.id.postsRecyclerView);
        rv.setLayoutManager(new LinearLayoutManager(this));

        List<Post> posts = new ArrayList<>();
        posts.add(new Post("User", "This is a text-only post"));
        posts.add(new Post("Layla", "Post with image!", R.drawable.ic_image));
        posts.add(new Post("Alawi habib galby abu Hussein", "Another text-only post"));

        PostAdapter adapter = new PostAdapter(posts);
        rv.setAdapter(adapter);
        ConstraintLayout mainLayout = findViewById(R.id.main);

        // bottom nav items
        LinearLayout navHome = findViewById(R.id.navHome);
        LinearLayout navShop = findViewById(R.id.navShop);
        LinearLayout navSettings = findViewById(R.id.navSettings);
        navPost = findViewById(R.id.navPost);
        iconPost = findViewById(R.id.iconPost);
        labelPost = findViewById(R.id.labelPost);

        //admin icon
        ImageView adminIcon = findViewById(R.id.imageView5);
        adminIcon.setOnClickListener(v -> {
            Intent intent = new Intent(Homepage.this, com.example.COSC341Task3.task4.adminReportPage.class);

            if (lastReportedUser != null) {
                intent.putExtra("username", lastReportedUser);
                intent.putExtra("postText", lastReportedPost);
                intent.putExtra("reason", lastReportedReason);
            }

            startActivity(intent);
        });

        // ----- POST BUTTON (same as before) -----
        navPost.setOnClickListener(v -> {
            if (!isPostArmed) {
                isPostArmed = true;

                navPost.setBackgroundResource(R.drawable.post_background);

                iconPost.setImageResource(R.drawable.pencil_icon);
                int white = ContextCompat.getColor(this, android.R.color.white);
                iconPost.setImageTintList(ColorStateList.valueOf(white));
                labelPost.setTextColor(white);

            } else {
                // SECOND TAP: later -> open CreatePostActivity
                // Intent intent = new Intent(this, CreatePostActivity.class);
                // startActivity(intent);
            }
        });

        // ----- HOME BUTTON -----
        navHome.setOnClickListener(v -> {
            resetPostButtonState();
            // we're already on Homepage, so no navigation needed
            // you could highlight the tab here if you want
        });

        // ----- SHOP BUTTON -----
        navShop.setOnClickListener(v -> {
            resetPostButtonState();
            Intent intent = new Intent(Homepage.this, MarketplaceActivity.class);
            startActivity(intent);
        });

        // ----- SETTINGS BUTTON -----
        navSettings.setOnClickListener(v -> {
            resetPostButtonState();
            // TODO: go to SettingsActivity when/if you have one
        });

        // tap anywhere else -> reset post state
        mainLayout.setOnClickListener(v -> resetPostButtonState());
    }



    private void resetPostButtonState() {
        if (!isPostArmed) return;

        isPostArmed = false;

        // remove green pill background
        navPost.setBackground(null);

        // back to original plus icon
        iconPost.setImageResource(R.drawable.add_icon);

        // use @color/green_theme instead of hex
        int iconGreen = ContextCompat.getColor(this, R.color.green_theme);
        iconPost.setImageTintList(ColorStateList.valueOf(iconGreen));

        int labelColor = ContextCompat.getColor(this, R.color.text_secondary);
        labelPost.setTextColor(labelColor);
    }
}