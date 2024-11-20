package com.example.turfly;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class SmsActivity extends AppCompatActivity {

    private TextView mobileNumberTextView;
    private EditText messageEditText;
    private Button sendSmsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);

        // Initialize views
        mobileNumberTextView = findViewById(R.id.mobile_number_text_view);
        messageEditText = findViewById(R.id.message_edit_text);
        sendSmsButton = findViewById(R.id.send_sms_button);

        // Get the mobile number from the intent
        String mobileNumber = getIntent().getStringExtra("MOBILE_NUMBER");

        // Display the mobile number
        if (mobileNumber != null) {
            mobileNumberTextView.setText(mobileNumber);
        }

        // Set OnClickListener for the send SMS button
        sendSmsButton.setOnClickListener(v -> sendSMS(mobileNumber));
    }

    // SMS Logic
    private void sendSMS(String mobileNumber) {
        if (mobileNumber == null || mobileNumber.isEmpty()) {
            Toast.makeText(this, "Please enter a valid mobile number.", Toast.LENGTH_SHORT).show();
            return;
        }

        String message = messageEditText.getText().toString();

        // Check for permission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 1);
        } else {
            // Send SMS
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(mobileNumber, null, message, null, null);
            Toast.makeText(this, "SMS sent successfully!", Toast.LENGTH_SHORT).show();
            messageEditText.setText(""); // Clear the message input
        }
    }

    // Permission check for SMS
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, you can send SMS now
                String mobileNumber = mobileNumberTextView.getText().toString();
                sendSMS(mobileNumber);
            } else {
                Toast.makeText(this, "Permission denied to send SMS", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
