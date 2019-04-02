package com.group12.journeysharing.activity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.group12.journeysharing.R;
import com.group12.journeysharing.model.Journey;
import com.group12.journeysharing.model.LatLng;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ConfirmJourneyActivity extends AppCompatActivity implements View.OnClickListener {

    Journey journey;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    TextView sourceTextView, destinationTextView, dateAndTimeTextView, modesOfTransportTextView, maxPassengersTextView, genderTextView, nameTextView;
    Button cancelButton, joinButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_journey);

        databaseReference = FirebaseDatabase.getInstance().getReference("");
        firebaseAuth = FirebaseAuth.getInstance();
        String json = getIntent().getStringExtra("json");
        journey = new Gson().fromJson(json, Journey.class);

        sourceTextView = findViewById(R.id.sourceTextView);
        destinationTextView = findViewById(R.id.destinationTextView);
        dateAndTimeTextView = findViewById(R.id.dateAndTimeTextView);
        modesOfTransportTextView = findViewById(R.id.modesOfTransportTextView);
        maxPassengersTextView = findViewById(R.id.maxPassengersTextView);
        genderTextView = findViewById(R.id.genderTextView);
        nameTextView = findViewById(R.id.serviceNameTextView);
        cancelButton = findViewById(R.id.cancelButton);
        joinButton = findViewById(R.id.joinButton);

        cancelButton.setOnClickListener(this);
        joinButton.setOnClickListener(this);

        sourceTextView.setText(getAddress(journey.getSource()));
        destinationTextView.setText(getAddress(journey.getDestination()));
        SimpleDateFormat outputDateFormat = new SimpleDateFormat("EEE, d MMM HH:mm a");
        dateAndTimeTextView.setText(outputDateFormat.format(journey.getPreference().getStartTime()));
        modesOfTransportTextView.setText(StringUtils.join(journey.getPreference().getModesOfTransport(), ", "));
        maxPassengersTextView.setText(String.valueOf(journey.getPreference().getMaxPassengers()));
        genderTextView.setText(journey.getPreference().getPreferredGender());

        databaseReference.child("user").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren())
                {
                    if(journey.getUserId().equals(ds.child("userId").getValue()))
                        nameTextView.setText(String.valueOf(ds.child("fullName").getValue()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public String getAddress(LatLng latLng) {

        StringBuilder add = new StringBuilder();
        Geocoder geocoder = new Geocoder(ConfirmJourneyActivity.this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latLng.getLatitude(), latLng.getLongitude(), 1);
            Address obj = addresses.get(0);

            if(obj.getSubThoroughfare() != null)
                add.append(obj.getSubThoroughfare() + ", ");
            if(obj.getThoroughfare() != null )
                add.append(obj.getThoroughfare());
            else if(obj.getSubThoroughfare() == null && obj.getThoroughfare() == null)
            {
                if(obj.getLocality() != null)
                    add.append(obj.getLocality());
                else if(obj.getSubAdminArea() != null)
                    add.append(obj.getSubAdminArea());
                else if(obj.getAdminArea() != null)
                    add.append(obj.getAdminArea());
                else if(obj.getCountryName() != null)
                    add.append(obj.getCountryName());
            }


            Log.v("IGA", "Address " + add);
            // Toast.makeText(this, "Address=>" + add,
            // Toast.LENGTH_SHORT).show();

            // TennisAppActivity.showDialog(add);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(ConfirmJourneyActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return add.toString();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == cancelButton.getId())
        {
            super.onBackPressed();
        }
        else if(v.getId() == joinButton.getId())
        {
            //add the person to the journey
            databaseReference.child("user").child(firebaseAuth.getCurrentUser().getUid()).child("active").setValue(true);
            ArrayList<String> passengerIds = journey.getPassengerIds();
            if(passengerIds == null)
                passengerIds = new ArrayList<>();
            passengerIds.add(firebaseAuth.getCurrentUser().getUid());
            journey.setPassengerIds(passengerIds);

            databaseReference.child("journey").child(journey.getJourneyId()).setValue(journey);

            Intent intent = new Intent(ConfirmJourneyActivity.this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }
}
