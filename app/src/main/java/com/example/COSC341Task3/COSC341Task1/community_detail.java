package com.example.COSC341Task3.COSC341Task1;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.COSC341Task3.R;

import java.util.ArrayList;

public class community_detail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_detail);

        TextView tvName = findViewById(R.id.cname);
        TextView tvDescription = findViewById(R.id.cdesc);
        ImageButton btnClose = findViewById(R.id.btnCloseReport);
        Button joinButton = findViewById(R.id.joinButton);
        Button reportButton = findViewById(R.id.reportButton);
        ListView postsListView = findViewById(R.id.postsListView);

        Community community = (Community) getIntent().getSerializableExtra("community_details");
        joinButton.setBackgroundColor(Color.parseColor("#4CAF50"));
        joinButton.setText("Join Community");
        if (community != null) {
            tvName.setText(community.getName());
            tvDescription.setText(community.getDescription());
        }

        btnClose.setOnClickListener(v -> {
            finish();
        });

        joinButton.setOnClickListener(v ->{
            if(joinButton.getText().toString().equals("Join Community")){
                joinButton.setText("Leave Community");
                joinButton.setBackgroundColor(Color.parseColor("#F44336"));


            }else{
                joinButton.setText("Join Community");
                joinButton.setBackgroundColor(Color.parseColor("#4CAF50"));

            }
        });
        reportButton.setOnClickListener(v -> {
            Intent intent = new Intent(community_detail.this, community_report.class);
            startActivity(intent);
        });
        ArrayList<CommunityPost> postList = new ArrayList<>();
        postList.add(new CommunityPost("First Ever Post!", "Welcome everyone to our new community. So glad to have you here."));
        postList.add(new CommunityPost("Upcoming Event", "Don't forget our first meetup next Tuesday at the park."));
        postList.add(new CommunityPost("Looking for advice", "Does anyone have experience with this particular topic? I'd love to hear your thoughts."));
        postList.add(new CommunityPost("Great resource I found", "Check out this link, it's super helpful for beginners!"));

        CommunityPostAdapter postAdapter = new CommunityPostAdapter(this, postList);

        postsListView.setAdapter(postAdapter);
    }
}
