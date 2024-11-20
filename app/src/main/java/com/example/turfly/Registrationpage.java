package com.example.turfly;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class Registrationpage extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registrationpage);

        EditText username = findViewById(R.id.inputUsername);
        EditText email = findViewById(R.id.inputEmail);
        EditText password = findViewById(R.id.inputPassword);
        EditText repassword = findViewById(R.id.inputConfirmPassword);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        TextView btn = findViewById(R.id.alreadyHaveAccount);
        Button register = findViewById(R.id.btnRegister);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = username.getText().toString();
                String mail = email.getText().toString();
                String pass = password.getText().toString();
                String repass = repassword.getText().toString();

                if (user.isEmpty() || mail.isEmpty() || pass.isEmpty() || repass.isEmpty()) {
                    Toast.makeText(Registrationpage.this, "Please enter all details", Toast.LENGTH_SHORT).show();
                } else if (!pass.equals(repass)) {
                    Toast.makeText(Registrationpage.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(mail).matches()) {
                    Toast.makeText(Registrationpage.this, "Invalid email format", Toast.LENGTH_SHORT).show();
                } else {
                    // Register user with Firebase Authentication
                    mAuth.createUserWithEmailAndPassword(mail, pass)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    // Get the user ID
                                    String userId = mAuth.getCurrentUser().getUid();

                                    // Create a user object to store in Firestore
                                    User newUser = new User(user, mail);

                                    // Add user data to Firestore
                                    db.collection("users").document(userId)
                                            .set(newUser)
                                            .addOnSuccessListener(aVoid -> {
                                                Toast.makeText(Registrationpage.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(getApplicationContext(), Homepage.class);
                                                startActivity(intent);
                                                finish(); // Close the registration activity
                                            })
                                            .addOnFailureListener(e -> {
                                                Toast.makeText(Registrationpage.this, "Failed to add user data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                            });
                                } else {
                                    Toast.makeText(Registrationpage.this, "Registration Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Registrationpage.this, Loginpage.class));
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}

// User class to hold user data
class User {
    String username;
    String email;

    User(String username, String email) {
        this.username = username;
        this.email = email;
    }
}
