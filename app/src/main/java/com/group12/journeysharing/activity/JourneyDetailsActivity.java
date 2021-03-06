package com.group12.journeysharing.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.model.Place;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.group12.journeysharing.AlertDialogCallback;
import com.group12.journeysharing.R;
import com.group12.journeysharing.model.Journey;
import com.group12.journeysharing.model.Preference;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class JourneyDetailsActivity extends AppCompatActivity implements View.OnClickListener, AlertDialogCallback<List<String>>, RadioGroup.OnCheckedChangeListener, SeekBar.OnSeekBarChangeListener {

    Place destination;
    LatLng source;
    TextInputLayout fromTIL, toTIL, startDateTimeTIL;
    Calendar calendar;
    boolean isDateTimeSet;
    Button modesOfTransportButton, bookButton;
    RadioGroup radioGroup;
    Journey journey;
    Preference preference;
    SeekBar passengerSeekBar, distanceToStartingPointSeekBar;
    TextView numberOfPassengersTextView, distanceToStartingPointTextView, selectedModesOfTransportTextView;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journey_details);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("journey");

        Bundle bundle = getIntent().getParcelableExtra("bundle");
        destination = bundle.getParcelable("destination");
        source = bundle.getParcelable("source");

        fromTIL = findViewById(R.id.fromTIL);
        toTIL = findViewById(R.id.toTIL);
        startDateTimeTIL = findViewById(R.id.startDateTimeTIL);
        modesOfTransportButton = findViewById(R.id.modesOfTransportButton);
        radioGroup = findViewById(R.id.radioGroup);
        passengerSeekBar = findViewById(R.id.passengerSeekBar);
        numberOfPassengersTextView = findViewById(R.id.numberOfPassengersTextView);
        distanceToStartingPointSeekBar = findViewById(R.id.distanceToStartingPointSeekBar);
        distanceToStartingPointTextView = findViewById(R.id.distanceToStartingPointTextView);
        selectedModesOfTransportTextView = findViewById(R.id.selectedModesOfTransportTextView);
        bookButton = findViewById(R.id.bookButton);

        fromTIL.getEditText().setText(getAddress(source));
        toTIL.getEditText().setText(destination.getName());

        fromTIL.getEditText().setFocusable(false);
        toTIL.getEditText().setFocusable(false);
        startDateTimeTIL.getEditText().setFocusable(false);

        modesOfTransportButton.setOnClickListener(this);
        startDateTimeTIL.getEditText().setOnClickListener(this);
        radioGroup.setOnCheckedChangeListener(this);
        passengerSeekBar.setOnSeekBarChangeListener(this);
        distanceToStartingPointSeekBar.setOnSeekBarChangeListener(this);
        bookButton.setOnClickListener(this);

        preference = new Preference();
        journey = new Journey();

        preference.setPreferredGender("All");
        String numberOfPassengers = getString(R.string.maximum_number_of_passengers) + ": " + 5;
        numberOfPassengersTextView.setText(numberOfPassengers);
        preference.setMaxPassengers(5);

        String distanceToStartingPoint = getString(R.string.distance_to_starting_point) + ": " + (1000) +"m";
        distanceToStartingPointTextView.setText(distanceToStartingPoint);
        preference.setDistanceToStartingPoint(1000);

    }
    public String getAddress(LatLng latLng) {

        StringBuilder add = new StringBuilder();
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
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
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return add.toString();
    }


    public void showDateTimePicker() {
        final Calendar currentDate = Calendar.getInstance();
        calendar = Calendar.getInstance();

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(year, month, dayOfMonth);

                new TimePickerDialog(JourneyDetailsActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);
                        isDateTimeSet = true;
                        Log.d("========", "The chosen one " + calendar.getTimeInMillis());

                        SimpleDateFormat outputDateFormat = new SimpleDateFormat("EEE, d MMM HH:mm a");

                        startDateTimeTIL.getEditText().setText(outputDateFormat.format(calendar.getTimeInMillis()));
                        preference.setStartTime(calendar.getTimeInMillis());
                    }
                }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), false).show();
            }
        }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE));

        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() + 864000000);
        datePickerDialog.show();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.modesOfTransportButton)
        {
            showModesOfTransportDialog(this);
        }
        else if(v.getId() == startDateTimeTIL.getEditText().getId())
        {
            showDateTimePicker();

        }
        else if(v.getId() == R.id.bookButton)
        {
            String journeyId = firebaseAuth.getCurrentUser().getUid() + System.currentTimeMillis();


            if(preference.getStartTime() == 0)
                Toast.makeText(this, "Choose Start time", Toast.LENGTH_SHORT).show();
            else if(preference.getMaxPassengers() == 0)
                Toast.makeText(this, "Choose number of passengers", Toast.LENGTH_SHORT).show();
            else if(preference.getDistanceToStartingPoint() == 0)
                Toast.makeText(this, "Choose distance to starting point", Toast.LENGTH_SHORT).show();
            else if(preference.getModesOfTransport() == null || preference.getModesOfTransport().size() == 0)
                Toast.makeText(this, "Choose modes of transport", Toast.LENGTH_SHORT).show();
            else
            {
                journey.setJourneyId(journeyId);
                journey.setUserId(firebaseAuth.getCurrentUser().getUid());
                journey.setSource(new com.group12.journeysharing.model.LatLng(source));
                journey.setDestination(new com.group12.journeysharing.model.LatLng(destination.getLatLng()));
                journey.setStartingPoint(null);
                journey.setPassengerIds(null);
                journey.setPreference(preference);
                journey.setStatus("active");
                journey.setCreatedDate(System.currentTimeMillis());

                Gson gson = new Gson();
                String json = gson.toJson(journey);
                Log.d("JSON", json);

                //Search for journey. if not found, then create
                Intent intent = new Intent(JourneyDetailsActivity.this, BookJourneyActivity.class);
                intent.putExtra("json", json);
                startActivity(intent);

//                Toast.makeText(this, "Data successfully entered", Toast.LENGTH_SHORT).show();
//                databaseReference.child(journeyId).setValue(journey);
            }

        }
    }

    private void showModesOfTransportDialog(final AlertDialogCallback<List<String>> callback) {

        final ArrayList<Integer> selectedItemsIndex = new ArrayList<>();
        final List<String>[] selectedItems = new List[]{new ArrayList<>()};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Select Modes of Transport")
           .setMultiChoiceItems(R.array.modesOfTransport, null,
                new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        if (isChecked) {
                            // If the user checked the item, add it to the selected items
                            selectedItemsIndex.add(which);
                        } else if (selectedItemsIndex.contains(which)) {
                            // Else, if the item is already in the array, remove it
                            selectedItemsIndex.remove(Integer.valueOf(which));
                        }
                    }
                })
                // Set the action buttons
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK, so save the selectedItems results somewhere
                        // or return them to the component that opened the dialog
                        List<String> tempList = Arrays.asList(getResources().getStringArray(R.array.modesOfTransport));

                        for(int i  = 0; i < tempList.size(); i++)
                        {
                            if(selectedItemsIndex.contains(i))
                                selectedItems[0].add(tempList.get(i));
                        }
                        callback.alertDialogCallback(selectedItems[0]);

                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

        builder.create().show();
    }

    @Override
    public void alertDialogCallback(List<String> list) {
            Toast.makeText(JourneyDetailsActivity.this, String.valueOf(list.size()), Toast.LENGTH_SHORT).show();
            StringBuilder selected = new StringBuilder();

            if(list.size() != 0)
            {
                for(String s : list)
                    selected.append(s).append(", ");
                selectedModesOfTransportTextView.setText("Selected: " + selected.toString().substring(0, selected.length() - 2));
                preference.setModesOfTransport(list);
            }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        if (checkedId == R.id.allRadioButton) {
            Toast.makeText(this, "All", Toast.LENGTH_SHORT).show();
            preference.setPreferredGender("All");
        }
        else if (checkedId == R.id.mRadioButton) {
            Toast.makeText(this, "Male", Toast.LENGTH_SHORT).show();
            preference.setPreferredGender("Male");
        }
        else if (checkedId == R.id.fRadioButton) {
            Toast.makeText(this, "Female", Toast.LENGTH_SHORT).show();
            preference.setPreferredGender("Female");
        }
        else if (checkedId == R.id.otherRadioButton) {
            Toast.makeText(this, "Other", Toast.LENGTH_SHORT).show();
            preference.setPreferredGender("Other");
        }


    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        if(seekBar.getId() == R.id.passengerSeekBar) {
            String numberOfPassengers = getString(R.string.maximum_number_of_passengers) + ": " + seekBar.getProgress();
            numberOfPassengersTextView.setText(numberOfPassengers);
            preference.setMaxPassengers(seekBar.getProgress());
        }
        else if(seekBar.getId() == R.id.distanceToStartingPointSeekBar)
        {
            String distanceToStartingPoint = getString(R.string.distance_to_starting_point) + ": " + (seekBar.getProgress()*100) +"m";
            distanceToStartingPointTextView.setText(distanceToStartingPoint);
            preference.setDistanceToStartingPoint(seekBar.getProgress()*100);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {


    }
}
