// BookingRepository.java
package com.example.turfly;

import android.content.Context;
import android.content.SharedPreferences;

public class BookingRepository {

    private static final String PREF_NAME = "BookingPrefs";
    private static final String BOOKING_KEY_PREFIX = "BOOKING_";
    private SharedPreferences sharedPreferences;

    public BookingRepository(Context context) {
        this.sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    // Check if a time slot is available for the given date with overlapping checks
    public boolean isTimeSlotAvailable(String date, int startHour, int endHour) {
        for (int hour = 0; hour < 24; hour++) {
            // Construct the key for each hour on the specified date
            String bookingKey = generateKey(date, hour, hour + 1);

            // If any overlapping hour is already booked, return false
            if (sharedPreferences.contains(bookingKey) && isOverlap(hour, hour + 1, startHour, endHour)) {
                return false;
            }
        }
        return true; // If no overlapping hours are booked, slot is available
    }

    // Book a time slot for the given date
    public void bookTimeSlot(String date, int startHour, int endHour) {
        for (int hour = startHour; hour < endHour; hour++) {
            // Create a key for each hour in the time range
            String bookingKey = generateKey(date, hour, hour + 1);
            sharedPreferences.edit().putBoolean(bookingKey, true).apply();
        }
    }

    // Check if two time ranges overlap
    private boolean isOverlap(int existingStart, int existingEnd, int newStart, int newEnd) {
        return (newStart < existingEnd && newEnd > existingStart); // Condition for overlap
    }

    // Generate a unique key for each hour of a booking by combining date and hour
    private String generateKey(String date, int startHour, int endHour) {
        return BOOKING_KEY_PREFIX + date + "_" + startHour + "-" + endHour;
    }
}
