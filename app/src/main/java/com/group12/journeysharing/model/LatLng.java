package com.group12.journeysharing.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Neeraj Athalye on 21-Mar-19.
 */
public class LatLng implements Parcelable {

    private double latitude;
    private double longitude;

    public LatLng() {
    }

    public LatLng(com.google.android.gms.maps.model.LatLng latLng)
    {
        this.latitude = latLng.latitude;
        this.longitude = latLng.longitude;
    }

    public LatLng(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    protected LatLng(Parcel in) {
        latitude = in.readDouble();
        longitude = in.readDouble();
    }

    public static final Creator<LatLng> CREATOR = new Creator<LatLng>() {
        @Override
        public LatLng createFromParcel(Parcel in) {
            return new LatLng(in);
        }

        @Override
        public LatLng[] newArray(int size) {
            return new LatLng[size];
        }
    };

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
    }
}
