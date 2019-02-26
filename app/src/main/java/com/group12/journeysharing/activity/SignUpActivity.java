package com.group12.journeysharing.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import com.group12.journeysharing.R;


public class SignUpActivity extends AppCompatActivity{

    EditText countryEditText, stateEditText, cityEditText;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        countryEditText = findViewById(R.id.countryEditText);
        stateEditText = findViewById(R.id.stateEditText);
        cityEditText = findViewById(R.id.cityEditText);

    }


}
