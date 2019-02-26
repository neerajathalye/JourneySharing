package com.group12.journeysharing.activity;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import com.group12.journeysharing.R;
import com.vikktorn.picker.City;
import com.vikktorn.picker.CityPicker;
import com.vikktorn.picker.Country;
import com.vikktorn.picker.CountryPicker;
import com.vikktorn.picker.OnCityPickerListener;
import com.vikktorn.picker.OnCountryPickerListener;
import com.vikktorn.picker.OnStatePickerListener;
import com.vikktorn.picker.State;
import com.vikktorn.picker.StatePicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;


public class SignUpActivity extends AppCompatActivity implements OnStatePickerListener, OnCountryPickerListener, OnCityPickerListener {

    EditText countryEditText, stateEditText, cityEditText;

    private CountryPicker countryPicker;
    private StatePicker statePicker;
    private CityPicker cityPicker;

    public static int countryID, stateID;

    // arrays of state object
    public static List<State> stateObject;
    // arrays of city object
    public static List<City> cityObject;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        countryEditText = findViewById(R.id.countryEditText);
        stateEditText = findViewById(R.id.stateEditText);
        cityEditText = findViewById(R.id.cityEditText);

        stateEditText.setVisibility(View.GONE);
        cityEditText.setVisibility(View.GONE);

        // initiate state object, parser, and arrays
        stateObject = new ArrayList<>();
        cityObject = new ArrayList<>();

        // get state from assets JSON
        try
        {
            getStateJson();
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        // get City from assets JSON
        try
        {
            getCityJson();
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        // initialize country picker
        countryPicker = new CountryPicker.Builder().with(this).listener(this).build();

        // initialize listeners
        countryEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countryPicker.showDialog(getSupportFragmentManager());
            }
        });

        stateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statePicker.showDialog(getSupportFragmentManager());
            }
        });

        cityEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cityPicker.showDialog(getSupportFragmentManager());
            }
        });




    }

    public void getStateJson() throws JSONException {
        String json = null;
        try {
            InputStream inputStream = getAssets().open("states.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            json = new String(buffer, StandardCharsets.UTF_8);


        } catch (IOException e) {
            e.printStackTrace();
        }


        JSONObject jsonObject = new JSONObject(json);
        JSONArray events = jsonObject.getJSONArray("states");
        for (int j = 0; j < events.length(); j++) {
            JSONObject cit = events.getJSONObject(j);
            State stateData = new State();

            stateData.setStateId(Integer.parseInt(cit.getString("id")));
            stateData.setStateName(cit.getString("name"));
            stateData.setCountryId(Integer.parseInt(cit.getString("country_id")));
            stateObject.add(stateData);
        }
    }

    public void getCityJson() throws JSONException {
        String json = null;
        try {
            InputStream inputStream = getAssets().open("cities.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            json = new String(buffer, StandardCharsets.UTF_8);


        } catch (IOException e) {
            e.printStackTrace();
        }


        JSONObject jsonObject = new JSONObject(json);
        JSONArray events = jsonObject.getJSONArray("cities");
        for (int j = 0; j < events.length(); j++) {
            JSONObject cit = events.getJSONObject(j);
            City cityData = new City();

            cityData.setCityId(Integer.parseInt(cit.getString("id")));
            cityData.setCityName(cit.getString("name"));
            cityData.setStateId(Integer.parseInt(cit.getString("state_id")));
            cityObject.add(cityData);
        }
    }

    @Override
    public void onSelectCountry(Country country) {
        // get country name and country ID
        countryEditText.setText(country.getName());
        countryID = country.getCountryId();
        statePicker.equalStateObject.clear();
        cityPicker.equalCityObject.clear();




        // GET STATES OF SELECTED COUNTRY
        for (int i = 0; i < stateObject.size(); i++) {
            // init state picker
            statePicker = new StatePicker.Builder().with(this).listener(this).build();
            State stateData = new State();
            if (stateObject.get(i).getCountryId() == countryID) {

                stateData.setStateId(stateObject.get(i).getStateId());
                stateData.setStateName(stateObject.get(i).getStateName());
                stateData.setCountryId(stateObject.get(i).getCountryId());
                stateData.setFlag(country.getFlag());
                statePicker.equalStateObject.add(stateData);
            }
        }

        stateEditText.setVisibility(View.VISIBLE);
    }

    // ON SELECTED STATE ADD CITY TO PICKER
    @Override
    public void onSelectState(State state) {

        stateEditText.setText(state.getStateName());
        stateID = state.getStateId();
        cityEditText.setVisibility(View.VISIBLE);
        cityPicker.equalCityObject.clear();


        for(int i = 0; i < cityObject.size(); i++) {
            cityPicker = new CityPicker.Builder().with(this).listener(this).build();
            City cityData = new City();
            if (cityObject.get(i).getStateId() == stateID) {
                cityData.setCityId(cityObject.get(i).getCityId());
                cityData.setCityName(cityObject.get(i).getCityName());
                cityData.setStateId(cityObject.get(i).getStateId());

                cityPicker.equalCityObject.add(cityData);
            }
        }
    }

    // ON SELECTED CITY
    @Override
    public void onSelectCity(City city) {

        cityEditText.setText(city.getCityName());
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

}
