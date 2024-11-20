package com.example.turfly;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;

public class ProfilePictureManager {
    private static final String PREFS_NAME = "ProfilePrefs";
    private static final String KEY_PROFILE_PICTURE_URI = "profile_picture_uri";
    private SharedPreferences sharedPreferences;

    public ProfilePictureManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    // Save the profile picture URI
    public void saveProfilePictureUri(Uri uri) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_PROFILE_PICTURE_URI, uri.toString());
        editor.apply();
    }

    // Load the profile picture URI
    public Uri loadProfilePictureUri() {
        String uriString = sharedPreferences.getString(KEY_PROFILE_PICTURE_URI, null);
        return uriString != null ? Uri.parse(uriString) : null;
    }
}
