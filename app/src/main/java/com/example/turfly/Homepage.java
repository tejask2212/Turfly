package com.example.turfly;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Homepage extends AppCompatActivity {

    private RecyclerView turfRecyclerView;
    private TurfAdapter turfAdapter;
    private List<Turf> turfList;
    private EditText searchBox;
    private ImageButton profileButton;
    private ImageView profilePicture;
    private ProfilePictureManager profilePictureManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        // Initialize views
        searchBox = findViewById(R.id.search_box);
        profilePicture = findViewById(R.id.profile_button); // Use the correct view ID
        profilePictureManager = new ProfilePictureManager(this); // Initialize ProfilePictureManager

        loadProfilePicture(); // Load profile picture from ProfilePictureManager

        // Handle Profile Button Click
        profileButton = findViewById(R.id.profile_button);
        profileButton.setOnClickListener(v -> {
            Intent intent = new Intent(Homepage.this, ProfileActivity.class);
            startActivity(intent);
        });

        // Initialize RecyclerView for turf listing
        turfRecyclerView = findViewById(R.id.turf_recycler_view);
        turfRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Load Turf Data
        turfList = new ArrayList<>();
        loadTurfData(); // Populate turf data

        // Set up the adapter
        turfAdapter = new TurfAdapter(turfList, this); // Pass context to the adapter
        turfRecyclerView.setAdapter(turfAdapter);

        // Add TextWatcher to the search box for real-time search
        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                turfAdapter.filter(charSequence.toString()); // Call the filter method in adapter
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadProfilePicture(); // Load the updated profile picture when returning to the homepage
    }

    // Load the profile picture from ProfilePictureManager
    private void loadProfilePicture() {
        Uri uri = profilePictureManager.loadProfilePictureUri();
        Log.d("Homepage", "Loaded URI: " + uri); // Log the URI

        if (uri != null) {
            Glide.with(this)
                    .load(uri) // Load the profile picture
                    .error(R.drawable.baseline_person_24) // Default image in case of error
                    .into(profilePicture); // Set it to the ImageView
        } else {
            profilePicture.setImageResource(R.drawable.baseline_person_24); // Set default image if no URI
        }
    }

    // Updated method to load turf data with unique names, descriptions, and images
    private void loadTurfData() {
        // List of unique turf names and descriptions
        String[] turfNames = {
                "The Foothill Arena",
                "Hindu Gymkhana",
                "Nutmeg Arena",
                "Corner Kick",
                "Dei Gratia Sports Club"
        };

        String[] turfDescriptions = {
                "MIT, Kothrud",
                "Vanaz, Kothrud",
                "Pimple Saudagar",
                "Ghorpadi, Hadapsar",
                "Clover Park, Viman Nagar"
        };

        // List of turf images (replace with actual image resource IDs)
        int[] turfImages = {
                R.drawable.foothill,
                R.drawable.hindugymkhana,
                R.drawable.nutmeg,
                R.drawable.cornerkick,
                R.drawable.dei
        };

        // Add turf data to the list
        for (int i = 0; i < turfNames.length; i++) {
            turfList.add(new Turf(i + 1, turfNames[i], turfDescriptions[i], turfImages[i]));
        }
    }
}
