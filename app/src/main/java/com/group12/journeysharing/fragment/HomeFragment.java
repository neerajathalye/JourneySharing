package com.group12.journeysharing.fragment;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.group12.journeysharing.R;
import com.group12.journeysharing.Util;
import com.group12.journeysharing.activity.JourneyDetailsActivity;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements View.OnClickListener, OnMapReadyCallback{

    MapView mMapView;
    private GoogleMap googleMap;
    private LocationManager locationManager;
    private static final long MIN_TIME = 400;
    private static final float MIN_DISTANCE = 1000;

    Place destination;
    Button bookJourneyButton;
    LatLng currentLocation;
    String country;
    CardView cardView;
    AutocompleteSupportFragment autocompleteFragment;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        mMapView = view.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        bookJourneyButton = view.findViewById(R.id.bookJourneyButton);
        bookJourneyButton.setOnClickListener(this);

        cardView = view.findViewById(R.id.cardView);


        String apiKey = "AIzaSyBe5jCnOW1PHXRSmkwZ1b2iqnBk1U6zif4";
        Places.initialize(getContext(), apiKey);

//         Initialize the AutocompleteSupportFragment.
        autocompleteFragment = (AutocompleteSupportFragment) getChildFragmentManager().findFragmentById(R.id.autoCompleteFragment);
        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS));
//        autocompleteFragment.getView().setVisibility(View.INVISIBLE);
        cardView.setVisibility(View.INVISIBLE);

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }


        mMapView.getMapAsync(this);

        return view;
    }

//    public class MapAsyncTask extends AsyncTask<>

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.bookJourneyButton)
        {

            if(currentLocation == null)
            {
                Toast.makeText(getContext(), "Current location cannot be null", Toast.LENGTH_SHORT).show();
            }
            else if(destination == null)
            {
                Toast.makeText(getContext(), "Please select a destination", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Intent intent = new Intent(getContext(), JourneyDetailsActivity.class);

                Bundle bundle = new Bundle();
                bundle.putParcelable("destination", destination);
                bundle.putParcelable("source", currentLocation);
                intent.putExtra("bundle", bundle);
//                intent.putExtra("destination", destination); //place object
//                intent.putExtra("source", currentLocation); //latlng object
                startActivity(intent);
            }

        }
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
//            googleMap = mMap;

        // For showing a move to my location button
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // No explanation needed; request the permission
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    Util.MY_PERMISSIONS_REQUEST_LOCATION);

        } else {
            // Permission has already been granted
            googleMap.setMyLocationEnabled(true);

            // Get the button view
            @SuppressLint("ResourceType") View locationButton = ((View) mMapView.findViewById(1).getParent()).findViewById(2);

            RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
            // position on right bottom
            rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
            rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            rlp.setMargins(0, 0, 30, 30);



            locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_DISTANCE, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);
                    googleMap.animateCamera(cameraUpdate);
                    locationManager.removeUpdates(this);
                    currentLocation = latLng;

                    Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
                    try {
                        List<Address> addresses = geocoder.getFromLocation(currentLocation.latitude, currentLocation.longitude, 1);
                        Address obj = addresses.get(0);
                        country = obj.getCountryCode();
                    }
                    catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    cardView.setVisibility(View.VISIBLE);
                    autocompleteFragment.setCountry(country);

                    // Set up a PlaceSelectionListener to handle the response.
                    autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                        @Override
                        public void onPlaceSelected(Place place) {
                            // TODO: Get info about the selected place.
                            Log.d("============", "Place ID: " + place.getId());
                            Log.d("============", "Place Name: " + place.getName());
                            Log.d("============", "Place Lat_lng: " + place.getLatLng());
                            Log.d("============", "Place Address: " + place.getAddress());

                            destination = place;

                            googleMap.clear();
                            googleMap.addMarker(new MarkerOptions().position(place.getLatLng()).title(place.getName()));
                            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 15);
                            googleMap.animateCamera(cameraUpdate);

                        }
                        @Override
                        public void onError(Status status) {
                            // TODO: Handle the error.
                            Toast.makeText(getContext(), "An error occurred:" + status, Toast.LENGTH_SHORT).show();
//                Log.d(TAG, "An error occurred: " + status);
                        }
                    });
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {
//
                }
            }); //You can also use LocationManager.GPS_PROVIDER and LocationManager.PASSIVE_PROVIDER

        }
    }
}
