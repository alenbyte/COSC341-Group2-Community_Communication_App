package com.example.COSC341Task3.Task2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.COSC341Task3.R;

public class PostDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // use the new centered layout
        setContentView(R.layout.activity_post_detail);

        TextView userNameView = findViewById(R.id.User);
        TextView postTextView = findViewById(R.id.postText);
        ImageView postImageView = findViewById(R.id.postImage);
        ImageView closeDetail = findViewById(R.id.iconCloseDetail);
        closeDetail.setOnClickListener(v -> finish());

        Intent intent = getIntent();
        String userName = intent.getStringExtra("userName");
        String text = intent.getStringExtra("text");
        int imageResId = intent.getIntExtra("imageResId", 0);

        userNameView.setText(userName);
        postTextView.setText(text);

        if (imageResId != 0) {
            postImageView.setVisibility(View.VISIBLE);
            postImageView.setImageResource(imageResId);
        } else {
            postImageView.setVisibility(View.GONE);
        }
    }
}
