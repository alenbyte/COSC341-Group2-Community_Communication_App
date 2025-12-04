package com.example.COSC341Task3.COSC341Task1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;
import androidx.appcompat.app.AppCompatActivity;


import com.example.COSC341Task3.R;

import java.util.ArrayList;

public class community_main extends AppCompatActivity {
    private CommunityAdapter adapter;
    private ArrayList<Community> communityList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_main);
        ListView communitiesListView = findViewById(R.id.communitiesListView);
        ImageButton btnBack = findViewById(R.id.btnClose3);
        SearchView searchView = findViewById(R.id.communitySearch);
        ArrayList<Community> communityList = new ArrayList<>();


        communityList.add(new Community("Kelowna Mountain Bikers", "For all riders passionate about the awesome singletrack and downhill runs around Kelowna. We organize group rides for all skill levels, from Crawford trails to Smith Creek. This is the place to discuss bike maintenance, share trail conditions, and post epic videos of your rides."));

        communityList.add(new Community("Okanagan Valley Painters", "Whether you work with oils, acrylics, or watercolors, this is your space to connect with fellow painters. We share our latest artwork, discuss techniques, and offer constructive feedback. We also organize monthly 'en plein air' painting sessions at scenic spots around the valley, from wineries to lakefront parks."));

        communityList.add(new Community("Local Makers & Artisans", "A community for Kelowna's crafters, artists, and makers to connect and collaborate. Show off your handmade goods, from pottery and jewelry to custom clothing and art prints. We share tips on selling at local markets, sourcing materials, and growing a small creative business in the Okanagan."));

        communityList.add(new Community("Beginner & Casual Volleyball", "Looking to play volleyball in a fun, low-pressure environment? We organize weekly drop-in games at City Park and local school gyms, depending on the season. This group is perfect for beginners who want to learn the game or experienced players looking for a casual match. It's all about getting active and having a good time."));

        communityList.add(new Community("Cryptocurrency & Web3 Discussion", "A group dedicated to exploring the world of blockchain, cryptocurrency, and decentralized applications. We discuss market trends, new projects, and the underlying technology of Web3. This is a place for both newcomers to ask basic questions and for veterans to have in-depth conversations about the future of finance and the internet."));

        communityList.add(new Community("Kelowna Dog Owners & Playdates", "Connect with other dog lovers in Kelowna to organize park playdates and group walks. Share your favorite dog-friendly trails, ask for recommendations on vets or groomers, and post adorable pictures of your furry friends. This group helps ensure our dogs are well-socialized and happy."));

        communityList.add(new Community("Sustainable Living Okanagan", "Join us to explore ways to live a more eco-friendly lifestyle in the Okanagan. We share tips on reducing waste, composting, conserving water, and supporting local, sustainable businesses. This is a positive space to learn from each other and make a collective impact on preserving the beauty of our valley."));

        communityList.add(new Community("Home Theatre & Audiophiles", "For those who are obsessed with getting the best possible picture and sound from their home entertainment setup. Discuss the latest in 4K projectors, Dolby Atmos receivers, and high-fidelity speakers. We share photos of our setups, help each other with calibration, and geek out over movie sound mixes."));

        communityList.add(new Community("Rock Climbing & Bouldering", "Connect with climbers in the Kelowna area to find belay partners and plan trips to local crags like the Skaha Bluffs. We welcome climbers of all levels, from those just starting at Gneiss or Beyond the Crux to experienced outdoor leaders. Safety is our priority, and we focus on building a supportive and knowledgeable climbing community."));

        communityList.add(new Community("Brewery Hoppers", "A group for exploring the ever-growing craft beer scene in Kelowna and the surrounding area. We organize casual meetups at different breweries to sample new releases and support local businesses. Share your reviews of the latest IPAs, sours, and lagers. Cheers to good beer and good company!"));


        CommunityAdapter adapter = new CommunityAdapter(this, communityList);
        ImageButton btnDiscover = findViewById(R.id.btnDiscover);
        communitiesListView.setAdapter(adapter);

        communitiesListView.setOnItemClickListener((parent, view, position, id) -> {
            Community selectedCommunity = adapter.getItem(position);

            Intent intent = new Intent(community_main.this, community_view.class);
            intent.putExtra("community_details", selectedCommunity);
            startActivity(intent);
        });
        btnDiscover.setOnClickListener(v -> {
            Intent intent = new Intent(community_main.this, Communities_discover.class);
            startActivity(intent);
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
        btnBack.setOnClickListener(v -> {
            finish();
        });
    }
    }



