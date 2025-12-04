package com.example.COSC341Task3.Task2;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.COSC341Task3.R;
import com.example.COSC341Task3.task4.ReportUserPage;
import android.app.AlertDialog;
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
            holder.userName.setText("Your Post");

            holder.ownerBellIcon.setVisibility(View.VISIBLE);
            holder.ownerDeleteIcon.setVisibility(View.VISIBLE);
            holder.ownerEditIcon.setVisibility(View.VISIBLE);

            // bell icon appearance depends on state
            if (post.isNotificationsOn()) {
                holder.ownerBellIcon.setImageResource(R.drawable.bell_icon);
                holder.ownerBellIcon.setAlpha(1.0f);
            } else {
                holder.ownerBellIcon.setImageResource(R.drawable.bell_off_icon);
                holder.ownerBellIcon.setAlpha(0.5f);
            }
        } else {
            holder.ownerBellIcon.setVisibility(View.GONE);
            holder.ownerDeleteIcon.setVisibility(View.GONE);
            holder.ownerEditIcon.setVisibility(View.GONE);
        }

        // report always visible (you decided that’s fine)
        holder.reportIcon.setVisibility(View.VISIBLE);

        // --- LIKE ICON STATE ---
        if (post.isLiked()) {
            holder.likeIcon.setImageResource(R.drawable.ic_like_filled);
        } else {
            holder.likeIcon.setImageResource(R.drawable.like_icon);
        }

        // --- BOOKMARK ICON STATE ---
        if (post.isSaved()) {

            holder.bookmarkIcon.setImageResource(R.drawable.ic_bookmark_filled);
        } else {
            holder.bookmarkIcon.setImageResource(R.drawable.bookmark_icon);
        }

        // --------- CLICK HANDLERS ---------

        // report → ReportUserPage (same as before)
        holder.reportIcon.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), ReportUserPage.class);
            intent.putExtra("username", post.getUserName());
            intent.putExtra("postText", post.getText());
            v.getContext().startActivity(intent);
        });

        // tap post → detail view (same as before)
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

        // DELETE (trash) icon for owned posts
        holder.ownerDeleteIcon.setOnClickListener(v -> {
            int pos = holder.getAdapterPosition();
            if (pos == RecyclerView.NO_POSITION) return;

            new AlertDialog.Builder(v.getContext())
                    .setTitle("Delete post")
                    .setMessage("Are you sure you want to delete this post?")
                    .setPositiveButton("Delete", (dialog, which) -> {
                        posts.remove(pos);
                        notifyItemRemoved(pos);
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });
        // EDIT (pencil) icon for owned posts
        holder.ownerEditIcon.setOnClickListener(v -> {
            int pos = holder.getAdapterPosition();
            if (pos == RecyclerView.NO_POSITION) return;

            Post p = posts.get(pos);

            // create an EditText pre-filled with current text
            EditText input = new EditText(v.getContext());
            input.setText(p.getText());
            // put cursor at end
            input.setSelection(input.getText().length());

            new AlertDialog.Builder(v.getContext())
                    .setTitle("Edit post")
                    .setView(input)
                    .setPositiveButton("Save", (dialog, which) -> {
                        String newText = input.getText().toString().trim();
                        if (!newText.isEmpty()) {
                            p.setText(newText);
                            notifyItemChanged(pos);
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });

        // BELL TOGGLE on feed (owner only)
        holder.ownerBellIcon.setOnClickListener(v -> {
            int pos = holder.getAdapterPosition();
            if (pos == RecyclerView.NO_POSITION) return;

            Post p = posts.get(pos);
            boolean newState = !p.isNotificationsOn();
            p.setNotificationsOn(newState);
            notifyItemChanged(pos);
        });

        // LIKE TOGGLE (everyone)
        holder.likeIcon.setOnClickListener(v -> {
            int pos = holder.getAdapterPosition();
            if (pos == RecyclerView.NO_POSITION) return;

            Post p = posts.get(pos);
            p.setLiked(!p.isLiked());
            notifyItemChanged(pos);
        });

        // BOOKMARK TOGGLE (everyone)
        holder.bookmarkIcon.setOnClickListener(v -> {
            int pos = holder.getAdapterPosition();
            if (pos == RecyclerView.NO_POSITION) return;

            Post p = posts.get(pos);
            p.setSaved(!p.isSaved());
            notifyItemChanged(pos);
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
        ImageView ownerBellIcon;
        ImageView ownerDeleteIcon;
        ImageView ownerEditIcon;
        ImageView likeIcon;


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
            likeIcon = itemView.findViewById(R.id.likeIcon);
        }
    }

}