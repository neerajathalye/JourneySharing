package com.group12.journeysharing.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.group12.journeysharing.R;
import com.group12.journeysharing.model.User;
import com.hbb20.CountryCodePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;


public class SignUpActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    EditText firstNameEditText, lastNameEditText, dobEditText, phoneNumberEditText, emailEditText, passwordEditText, confirmPasswordEditText, emNameEditText, emPhoneEditText, emEmailEditText;
    RadioGroup radioGroup;
    Button verifyButton, submitButton;
    CountryCodePicker countryCodePicker;
    RadioButton mRadioButton, fRadioButton, oRadioButton;

    TextView genderTextView;

    FirebaseAuth mAuth;
    DatabaseReference mDatabase;
    User user;

    private String mVerificationId;
    boolean isPhoneVerified = false;

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        firstNameEditText = findViewById(R.id.firstNameEditText);
        lastNameEditText = findViewById(R.id.lastNameEditText);
        dobEditText = findViewById(R.id.dobEditText);
        phoneNumberEditText = findViewById(R.id.phoneEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        emNameEditText = findViewById(R.id.emergencyNameEditText);
        emPhoneEditText = findViewById(R.id.emergencyPhoneEditText);
        emEmailEditText = findViewById(R.id.emergencyEmailEditText);
        radioGroup = findViewById(R.id.radioGroup);
        verifyButton = findViewById(R.id.verifyButton);
        submitButton = findViewById(R.id.buttonSubmit);
        countryCodePicker = findViewById(R.id.countryCodePicker);
        mRadioButton = findViewById(R.id.mRadioButton);
        fRadioButton = findViewById(R.id.fRadioButton);
        oRadioButton = findViewById(R.id.oRadioButton);
        genderTextView = findViewById(R.id.genderTextView);

        user = new User();

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();


        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                Log.d(":::::::::::::", "onVerificationCompleted:" + credential);

                String code = credential.getSmsCode();


                Toast.makeText(SignUpActivity.this, "Phone Number Verified: " + code, Toast.LENGTH_SHORT).show();

                phoneNumberEditText.setEnabled(false);
                countryCodePicker.setEnabled(false);
                verifyButton.setEnabled(false);
                isPhoneVerified = true;

                otpVerificationDialog(code);


            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.

                Toast.makeText(SignUpActivity.this, "Verification Failed", Toast.LENGTH_SHORT).show();
                phoneNumberEditText.setError("Invalid Phone number");
                Log.w(":::::::::::::", "onVerificationFailed", e);

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    // ...
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    // ...
                }
            }

            @Override
            public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d(":::::::::::::", "onCodeSent:" + verificationId);

                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
