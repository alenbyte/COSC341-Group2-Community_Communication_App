package com.example.COSC341Task1;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.example.COSC341Task3.R;

import java.util.ArrayList;

public class community_view extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_view);

        TextView tvName = findViewById(R.id.viewCommunityName);
        TextView tvDescription = findViewById(R.id.viewCommunityDescription);
        ImageButton btnClose = findViewById(R.id.btnCloseView);
        ListView posts = findViewById(R.id.communityPostsListView);

        Button joinButton = findViewById(R.id.joinButtonView);
        Button reportButton = findViewById(R.id.reportButtonView);

        Community community = (Community) getIntent().getSerializableExtra("community_details");

        if (community != null) {
            tvName.setText(community.getName());
            tvDescription.setText(community.getDescription());
        }

        joinButton.setText("Leave Community");
        joinButton.setBackgroundColor(Color.parseColor("#F44336"));

        btnClose.setOnClickListener(v -> {
            finish();
        });

        joinButton.setOnClickListener(v -> {
            if (joinButton.getText().toString().equals("Leave Community")) {

                Toast.makeText(community_view.this, "You have left the community", Toast.LENGTH_SHORT).show();
                finish();
            }

        });

        reportButton.setOnClickListener(v -> {
            Intent intent = new Intent(community_view.this, community_report.class);
            startActivity(intent);
        });


        ArrayList<CommunityPost> postList = new ArrayList<>();
        postList.add(new CommunityPost("Welcome to " + (community != null ? community.getName() : "the Community") + "!", "This is the first post. Feel free to introduce yourselves."));
        postList.add(new CommunityPost("Weekly Discussion Thread", "What's on your mind this week? Share your thoughts and ideas below."));
        postList.add(new CommunityPost("Question about our next event", "Does anyone know if the time for the Saturday meetup has been confirmed?"));
        postList.add(new CommunityPost("A great article I wanted to share", "I found this resource online and thought it would be perfect for our group's interests."));

        CommunityPostAdapter postAdapter = new CommunityPostAdapter(this, postList);

        posts.setAdapter(postAdapter);
    }
}
