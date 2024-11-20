package com.example.turfly;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileActivity extends AppCompatActivity {

    private static final int PICK_IMAGE = 1;
    private ImageView profilePicture;
    private TextView usernameTextView; // Declare TextView for username
    private ProfilePictureManager profilePictureManager;
    private FirebaseAuth mAuth; // FirebaseAuth instance
    private FirebaseFirestore db; // Firebase Firestore instance

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize Firebase Auth and Firestore
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        profilePicture = findViewById(R.id.profile_picture);
        usernameTextView = findViewById(R.id.usernameTextView); // Initialize the username TextView
        profilePictureManager = new ProfilePictureManager(this);

        loadProfilePicture(); // Load profile picture from ProfilePictureManager
        loadUserData(); // Fetch and display user data

        // Set click listener to select image
        profilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, PICK_IMAGE);
            }
        });

        // Handle Logout Button Click
        LinearLayout logoutButton = findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut(); // Firebase sign out
                Intent intent = new Intent(ProfileActivity.this, Loginpage.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear activity stack
                startActivity(intent);
                finish(); // Close ProfileActivity
                Toast.makeText(ProfileActivity.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
            }
        });

        // Handle Edit Profile Button Click
        LinearLayout editProfileButton = findViewById(R.id.editProfileButton);
        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
                startActivity(intent);
            }
        });

        // Handle Change Password Button Click
        LinearLayout changePasswordButton = findViewById(R.id.changePasswordButton);
        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, ChangePaaswordActivity.class);
                startActivity(intent);
            }
        });
    }

    private void loadProfilePicture() {
        Uri uri = profilePictureManager.loadProfilePictureUri();
        if (uri != null) {
            Log.d("ProfileActivity", "Loaded URI: " + uri.toString());
            Glide.with(this).load(uri).into(profilePicture); // Load image using Glide
        } else {
            Log.d("ProfileActivity", "No URI found for profile picture.");
            profilePicture.setImageResource(R.drawable.baseline_person_24); // Use the vector image as default
        }
    }

    private void loadUserData() {
        String userId = mAuth.getCurrentUser().getUid(); // Get the current user ID
        db.collection("users").document(userId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Fetch username from Firestore and set it to the TextView
                        String username = documentSnapshot.getString("username");
                        usernameTextView.setText(username); // Set the retrieved username
                    } else {
                        Log.d("ProfileActivity", "No user data found.");
                    }
                })
                .addOnFailureListener(e -> {
                    Log.d("ProfileActivity", "Error getting user data: " + e.getMessage());
                    Toast.makeText(ProfileActivity.this, "Failed to load user data", Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            if (imageUri != null) {
                Log.d("ProfileActivity", "Selected image URI: " + imageUri.toString());
                Glide.with(this)
                        .load(imageUri) // Load the selected image
                        .into(profilePicture); // Set it to the ImageView
                profilePictureManager.saveProfilePictureUri(imageUri); // Save the selected image URI
            } else {
                Toast.makeText(this, "Failed to get image", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