//                mResendToken = token;





            }
        };

        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String phoneNumber = "+" + countryCodePicker.getSelectedCountryCode() + phoneNumberEditText.getText().toString();

                verifyPhoneNumber(phoneNumber);
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String firstName = firstNameEditText.getText().toString().trim();
                String lastName = lastNameEditText.getText().toString().trim();
                String email = emailEditText.getText().toString().trim();
                String phoneNumber = "+" + countryCodePicker.getSelectedCountryCode() + phoneNumberEditText.getText().toString();
                String password = passwordEditText.getText().toString().trim();
                String confirmPassword = confirmPasswordEditText.getText().toString().trim();
                String emName = emNameEditText.getText().toString().trim();
                String emPhone = emPhoneEditText.getText().toString().trim();
                String emEmail = emEmailEditText.getText().toString().trim();


                if(firstName.isEmpty())
                    firstNameEditText.setError(getString(R.string.empty_error));
                else if(lastName.isEmpty())
                    lastNameEditText.setError(getString(R.string.empty_error));
                else if(dobEditText.getText().toString().isEmpty())
                    dobEditText.setError(getString(R.string.empty_error));
                else if(!(mRadioButton.isChecked() || fRadioButton.isChecked() || oRadioButton.isChecked()))
                    genderTextView.setError("Select Gender");
                else if (!isPhoneVerified)
                    verifyButton.setError("Phone Number not verified");
                else if(email.isEmpty())
                    emailEditText.setError(getString(R.string.empty_error));
                else if (!isValidEmail(email))
                    emailEditText.setError("Invalid Email Address");
                else if(password.isEmpty())
                    passwordEditText.setError(getString(R.string.empty_error));
                else if(password.length() < 8 || password.length() > 16)
                    passwordEditText.setError("Password must be 8-16 characters long");
                else if(confirmPassword.isEmpty())
                    confirmPasswordEditText.setError(getString(R.string.empty_error));
                else if(!password.equals(confirmPassword))
                    confirmPasswordEditText.setError("Password mismatch");

                else
                {
                    if(emName.isEmpty())
                        emName = "";
                    if(emPhone.isEmpty())
                        emPhone = "";
                    if(emEmail.isEmpty())
                        emEmail = "";

                    user.setFirstName(firstName);
                    user.setLastName(lastName);
                    user.setFullName();
                    user.setEmail(email);
                    user.setPhoneNumber(phoneNumber);
                    user.setEmergencyName(emName);
                    user.setEmergencyPhoneNumber(emPhone);
                    user.setEmergencyEmail(emEmail);

                    Toast.makeText(SignUpActivity.this, "user details entered", Toast.LENGTH_SHORT).show();

                    Log.d("FULL NAME :::::::::::::", user.getFullName());
                    Log.d("DOB :::::::::::::::::::", user.getDob().toString());
                    Log.d("EM GENDER :::::::::::::", user.getGender());
                    Log.d("PHONE NUMBER ::::::::::", user.getPhoneNumber());
                    Log.d("EMAIL :::::::::::::::::", user.getEmail());
                    Log.d("EM MAIL :::::::::::::::", user.getEmergencyEmail());
                    Log.d("EM NAME :::::::::::::::", user.getEmergencyName());
                    Log.d("EM PHONE ::::::::::::::", user.getEmergencyPhoneNumber());


                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful())
                                    {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d(":::::::::::::::::::::::", "createUserWithEmail:success");

                                        final FirebaseUser firebaseUser = mAuth.getCurrentUser();
                                        assert firebaseUser != null;
                                        firebaseUser.sendEmailVerification()
                                                .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task task) {

                                                        if (task.isSuccessful()) {

                                                            //Add user to database

                                                            user.setUserId(firebaseUser.getUid());

                                                            mDatabase.child("user").child(user.getUserId()).setValue(user);

                                                            Toast.makeText(SignUpActivity.this, "Verification email sent to " + firebaseUser.getEmail(), Toast.LENGTH_SHORT).show();
                                                            Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                                                            startActivity(intent);
                                                            finish();
                                                        } else {
                                                            Log.e(":::::::::::::::::::::::", "sendEmailVerification", task.getException());
                                                            Toast.makeText(SignUpActivity.this, "Failed to send verification email.", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                    }
                                    else {
                                        // If sign in fails, display a message to the user.
                                        Log.w(":::::::::::::::::::::::", "createUserWithEmail:failure", task.getException());
                                        Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
//                                        updateUI(null);
                                    }

                                    // ...
                                }
                            });

                }

            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                genderTextView.setError(null);
                if (checkedId == R.id.mRadioButton) {
                    Toast.makeText(SignUpActivity.this, "Male", Toast.LENGTH_SHORT).show();
                    user.setGender("Male");
                }
                else if (checkedId == R.id.fRadioButton) {
                    Toast.makeText(SignUpActivity.this, "Female", Toast.LENGTH_SHORT).show();
                    user.setGender("Female");
                }
                else if (checkedId == R.id.oRadioButton) {
                    Toast.makeText(SignUpActivity.this, "Other", Toast.LENGTH_SHORT).show();
                    user.setGender("Other");
                }
            }
        });

        String string_date = "01-January-1900";
        long date = 0;

        @SuppressLint("SimpleDateFormat") SimpleDateFormat f = new SimpleDateFormat("dd-MMM-yyyy");
        try {
            Date d = f.parse(string_date);
            date = d.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        final DatePickerDialog datePickerDialog = new DatePickerDialog(this, this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());

        datePickerDialog.getDatePicker().setMinDate(date);

        dobEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    datePickerDialog.show();
            }
        });

    }

    private void verifyPhoneNumber(String phoneNumber) {

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                30,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                SignUpActivity.this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks

    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        String dateString = dayOfMonth + "/" + (month+1) + "/" + year;
        dobEditText.setText(dateString);
        dobEditText.setError(null);

//        Date date = new Date(year, month, dayOfMonth);

        user.setDob(dateString);

        Toast.makeText(this, dateString, Toast.LENGTH_SHORT).show();

//        Toast.makeText(this, "Date: " + dayOfMonth + "/" + month + "/" + year, Toast.LENGTH_SHORT).show();
    }
    public static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    void otpVerificationDialog(final String code)
    {
        final AlertDialog.Builder alert = new AlertDialog.Builder(SignUpActivity.this);
        final EditText editText = new EditText(SignUpActivity.this);
        editText.setSingleLine();
        if(code != null)
            editText.setText(code);
        FrameLayout container = new FrameLayout(SignUpActivity.this);
        FrameLayout.LayoutParams params = new  FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.leftMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
        params.rightMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
        params.topMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
        params.bottomMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
        editText.setLayoutParams(params);
        container.addView(editText);
        alert.setTitle("Enter OTP");
        alert.setView(container);
        alert.setPositiveButton(R.string.submit, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String otp = String.valueOf(editText.getText());
                if(otp.equals(code))
                    Toast.makeText(SignUpActivity.this, "OTP VERIFIED", Toast.LENGTH_SHORT).show();
            }
        });
        alert.setNegativeButton(getString(R.string.cancel), null);
        alert.create();
        alert.show();
    }

}
