package com.example.COSC341Task1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;


import com.example.COSC341Task3.R;

import java.util.ArrayList;

public class Communities_discover extends AppCompatActivity {
    private CommunityAdapter adapter;
    private ArrayList<Community> communityList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_communities_discover);
        ListView communitiesListView = findViewById(R.id.communitiesListView);
        ImageButton back = findViewById(R.id.btnClose3);
        SearchView searchView = findViewById(R.id.discoverSearch);
        ArrayList<Community> communityList = new ArrayList<>();

        communityList.add(new Community("Sunset Photographers", "A group for those who love chasing the golden hour. Share your best sunset and sunrise photos from around the area. We also discuss gear, editing techniques, and scout new locations for the perfect shot. Once a month, we organize a group photo walk to capture the evening light together."));

        communityList.add(new Community("Kelowna Trail Hikers", "Join us as we explore the beautiful trails and natural parks in our region. Our hikes range from easy afternoon walks to challenging all-day treks, so there's something for every skill level. We post trail conditions, share photos from our adventures, and plan group hikes every weekend, weather permitting. It's a great way to stay active and meet fellow nature lovers."));

        communityList.add(new Community("Budget Meal Preppers", "Discover how to eat healthy and save money by planning your meals. In this community, we share our favorite budget-friendly recipes, weekly meal plans, and tips for efficient grocery shopping. Learn how to cook in bulk and make your food last longer. We believe delicious and nutritious food doesn't have to break the bank."));

        communityList.add(new Community("Indie Game Developers", "A collaborative space for aspiring and experienced indie game developers. Here, we share our works-in-progress, give and receive feedback, and discuss everything from pixel art to game engine performance. We also post resources for learning, game jam announcements, and celebrate each other's project launches. Let's build the next great indie hit together."));

        communityList.add(new Community("Urban Gardening Group", "Turn your balcony, patio, or windowsill into a green oasis. This group is for anyone interested in growing plants in an urban environment, from herbs and vegetables to beautiful flowers. We exchange tips on container gardening, soil health, and pest control. Share your progress, ask questions, and let's grow together."));

        communityList.add(new Community("Kelowna Sci-fi Book Club", "Let's journey to other worlds and explore as imagined by the masters of science fiction. Each month, we select a classic novel from authors like Asimov, Herbert, or Le Guin for discussion. We delve into the themes, characters, and the impact these stories have had on literature and culture. Join our thoughtful conversations about the genre that dares to ask 'what if?'."));

        communityList.add(new Community("DIY Woodworking Projects", "For anyone who loves the smell of sawdust and the satisfaction of building something with their own hands. This community is a place to showcase your woodworking projects, from simple birdhouses to intricate furniture. We share plans, ask for advice on challenging cuts, and discuss the best tools for the job. All skill levels are welcome to join and learn."));

        communityList.add(new Community("Board Game Enthusiasts", "Tired of the same old games? Join our club to discover new and exciting board games, from complex strategy epics to hilarious party games. We host a weekly game night at the local library where you can meet new people and try out games from our shared collection. No experience is necessary, just a willingness to learn and have fun."));

        communityList.add(new Community("3D Printing Hobbyists", "This is a hub for everything related to 3D printing. Share your latest creations, ask for help troubleshooting a failed print, and discuss the newest filament types and printer models. We help each other optimize slicer settings and share cool models we find online. Whether you're a beginner or an expert, you'll find valuable insight here."));

        communityList.add(new Community("Amateur Astronomers", "Explore the cosmos with a group of fellow stargazers. We share astrophotography, discuss recent astronomical news, and offer advice on choosing telescopes and binoculars. During clear nights and meteor showers, we organize observation meetups away from the city lights. Come and learn the constellations with us."));


        CommunityAdapter adapter = new CommunityAdapter(this, communityList);
        communitiesListView.setAdapter(adapter);
        communitiesListView.setOnItemClickListener((parent, view, position, id) -> {
            Community selectedCommunity = adapter.getItem(position);
            Intent intent = new Intent(Communities_discover.this, community_detail.class);
            intent.putExtra("community_details", selectedCommunity);
            startActivity(intent);
        });
        back.setOnClickListener(v ->{
            finish();
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // This is where the filtering happens
                adapter.getFilter().filter(newText);
                return false;
            }
        });
    }
    }


