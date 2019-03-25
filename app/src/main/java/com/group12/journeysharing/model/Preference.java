package com.group12.journeysharing.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Neeraj Athalye on 14-Mar-19.
 */
public class Preference {

    private String preferredGender; // male, female, other, all
    private long startTime;
    private int maxPassengers;
    private List<String> modesOfTransport;
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

    public List<String> getModesOfTransport() {
        return modesOfTransport;
    }

    public void setModesOfTransport(List<String> modesOfTransport) {
        this.modesOfTransport = modesOfTransport;
    }

    public int getDistanceToStartingPoint() {
        return distanceToStartingPoint;
    }

    public void setDistanceToStartingPoint(int distanceToStartingPoint) {
        this.distanceToStartingPoint = distanceToStartingPoint;
    }

}
