package com.group12.journeysharing.model;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by Neeraj Athalye on 14-Mar-19.
 */
public class Journey {

    private String userId;
    private String journeyId;
    private LatLng source;
    private LatLng destination;
    private LatLng startingPoint;
    private ArrayList<String> passengerIds; //User Id of other passengers
    private Preference preference;

    public Journey() {
    }

    public String getUser() {
        return userId;
    }

    public void setUser(String userId) {
        this.userId = userId;
    }

    public String getJourneyId() {
        return journeyId;
    }

    public void setJourneyId(String journeyId) {
        this.journeyId = journeyId;
    }

    public LatLng getSource() {
        return source;
    }

    public void setSource(LatLng source) {
        this.source = source;
    }

    public LatLng getDestination() {
        return destination;
    }

    public void setDestination(LatLng destination) {
        this.destination = destination;
    }

    public LatLng getStartingPoint() {
        return startingPoint;
    }

    public void setStartingPoint(LatLng startingPoint) {
        this.startingPoint = startingPoint;
    }

    public ArrayList<String> getPassengerIds() {
        return passengerIds;
    }

    public void setPassengerIds(ArrayList<String> passengerIds) {
        this.passengerIds = passengerIds;
    }

    public Preference getPreference() {
        return preference;
    }

    public void setPreference(Preference preference) {
        this.preference = preference;
    }
}
