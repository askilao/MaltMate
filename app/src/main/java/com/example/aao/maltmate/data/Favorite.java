package com.example.aao.maltmate.data;

/**
 * Created by aao on 03/12/17.
 */

public class Favorite {
    private String id;
    private float rating;
    private String location;
    private String notes;

    public Favorite(String id, float rating, String location, String notes) {
        this.id = id;
        this.rating = rating;
        this.location = location;
        this.notes = notes;
    }


    public String getName() {
        return id;
    }

    public void setName(String id) {
        this.id = id;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
