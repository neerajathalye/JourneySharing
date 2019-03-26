package com.group12.journeysharing.activity;

import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.group12.journeysharing.JourneyAdapter;
import com.group12.journeysharing.R;
import com.group12.journeysharing.model.Journey;

import java.util.ArrayList;

public class BookJourneyActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    Journey journey;
    RecyclerView journeyRecyclerView;
    JourneyAdapter journeyAdapter;
    ArrayList<Journey> journeys;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_journey);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("");

        String json = getIntent().getStringExtra("json");
        Log.d("JSON:::", json);

        Gson gson = new Gson();
        journey = gson.fromJson(json, Journey.class);

        journeyRecyclerView = findViewById(R.id.journeyRecyclerView);

        databaseReference.child("journey").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                journeys = new ArrayList<>();

                if(!dataSnapshot.hasChildren())
                {
                    databaseReference.child("journey").child(journey.getJourneyId()).setValue(journey);
                    databaseReference.child("user").child(firebaseAuth.getCurrentUser().getUid()).child("active").setValue(true);
                }
                else
                {
                    for(DataSnapshot snapshot : dataSnapshot.getChildren())
                    {
                        Journey j = snapshot.getValue(Journey.class);

                        Location s1 = new Location("");
                        s1.setLatitude(journey.getSource().getLatitude());
                        s1.setLongitude(journey.getSource().getLongitude());

                        Location s2 = new Location("");
                        s2.setLatitude(j.getSource().getLatitude());
                        s2.setLongitude(j.getSource().getLongitude());
                        float distanceInMeters = s1.distanceTo(s2);
                        Toast.makeText(BookJourneyActivity.this, "Distance: " + distanceInMeters, Toast.LENGTH_SHORT).show();
                        if(distanceInMeters <= journey.getPreference().getDistanceToStartingPoint() && distanceInMeters <= j.getPreference().getDistanceToStartingPoint() && j.getStatus().equals("active"))
                            //Join existing journey
                            journeys.add(j);
                        else
                        {
                            //create new journey
                            databaseReference.child("journey").child(journey.getJourneyId()).setValue(journey);
                            databaseReference.child("user").child(firebaseAuth.getCurrentUser().getUid()).child("active").setValue(true);
                        }
                    }
                }


                journeyAdapter = new JourneyAdapter(journeys, BookJourneyActivity.this);
                journeyRecyclerView.setAdapter(journeyAdapter);
                journeyRecyclerView.setLayoutManager(new LinearLayoutManager(BookJourneyActivity.this));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }
}
