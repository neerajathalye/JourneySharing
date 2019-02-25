package com.group12.journeysharing.activity;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.group12.journeysharing.R;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;


public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private Button buttonSubmit;
    private EditText editTextFirstName;
    private EditText editTextSecondName;
    private ProgressDialog progressBarDialog;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    @Override
    public void onClick(View view) {
        if(view == buttonSubmit) {
            Toast.makeText(this, "register pressed", Toast.LENGTH_SHORT).show();
            registerUser();
        }

    }

    private void registerUser()
    {
        String firstName = editTextFirstName.getText().toString().trim();
        String secondName = editTextSecondName.getText().toString().trim();

        if(TextUtils.isEmpty(firstName))
        {
            Toast.makeText(this,"Enter FirstName",Toast.LENGTH_SHORT).show();
        return;
        }

        progressBarDialog.setMessage("Loading");
        progressBarDialog.show();

        firebaseAuth.

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        firebaseAuth = FirebaseAuth.getInstance();

        buttonSubmit = (Button) findViewById(R.id.buttonSubmit);
        editTextFirstName= (EditText) findViewById(R.id.editText);
        editTextSecondName= (EditText) findViewById(R.id.editText2);
        buttonSubmit.setOnClickListener(this);

        progressBarDialog = new ProgressDialog(this);
    }
}
