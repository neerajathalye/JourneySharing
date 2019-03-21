package com.group12.journeysharing.model;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by Neeraj Athalye on 14-Mar-19.
 */
public class Journey {

    private long journeyId;
    private LatLng source;
    private LatLng destination[];
    private LatLng startingPoint[];
    private ArrayList<String> passengerIds; //User Id of other passengers



}
