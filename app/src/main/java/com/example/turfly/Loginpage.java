package com.example.turfly;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.app.AlertDialog;
import android.text.InputType;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class Loginpage extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private GoogleSignInClient googleSignInClient;
    private static final int RC_SIGN_IN = 9001; // Request code for Google Sign-In

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_loginpage);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Configure Google Sign-In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id)) // Make sure to replace with your web client ID from Firebase
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);

        // Check if user is already signed in
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // If the user is already signed in, redirect to the Homepage
            Intent intent = new Intent(Loginpage.this, Homepage.class);
            startActivity(intent);
            finish(); // Close the login activity
            return;
        }

        // If user is not signed in, show the login form
        EditText username = findViewById(R.id.inputLUsername);
        EditText password = findViewById(R.id.inputLPassword);
        Button lbtn = findViewById(R.id.btnlogin);
        Button googleSignInButton = findViewById(R.id.btnGoogle); // Button for Google Sign-In

        lbtn.setOnClickListener(view -> {
            String user = username.getText().toString();
            String pass = password.getText().toString();

            if (user.isEmpty() || pass.isEmpty()) {
                Toast.makeText(Loginpage.this, "Please fill all the details", Toast.LENGTH_SHORT).show();
            } else {
                // Sign in with Firebase
                mAuth.signInWithEmailAndPassword(user, pass)
                        .addOnCompleteListener(Loginpage.this, task -> {
                            if (task.isSuccessful()) {
                                // Sign in success
                                Toast.makeText(Loginpage.this, "Sign in Successful", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), Homepage.class);
                                startActivity(intent);
                                finish(); // Optional: Close the login activity
                            } else {
                                // If sign in fails
                                Toast.makeText(Loginpage.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        googleSignInButton.setOnClickListener(view -> signInWithGoogle()); // Set up Google Sign-In button listener

        TextView btn = findViewById(R.id.textViewSignUp);
        btn.setOnClickListener(view -> startActivity(new Intent(Loginpage.this, Registrationpage.class)));

        // New code for "Forgot Password?" functionality
        TextView forgotPassword = findViewById(R.id.forgotPassword);
        forgotPassword.setOnClickListener(view -> {
            // Create an EditText to input the email
            final EditText emailInput = new EditText(Loginpage.this);
            emailInput.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

            // Create a LinearLayout to hold the EditText
            LinearLayout layout = new LinearLayout(Loginpage.this);
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.addView(emailInput);

            // Create an AlertDialog to show the input
            new AlertDialog.Builder(Loginpage.this)
                    .setTitle("Reset Password")
                    .setMessage("Enter your email to receive a password reset link:")
                    .setView(layout)
                    .setPositiveButton("Send", (dialog, which) -> {
                        String email = emailInput.getText().toString();
                        if (!email.isEmpty()) {
                            // Send the password reset email
                            mAuth.sendPasswordResetEmail(email)
                                    .addOnCompleteListener(task -> {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(Loginpage.this,
                                                    "Password reset email sent.",
                                                    Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(Loginpage.this,
                                                    "Error: " + task.getException().getMessage(),
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            Toast.makeText(Loginpage.this, "Please enter your email.", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void signInWithGoogle() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            // Google Sign-In was successful, authenticate with Firebase
            firebaseAuthWithGoogle(account);
        } catch (ApiException e) {
            // Google Sign-In failed
            Toast.makeText(Loginpage.this, "Google Sign-In Failed", Toast.LENGTH_SHORT).show();
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        // Get the ID token from the Google account
        String idToken = account.getIdToken();
        if (idToken != null) {
            mAuth.signInWithCredential(GoogleAuthProvider.getCredential(idToken, null))
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            // Sign in success
                            Toast.makeText(Loginpage.this, "Google Sign-In Successful", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Loginpage.this, Homepage.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // If sign in fails
                            Toast.makeText(Loginpage.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}
