package com.example.turfly;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EditProfileActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        usernameEditText = findViewById(R.id.username_edit_text);
        saveButton = findViewById(R.id.save_button);

        // Assuming you have a method to get the current username
        String currentUsername = "User Name"; // Replace with actual username retrieval
        usernameEditText.setText(currentUsername);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newUsername = usernameEditText.getText().toString();
                if (!newUsername.isEmpty()) {
                    // TODO: Save new username to Firebase or local storage
                    Toast.makeText(EditProfileActivity.this, "Username updated to: " + newUsername, Toast.LENGTH_SHORT).show();
                    finish(); // Close the activity after saving
                } else {
                    Toast.makeText(EditProfileActivity.this, "Username cannot be empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
