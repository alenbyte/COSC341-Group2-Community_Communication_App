package com.example.COSC341Task3.Task5;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.COSC341Task3.R;
import java.util.List;
import java.util.Locale;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.LocationViewHolder> {

    // The adapter should only manage one list: the one it displays.
    private final List<Location> locationList;

    public LocationAdapter(List<Location> locationList) {
        this.locationList = locationList;
    }

    // REMOVE the filter() and updateOriginalList() methods from here.
    // The Activity will now handle filtering and updating the adapter's list directly.

    @NonNull
    @Override
    public LocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_location, parent, false);
        return new LocationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationViewHolder holder, int position) {
        Location location = locationList.get(position);
        holder.locationName.setText(location.getName());
        holder.locationDescription.setText(location.getDescription());

        float distance = location.getDistanceInMeters();
        if (distance >= 0) {
            if (distance > 1000) {
                holder.locationDistance.setText(String.format(Locale.getDefault(), "%.2f km", distance / 1000));
            } else {
                holder.locationDistance.setText(String.format(Locale.getDefault(), "%.0f m", distance));
            }
            holder.locationDistance.setVisibility(View.VISIBLE);
        } else {
            holder.locationDistance.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(v -> {
            Context context = v.getContext();
            Intent intent = new Intent(context, LocationDetailActivity.class);
            intent.putExtra(LocationDetailActivity.EXTRA_LOCATION, location);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return locationList.size();
    }

    public static class LocationViewHolder extends RecyclerView.ViewHolder {
        TextView locationName;
        TextView locationDescription;
        TextView locationDistance;

        public LocationViewHolder(@NonNull View itemView) {
            super(itemView);
            locationName = itemView.findViewById(R.id.locationName);
            locationDescription = itemView.findViewById(R.id.locationDescription);
            locationDistance = itemView.findViewById(R.id.locationDistance);
        }
    }
}
