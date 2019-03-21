package com.group12.journeysharing.model;

import android.os.Parcel;
import android.os.Parcelable;


import java.util.ArrayList;

/**
 * Created by Neeraj Athalye on 14-Mar-19.
 */
public class Journey implements Parcelable {

    private String userId;
    private String journeyId;
    private LatLng source;
    private LatLng destination;
    private LatLng startingPoint;
    private ArrayList<String> passengerIds; //User Id of other passengers
    private Preference preference;

    public Journey() {
    }

    protected Journey(Parcel in) {
        userId = in.readString();
        journeyId = in.readString();
        source = in.readParcelable(LatLng.class.getClassLoader());
        destination = in.readParcelable(LatLng.class.getClassLoader());
        startingPoint = in.readParcelable(LatLng.class.getClassLoader());
        passengerIds = in.createStringArrayList();
    }

    public static final Creator<Journey> CREATOR = new Creator<Journey>() {
        @Override
        public Journey createFromParcel(Parcel in) {
            return new Journey(in);
        }

        @Override
        public Journey[] newArray(int size) {
            return new Journey[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userId);
        dest.writeString(journeyId);
        dest.writeParcelable(source, flags);
        dest.writeParcelable(destination, flags);
        dest.writeParcelable(startingPoint, flags);
        dest.writeList(passengerIds);
        dest.writeParcelable(preference, flags);
    }
}
