package com.example.turfly;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class TurfDetailActivity extends AppCompatActivity {

    private TextView turfNameTextView, turfDescriptionTextView, turfDescription;
    private ImageView turfImageView;
    private Button booknow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_turf_detail);

        // Initialize views
        turfNameTextView = findViewById(R.id.turf_name_detail);
        turfDescriptionTextView = findViewById(R.id.turf_description_detail);
        turfDescription = findViewById(R.id.description);
        turfImageView = findViewById(R.id.turf_image_detail);
        booknow = findViewById(R.id.book);

        // Get the turf ID from the intent
        int turfId = getIntent().getIntExtra("TURF_ID", -1);

        // Display the turf details based on the ID
        displayTurfDetails(turfId);

        // Set the OnClickListener for the booknow button
        booknow.setOnClickListener(v -> {
            String mobileNumber = getMobileNumber(turfId);
            if (mobileNumber != null) {
                Intent intent = new Intent(TurfDetailActivity.this, SmsActivity.class);
                intent.putExtra("MOBILE_NUMBER", mobileNumber);
                startActivity(intent);
            }
        });
    }

    private void displayTurfDetails(int turfId) {
        // Display the turf details based on the ID
        if (turfId != -1) {
            switch (turfId) {
                case 1:
                    turfNameTextView.setText("The Foothill Arena");
                    turfDescriptionTextView.setText("MIT, Kothrud");
                    turfDescription.setText("Tucked behind MIT Kothrud, The Foothill Arena boasts a lush synthetic turf that sets the stage for exhilarating sports activities. This expansive venue is designed for football and cricket enthusiasts, offering a professional-grade surface that enhances performance. The arena’s vibrant community spirit is amplified by seating areas for spectators, making it a hub for local tournaments and friendly matches. With modern facilities, including clean restrooms and snack options, The Foothill Arena is perfect for athletes and families looking to enjoy an active day outdoors.\n\n");
                    turfImageView.setImageResource(R.drawable.foothill); // Replace with the actual image resource
                    break;
                case 2:
                    turfNameTextView.setText("Hindu Gymkhana");
                    turfDescriptionTextView.setText("Vanaz, Kothrud");
                    turfDescription.setText("At the heart of Vanaz Kothrud, the Hindu Gymkhana turf is a cornerstone for sporting events in the area. This well-maintained synthetic field caters to a variety of sports, promoting an active lifestyle within the community. The venue features professional goalposts and ample seating for spectators, creating an inviting atmosphere for both players and fans. With facilities that include restrooms and refreshment stalls, Hindu Gymkhana ensures a convenient and enjoyable experience for all. Whether for competitive matches or casual games, this turf is a local favorite.\n\n");
                    turfImageView.setImageResource(R.drawable.hindugymkhana);
                    break;
                case 3:
                    turfNameTextView.setText("Nutmeg Arena");
                    turfDescriptionTextView.setText("Pimple Saudagar");
                    turfDescription.setText("Nutmeg Arena, located in Pimple Saudagar, is a vibrant turf facility that caters to athletes of all skill levels. Known for its exceptional synthetic grass, this venue offers a safe and durable surface for football, cricket, and more. The arena’s well-kept conditions promote an engaging atmosphere for both practice and competitive play. Spectators can enjoy the action from comfortable seating while enjoying amenities like restrooms and snack options. Nutmeg Arena is the perfect spot for sports enthusiasts looking to hone their skills or participate in friendly competitions.\n\n");
                    turfImageView.setImageResource(R.drawable.nutmeg);
                    break;
                case 4:
                    turfNameTextView.setText("Corner Kick");
                    turfDescriptionTextView.setText("Ghorpadi, Hadapsar");
                    turfDescription.setText("Positioned in Ghorpadi, Hadapsar, Corner Kick is a standout turf facility designed for sports lovers. This modern venue features high-quality synthetic turf, ensuring a reliable surface for football and other team sports. The arena is meticulously maintained, providing excellent playing conditions for both serious competitors and casual players. With spacious seating for fans and convenient amenities, including restrooms and food stands, Corner Kick fosters a welcoming environment. Whether organizing a tournament or enjoying a weekend match with friends, this turf is a prime destination for local sports.\n\n");
                    turfImageView.setImageResource(R.drawable.cornerkick);
                    break;
                case 5:
                    turfNameTextView.setText("Dei Gratia Sports Club");
                    turfDescriptionTextView.setText("Clover Park, Viman Nagar");
                    turfDescription.setText("Dei Gratia Sports Club, nestled in Clover Park, Viman Nagar, is a premier sporting venue that emphasizes quality and community. The synthetic turf offers an exceptional playing surface suitable for football, cricket, and various other sports. Known for its vibrant atmosphere, the club provides ample seating for spectators, creating a lively backdrop for events and games. With modern conveniences such as restrooms and refreshment options, Dei Gratia ensures a comfortable experience for athletes and fans alike. It’s the go-to location for anyone looking to engage in sports and foster camaraderie in the community.\n\n");
                    turfImageView.setImageResource(R.drawable.dei);
                    break;
                default:
                    turfNameTextView.setText("Unknown Turf");
                    turfDescriptionTextView.setText("No details available.");
                    turfImageView.setImageResource(R.drawable.baseline_person_24); // Default image
                    break;
            }
        }
    }

    private String getMobileNumber(int turfId) {
        switch (turfId) {
            case 1: return "1234567890"; // Mobile number for The Foothill Arena
            case 2: return "0987654321"; // Mobile number for Hindu Gymkhana
            case 3: return "1234598705"; // Mobile number for Nutmeg Arena
            case 4: return "4522054830"; // Mobile number for Corner Kick
            case 5: return "0541054082"; // Mobile number for Dei Gratia Sports Club
            default: return null; // No valid turf ID
        }
    }
}
