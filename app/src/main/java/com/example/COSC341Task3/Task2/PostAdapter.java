package com.example.COSC341Task3.Task2;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.COSC341Task3.R;
import com.example.COSC341Task3.task4.ReportUserPage;

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

        // default text
        holder.userName.setText(post.getUserName());
        holder.postText.setText(post.getText());

        // image visibility
        if (post.getImageResId() != null) {
            holder.postImage.setVisibility(View.VISIBLE);
            holder.postImage.setImageResource(post.getImageResId());
        } else {
            holder.postImage.setVisibility(View.GONE);
        }

        // --- OWNED vs NOT OWNED UI ---
        if (post.isOwnedByCurrentUser()) {
            // label as "Your Post" if you want that look
            holder.userName.setText("Your Post");

            holder.ownerBellIcon.setVisibility(View.VISIBLE);
            holder.ownerDeleteIcon.setVisibility(View.VISIBLE);
            holder.ownerEditIcon.setVisibility(View.VISIBLE);

            // you probably don't want to report yourself
            holder.reportIcon.setVisibility(View.GONE);

            // bell state (on/off) from create screen
            if (post.isNotificationsOn()) {
                holder.ownerBellIcon.setImageResource(R.drawable.bell_icon);
                holder.ownerBellIcon.setAlpha(1.0f);
            } else {
                holder.ownerBellIcon.setImageResource(R.drawable.bell_off_icon);
                holder.ownerBellIcon.setAlpha(0.5f);
            }
        } else {
            // normal posts: no owner controls
            holder.ownerBellIcon.setVisibility(View.GONE);
            holder.ownerDeleteIcon.setVisibility(View.GONE);
            holder.ownerEditIcon.setVisibility(View.GONE);
            holder.reportIcon.setVisibility(View.VISIBLE);
        }

        // existing report behaviour
        holder.reportIcon.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), ReportUserPage.class);
            intent.putExtra("username", post.getUserName());
            intent.putExtra("postText", post.getText());
            v.getContext().startActivity(intent);
        });

        // existing “tap post → detail” behaviour
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(),
                    com.example.COSC341Task3.Task2.PostDetailActivity.class);
            intent.putExtra("userName", post.getUserName());
            intent.putExtra("text", post.getText());
            if (post.getImageResId() != null) {
                intent.putExtra("imageResId", post.getImageResId());
            }
            v.getContext().startActivity(intent);
        });

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
        ImageView reportIcon;

        // NEW
        ImageView ownerBellIcon;
        ImageView ownerDeleteIcon;
        ImageView ownerEditIcon;

        PostViewHolder(@NonNull View itemView) {
            super(itemView);
            profileIcon = itemView.findViewById(R.id.profileIcon);
            userName = itemView.findViewById(R.id.User);
            postText = itemView.findViewById(R.id.postText);
            postImage = itemView.findViewById(R.id.postImage);
            bookmarkIcon = itemView.findViewById(R.id.bookmarkIcon);
            reportIcon = itemView.findViewById(R.id.reportIcon);
            ownerBellIcon = itemView.findViewById(R.id.ownerBellIcon);
            ownerDeleteIcon = itemView.findViewById(R.id.ownerDeleteIcon);
            ownerEditIcon = itemView.findViewById(R.id.ownerEditIcon);
        }
    }

}