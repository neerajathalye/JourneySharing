package com.group12.journeysharing.fragment;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.model.Place;
import com.group12.journeysharing.R;
import com.group12.journeysharing.Util;

/**
 * A simple {@link Fragment} subclass.
 */
public class JourneyFragment extends Fragment {

    MapView mMapView;
    private GoogleMap googleMap;
    private LocationManager locationManager;
    private static final long MIN_TIME = 400;
    private static final float MIN_DISTANCE = 1000;

    LatLng currentLocation;

    String routesAPIKey = "AIzaSyDqRlGeXvPrmrf9oDhfWX8jD5xGWRxPt9s";


    public JourneyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_journey, container, false);

        mMapView = view.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }


        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

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
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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

}
