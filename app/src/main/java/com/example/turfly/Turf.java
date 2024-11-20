package com.example.turfly;

public class Turf {
    private int id; // Turf ID
    private String name; // Turf name
    private String description; // Turf description
    private int imageResource; // Resource ID for the turf image

    // Constructor
    public Turf(int id, String name, String description, int imageResource) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.imageResource = imageResource;
    }

    // Getter for ID
    public int getId() {
        return id;
    }

    // Getter for name
    public String getName() {
        return name;
    }

    // Getter for description
    public String getDescription() {
        return description;
    }

    // Getter for image resource
    public int getImageResource() {
        return imageResource;
    }
}
