package com.group12.journeysharing.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Neeraj Athalye on 14-Mar-19.
 */
public class Preference implements Parcelable {

    private String preferredGender; // male, female, other, all
    private long startTime;
    private int maxPassengers;
    private List<String> modesOfTransport;
    private int distanceToStartingPoint; //in metres

    public Preference() {
    }

    protected Preference(Parcel in) {
        preferredGender = in.readString();
        startTime = in.readLong();
        maxPassengers = in.readInt();
        modesOfTransport = in.createStringArrayList();
        distanceToStartingPoint = in.readInt();
    }

    public static final Creator<Preference> CREATOR = new Creator<Preference>() {
        @Override
        public Preference createFromParcel(Parcel in) {
            return new Preference(in);
        }

        @Override
        public Preference[] newArray(int size) {
            return new Preference[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(preferredGender);
        dest.writeLong(startTime);
        dest.writeInt(maxPassengers);
        dest.writeList(modesOfTransport);
        dest.writeInt(distanceToStartingPoint);
    }
}
