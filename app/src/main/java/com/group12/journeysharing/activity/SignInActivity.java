package com.group12.journeysharing.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.group12.journeysharing.R;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {



    private Button signUpButton;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button signInButton;
    private TextView forgotPassword;


    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        firebaseAuth = FirebaseAuth.getInstance();


        editTextEmail = findViewById(R.id.emailField);
        editTextPassword = findViewById(R.id.passwordField);
        signInButton = findViewById(R.id.buttonSignin);
        signUpButton = findViewById(R.id.buttonRegister);
        forgotPassword = findViewById(R.id.forgotPassword);
        signInButton.setOnClickListener(this);
        signUpButton.setOnClickListener(this);
        forgotPassword.setOnClickListener(this);


    }


    private void userLogin(){
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        boolean validDetails = isValidEmailAndPassword(email, password);

        if(validDetails)
        {
            //TODO: Check if email is verified
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        //start profile activity
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        if (user.isEmailVerified()) {
                            Toast.makeText(SignInActivity.this, "Email Verified", Toast.LENGTH_SHORT).show();

                            //TODO: Download user data from the database

                            //TODO: Pass user data to HomeActivity
                            finish();
                            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        }
                        else {
                            Toast.makeText(SignInActivity.this, "Email Not Verified", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                {
                    Toast.makeText(SignInActivity.this, "Invalid Email/Password", Toast.LENGTH_SHORT).show();
                }
                }
            });





        }
    }
    private boolean isValidEmailAndPassword(String email, String password) {
        if(TextUtils.isEmpty(email)) {

            //email is empty
            Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(TextUtils.isEmpty(password)) {
            //password is empty
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
            return false;
        }
               return true;
    }

    @Override
    public void onClick(View view) {
        if(view == signInButton){
            userLogin();
        }
        if(view == signUpButton){
            Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
            startActivity(intent);
//            finish();
        }
        if(view == forgotPassword) {

            final AlertDialog.Builder alert = new AlertDialog.Builder(SignInActivity.this);
            final EditText editText = new EditText(SignInActivity.this);
            editText.setSingleLine();
            FrameLayout container = new FrameLayout(SignInActivity.this);
            FrameLayout.LayoutParams params = new  FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.leftMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
            params.rightMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
            params.topMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
            params.bottomMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
            editText.setLayoutParams(params);
            container.addView(editText);
            alert.setTitle("Enter Email");
            alert.setView(container);
            alert.setPositiveButton(R.string.send_email, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String email = String.valueOf(editText.getText());
                    if (TextUtils.isEmpty(email)) {
                        editText.setError("Email address cannot be empty!");
                        Toast.makeText(SignInActivity.this, "Please enter valid email address", Toast.LENGTH_SHORT).show();
                    } else {
                        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()) {
                                    Toast.makeText(SignInActivity.this, "Please check your email account...", Toast.LENGTH_SHORT).show();

                                } else {
                                    String message = task.getException().getMessage();
                                    Toast.makeText(SignInActivity.this, "Error Occured: " + message, Toast.LENGTH_SHORT).show();

                                }
                            }
                        });


                    }

                }
            });
            alert.setNegativeButton(getString(R.string.cancel), null);
            alert.create();
            alert.show();


        }


    }



}
