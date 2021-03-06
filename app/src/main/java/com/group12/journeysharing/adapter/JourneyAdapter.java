package com.group12.journeysharing.adapter;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.group12.journeysharing.viewholder.JourneyViewHolder;
import com.group12.journeysharing.R;
import com.group12.journeysharing.activity.ConfirmJourneyActivity;
import com.group12.journeysharing.model.Journey;
import com.group12.journeysharing.model.LatLng;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Neeraj Athalye on 21-Mar-19.
 */
public class JourneyAdapter extends RecyclerView.Adapter<JourneyViewHolder> {

    ArrayList<Journey> journeys;
    Context context;

    public JourneyAdapter(ArrayList<Journey> journeys, Context context) {
        this.journeys = journeys;
        this.context = context;
    }

    @NonNull
    @Override
    public JourneyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.journey_row, viewGroup, false);

        return new JourneyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JourneyViewHolder holder, final int i) {

        holder.fromTextView.setText(getAddress(journeys.get(i).getSource()));
        holder.toTextView.setText(getAddress(journeys.get(i).getDestination()));

        long startTime = journeys.get(i).getPreference().getStartTime();

        String startTimeString = new SimpleDateFormat("EEE, d MMM HH:mm a").format(new Date(startTime));
        holder.timeTextView.setText(startTimeString);

        holder.journeyContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String json = new Gson().toJson(journeys.get(i));
                Intent intent = new Intent(context, ConfirmJourneyActivity.class);
                intent.putExtra("json", json);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return journeys.size();
    }

    public String getAddress(LatLng latLng) {

        StringBuilder add = new StringBuilder();
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
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
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return add.toString();
    }
}
