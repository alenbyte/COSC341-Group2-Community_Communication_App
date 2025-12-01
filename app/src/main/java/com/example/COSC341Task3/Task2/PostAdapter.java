package com.example.COSC341Task3.Task2;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.COSC341Task3.R;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private final List<Post> posts;

    public PostAdapter(List<Post> posts) {
        this.posts = posts;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_post, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = posts.get(position);

        holder.userName.setText(post.getUserName());
        holder.postText.setText(post.getText());

        // show/hide image depending on post type
        if (post.getImageResId() != null) {
            holder.postImage.setVisibility(View.VISIBLE);
            holder.postImage.setImageResource(post.getImageResId());
        } else {
            holder.postImage.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    static class PostViewHolder extends RecyclerView.ViewHolder {
        ImageView profileIcon;
        TextView userName;
        TextView postText;
        ImageView postImage;
        ImageView bookmarkIcon;

        PostViewHolder(@NonNull View itemView) {
            super(itemView);
            profileIcon = itemView.findViewById(R.id.profileIcon);
            userName = itemView.findViewById(R.id.User);
            postText = itemView.findViewById(R.id.postText);
            postImage = itemView.findViewById(R.id.postImage);
            bookmarkIcon = itemView.findViewById(R.id.bookmarkIcon);
        }
    }
}