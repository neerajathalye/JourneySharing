package com.group12.journeysharing.activity;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
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
        databaseReference = FirebaseDatabase.getInstance().getReference("journey");
        journey = getIntent().getParcelableExtra("journey");

        journeyRecyclerView = findViewById(R.id.journeyRecyclerView);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

//                GenericTypeIndicator<ArrayList<Journey>> typeIndicator = new GenericTypeIndicator<ArrayList<Journey>>() {};

                journeys = new ArrayList<>();

                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    Journey journey = snapshot.getValue(Journey.class);
                    journeys.add(journey);
                }


//                journeys = dataSnapshot.getValue(typeIndicator);

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
