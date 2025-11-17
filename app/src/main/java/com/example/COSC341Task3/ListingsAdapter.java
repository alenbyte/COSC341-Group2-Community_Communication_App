package com.example.COSC341Task3;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ListingsAdapter extends RecyclerView.Adapter<ListingsAdapter.ListingViewHolder> {

    private List<Listing> listings;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Listing listing);
    }

    public ListingsAdapter(List<Listing> listings, OnItemClickListener listener) {
        this.listings = listings;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ListingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_listing_card, parent, false);
        return new ListingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListingViewHolder holder, int position) {
        Listing listing = listings.get(position);
        holder.bind(listing, listener);
    }

    @Override
    public int getItemCount() {
        return listings.size();
    }

    static class ListingViewHolder extends RecyclerView.ViewHolder {
        private TextView titleTextView;
        private TextView priceTextView;
        private ImageView imageView;
        private ImageView placeholderIcon;

        public ListingViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.itemTitle);
            priceTextView = itemView.findViewById(R.id.itemPrice);
            imageView = itemView.findViewById(R.id.itemImage);
            placeholderIcon = itemView.findViewById(R.id.placeholderIcon);
        }

        public void bind(Listing listing, OnItemClickListener listener) {
            titleTextView.setText(listing.getTitle());
            priceTextView.setText(listing.getFormattedPrice());

            // Since we're using mock data without actual images, show placeholder
            imageView.setVisibility(View.GONE);
            placeholderIcon.setVisibility(View.VISIBLE);

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemClick(listing);
                }
            });
        }
    }
}

