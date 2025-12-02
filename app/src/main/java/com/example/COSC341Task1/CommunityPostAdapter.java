package com.example.COSC341Task1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.COSC341Task3.R;
import java.util.ArrayList;

public class CommunityPostAdapter extends ArrayAdapter<CommunityPost> {

    public CommunityPostAdapter(Context context, ArrayList<CommunityPost> postList) {
        super(context, 0, postList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        CommunityPost post = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.community_post_item, parent, false);
        }

        TextView tvTitle = convertView.findViewById(R.id.postTitle);
        TextView tvContent = convertView.findViewById(R.id.postContent);

        if (post != null) {
            tvTitle.setText(post.getTitle());
            tvContent.setText(post.getContent());
        }

        return convertView;
    }
}
