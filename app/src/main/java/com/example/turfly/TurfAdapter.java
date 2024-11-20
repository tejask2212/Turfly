package com.example.turfly;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class TurfAdapter extends RecyclerView.Adapter<TurfAdapter.TurfViewHolder> {

    private List<Turf> turfList; // Current filtered list
    private List<Turf> turfListFull; // Original list for filtering
    private Context context; // To hold the context for launching intents

    public TurfAdapter(List<Turf> turfList, Context context) {
        this.turfList = turfList;
        this.turfListFull = new ArrayList<>(turfList); // Initialize the full list
        this.context = context; // Set the context
    }

    @NonNull
    @Override
    public TurfViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_turf, parent, false);
        return new TurfViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TurfViewHolder holder, int position) {
        Turf turf = turfList.get(position);
        holder.bind(turf);
    }

    @Override
    public int getItemCount() {
        return turfList.size();
    }

    // Updated filter method to search by name and description
    public void filter(String text) {
        turfList.clear();
        if (text.isEmpty()) {
            turfList.addAll(turfListFull); // Show all items
        } else {
            for (Turf turf : turfListFull) {
                // Check if the name or description contains the search text
                if (turf.getName().toLowerCase().contains(text.toLowerCase()) ||
                        turf.getDescription().toLowerCase().contains(text.toLowerCase())) {
                    turfList.add(turf);
                }
            }
        }
        notifyDataSetChanged(); // Notify adapter of data change
    }

    static class TurfViewHolder extends RecyclerView.ViewHolder {

        ImageView turfImageView;
        TextView turfNameTextView, turfDescriptionTextView;

        public TurfViewHolder(@NonNull View itemView) {
            super(itemView);
            turfImageView = itemView.findViewById(R.id.turf_image);
            turfNameTextView = itemView.findViewById(R.id.turf_name);
            turfDescriptionTextView = itemView.findViewById(R.id.turf_description);
        }

        public void bind(final Turf turf) {
            turfNameTextView.setText(turf.getName());
            turfDescriptionTextView.setText(turf.getDescription());
            turfImageView.setImageResource(turf.getImageResource());

            // Set an OnClickListener for the entire item
            itemView.setOnClickListener(v -> {
                // Create an intent to start TurfDetailActivity
                Intent intent = new Intent(v.getContext(), TurfDetailActivity.class);
                intent.putExtra("TURF_ID", turf.getId()); // Pass the turf ID to the detail activity
                v.getContext().startActivity(intent);
            });
        }
    }
}
