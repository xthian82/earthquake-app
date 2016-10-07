package com.example.android.quakereport;

/**
 * Created by Casa on 03/10/2016.
 */
public class Earthquake {
    private double magnitude;
    private String place;
    private long datetime;
    private String url;

    public Earthquake( ) { }

    public Earthquake(double magnitude, String place, long datetime) {
        this.magnitude = magnitude;
        this.place = place;
        this.datetime = datetime;
    }

    public double getMagnitude() {
        return magnitude;
    }

    public void setMagnitude(double magnitude) {
        this.magnitude = magnitude;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public long getTimeInMilliseconds() {
        return datetime;
    }

    public void setTimeInMilliseconds(long datetime) {
        this.datetime = datetime;
    }

    public String getUrl() { return url; }

    public void setUrl(String url) { this.url = url; }
}
