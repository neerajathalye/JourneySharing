package com.group12.journeysharing.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.model.Place;
import com.group12.journeysharing.R;

public class BookJourneyActivity extends AppCompatActivity {

    Place destination;
    LatLng source;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_journey);

        Bundle bundle = getIntent().getParcelableExtra("bundle");
        destination = bundle.getParcelable("destination");
        source = bundle.getParcelable("source");

        textView = findViewById(R.id.textView);

        textView.setText("Source: (" + source.latitude + ", " + source.longitude + ")\nDestination: \n\tName: " + destination.getName() + "\n\tLatLng: (" + destination.getLatLng().latitude + ", " + destination.getLatLng().longitude );
    }
}
