package com.group12.journeysharing.fragment;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.group12.journeysharing.DirectionsJSONParser;
import com.group12.journeysharing.R;
import com.group12.journeysharing.Util;
import com.group12.journeysharing.activity.BookJourneyActivity;
import com.group12.journeysharing.activity.HomeActivity;
import com.group12.journeysharing.model.Journey;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class JourneyFragment extends Fragment {

    MapView mMapView;
    private GoogleMap googleMap;
    Button cancelJourneyButton, startJourneyButton;

    LatLng source;
    LatLng destination;

    List<String> modesOfTransport;

    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;

    Journey journey;

    String routesAPIKey = "AIzaSyDqRlGeXvPrmrf9oDhfWX8jD5xGWRxPt9s";


    public JourneyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_journey, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("");


        mMapView = view.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        cancelJourneyButton = view.findViewById(R.id.cancelJourneyButton);
        startJourneyButton = view.findViewById(R.id.startJourneyButton);

        journey = new Journey();

        databaseReference.child("journey").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.hasChildren()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Journey j = snapshot.getValue(Journey.class);
                        if(j.getStatus().equals("active") && (j.getUserId().equals(firebaseAuth.getCurrentUser().getUid()) || j.getPassengerIds().contains(firebaseAuth.getCurrentUser().getUid())))
                        {

                            journey = j;
                            if(journey.getUserId().equals(firebaseAuth.getCurrentUser().getUid()))
                                cancelJourneyButton.setVisibility(View.GONE);

                            mMapView.onResume(); // needed to get the map to display immediately

                            try {
                                MapsInitializer.initialize(getActivity().getApplicationContext());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            modesOfTransport = new ArrayList<>();


                            mMapView.getMapAsync(new OnMapReadyCallback() {
                                @Override
                                public void onMapReady(final GoogleMap mMap) {
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

                                        source = new LatLng(journey.getSource().getLatitude(), journey.getSource().getLongitude());
                                        destination = new LatLng(journey.getDestination().getLatitude(), journey.getDestination().getLongitude());
                                        modesOfTransport = journey.getPreference().getModesOfTransport();

                                        googleMap.addMarker(new MarkerOptions().position(destination));

                                        // Getting URL to the Google Directions API
                                        String url = getDirectionsUrl(source, destination);

                                        DownloadTask downloadTask = new DownloadTask();

                                        // Start downloading json data from Google Directions API
                                        downloadTask.execute(url);


                                    }
                                }
                            });
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        cancelJourneyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.child("user").child(firebaseAuth.getCurrentUser().getUid()).child("active").setValue(false);

                ArrayList<String> passengerIds = journey.getPassengerIds();
                passengerIds.remove(firebaseAuth.getCurrentUser().getUid());
                journey.setPassengerIds(passengerIds);
                databaseReference.child("journey").child(journey.getJourneyId()).setValue(journey);

                Intent intent = new Intent(getContext(), HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        return view;
    }

    private class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {

            String data = "";

            try {
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();


            parserTask.execute(result);

        }
    }

    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();
//
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = new ArrayList<>();
            PolylineOptions lineOptions = new PolylineOptions();
            MarkerOptions markerOptions = new MarkerOptions();

            for (int i = 0; i < result.size(); i++) {
//                points = new ArrayList();
//                lineOptions = new PolylineOptions();

                List<HashMap<String, String>> path = result.get(i);

                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                lineOptions.addAll(points);
                lineOptions.width(12);
                lineOptions.color(Color.RED);
                lineOptions.geodesic(true);

            }

            // Drawing polyline in the Google Map for the i-th route
            googleMap.addPolyline(lineOptions);

            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            builder.include(source);
            builder.include(destination);
            LatLngBounds bound = builder.build();
            googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bound, 100), 1000, null);
        }
    }

    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";
        String mode = null;

        if(modesOfTransport.contains("Car") || modesOfTransport.contains("Cab")  || modesOfTransport.contains("Motorcycle"))
            mode = "mode=driving";
        else if(modesOfTransport.contains("Walk"))
            mode = "mode=walking";
        else if(modesOfTransport.contains("Cycle"))
            mode = "mode=bicycling";
        else if(modesOfTransport.contains("Public Transport"))
            mode = "mode=transit";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + mode + "&key=" + routesAPIKey;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
        Log.d("JSON REQUEST:: ", url);

        return url;
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.connect();

            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
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
