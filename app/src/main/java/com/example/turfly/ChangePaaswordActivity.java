package com.example.turfly;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.AuthCredential;

public class ChangePaaswordActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText, newPasswordEditText;
    private Button changePasswordButton;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_paasword);

        emailEditText = findViewById(R.id.email_edit_text); // New EditText for email
        passwordEditText = findViewById(R.id.password_edit_text); // New EditText for current password
        newPasswordEditText = findViewById(R.id.new_password_edit_text);
        changePasswordButton = findViewById(R.id.change_password_button);
        mAuth = FirebaseAuth.getInstance();

        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();
                String newPassword = newPasswordEditText.getText().toString().trim();

                if (!email.isEmpty() && !password.isEmpty() && !newPassword.isEmpty()) {
                    FirebaseUser user = mAuth.getCurrentUser();
                    if (user != null) {
                        // Re-authenticate the user
                        AuthCredential credential = EmailAuthProvider.getCredential(email, password);
                        user.reauthenticate(credential)
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        // Proceed to change password
                                        user.updatePassword(newPassword)
                                                .addOnCompleteListener(updateTask -> {
                                                    if (updateTask.isSuccessful()) {
                                                        Toast.makeText(ChangePaaswordActivity.this, "Password changed successfully", Toast.LENGTH_SHORT).show();
                                                        finish(); // Close the activity after changing password
                                                    } else {
                                                        Log.e("ChangePassword", "Error changing password", updateTask.getException());
                                                        Toast.makeText(ChangePaaswordActivity.this, "Error changing password: " + updateTask.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    } else {
                                        Log.e("ChangePassword", "Error re-authenticating", task.getException());
                                        Toast.makeText(ChangePaaswordActivity.this, "Error re-authenticating: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else {
                        Toast.makeText(ChangePaaswordActivity.this, "User not signed in", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ChangePaaswordActivity.this, "Fields cannot be empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
