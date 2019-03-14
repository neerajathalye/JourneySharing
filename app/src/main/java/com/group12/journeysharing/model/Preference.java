package com.group12.journeysharing.model;

/**
 * Created by Neeraj Athalye on 14-Mar-19.
 */
public class Preference {

    private String preferredGender; // male, female, other, all
    private long startTime;
    private int maxPassengers;
    private String modeOfTransport;
    private int distanceToStartingPoint; //in metres

    public Preference() {
    }

    public String getPreferredGender() {
        return preferredGender;
    }

    public void setPreferredGender(String preferredGender) {
        this.preferredGender = preferredGender;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public int getMaxPassengers() {
        return maxPassengers;
    }

    public void setMaxPassengers(int maxPassengers) {
        this.maxPassengers = maxPassengers;
    }

    public String getModeOfTransport() {
        return modeOfTransport;
    }

    public void setModeOfTransport(String modeOfTransport) {
        this.modeOfTransport = modeOfTransport;
    }

    public int getDistanceToStartingPoint() {
        return distanceToStartingPoint;
    }

    public void setDistanceToStartingPoint(int distanceToStartingPoint) {
        this.distanceToStartingPoint = distanceToStartingPoint;
    }
}
