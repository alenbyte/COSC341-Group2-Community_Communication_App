package com.example.COSC341Task3.COSC341Task1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter; // Import Filter
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.COSC341Task3.R;
import java.util.ArrayList;
import java.util.List; // Import List

public class CommunityAdapter extends ArrayAdapter<Community> {

    private List<Community> originalList;
    private List<Community> filteredList;

    public CommunityAdapter(Context context, ArrayList<Community> communityList) {
        super(context, 0, communityList);
        this.originalList = new ArrayList<>(communityList);
        this.filteredList = new ArrayList<>(communityList);
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return new CommunityFilter();
    }

    @Override
    public int getCount() {
        return filteredList.size();
    }

    @Nullable
    @Override
    public Community getItem(int position) {
        return filteredList.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Community community = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.community_list_item, parent, false);
        }

        TextView tvName = convertView.findViewById(R.id.communityName);
        TextView tvDescription = convertView.findViewById(R.id.communityDescription);

        if (community != null) {
            tvName.setText(community.getName());
            tvDescription.setText(community.getDescription());
        }

        return convertView;
    }

    private class CommunityFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            String filterString = constraint == null ? "" : constraint.toString().toLowerCase();

            if (filterString.isEmpty()) {
                results.values = new ArrayList<>(originalList);
            } else {
                ArrayList<Community> newlyFilteredList = new ArrayList<>();
                for (Community community : originalList) {
                    if (community.getName().toLowerCase().contains(filterString) ||
                            community.getDescription().toLowerCase().contains(filterString)) {
                        newlyFilteredList.add(community);
                    }
                }
                results.values = newlyFilteredList;
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredList.clear();
            if (results.values != null) {
                filteredList.addAll((List) results.values);
            }
            notifyDataSetChanged();
        }
    }
}

