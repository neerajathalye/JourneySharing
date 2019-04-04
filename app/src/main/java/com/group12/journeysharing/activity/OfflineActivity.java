package com.group12.journeysharing.activity;


import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.connection.AdvertisingOptions;
import com.google.android.gms.nearby.connection.ConnectionInfo;
import com.google.android.gms.nearby.connection.ConnectionLifecycleCallback;
import com.google.android.gms.nearby.connection.ConnectionResolution;
import com.google.android.gms.nearby.connection.ConnectionsClient;
import com.google.android.gms.nearby.connection.ConnectionsStatusCodes;
import com.google.android.gms.nearby.connection.DiscoveredEndpointInfo;
import com.google.android.gms.nearby.connection.DiscoveryOptions;
import com.google.android.gms.nearby.connection.EndpointDiscoveryCallback;
import com.google.android.gms.nearby.connection.Payload;
import com.google.android.gms.nearby.connection.PayloadCallback;
import com.google.android.gms.nearby.connection.PayloadTransferUpdate;
import com.google.android.gms.nearby.connection.Strategy;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;
import com.group12.journeysharing.R;
import com.group12.journeysharing.adapter.OfflineAdapter;
import com.group12.journeysharing.model.OfflineJourney;
import com.group12.journeysharing.model.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import static org.apache.commons.lang3.StringUtils.equalsIgnoreCase;


public class OfflineActivity extends AppCompatActivity implements View.OnClickListener, OfflineAdapter.ClickListener {

    TextInputLayout fromTIL, toTIL, timeTIL;
    Button searchJourneyButton;
    RecyclerView offlineRecyclerView;
    OfflineAdapter offlineAdapter;
    Calendar calendar;
    ProgressBar progressBar;
    String from, to, time, createdBy;
    User user;
    ConnectionsClient connectionsClient;
    String codeName;
    ArrayList<OfflineJourney> offlineJourneys;

    private static final String[] REQUIRED_PERMISSIONS =
            new String[] {
                    Manifest.permission.BLUETOOTH,
                    Manifest.permission.BLUETOOTH_ADMIN,
                    Manifest.permission.ACCESS_WIFI_STATE,
                    Manifest.permission.CHANGE_WIFI_STATE,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
            };

    private static final int REQUEST_CODE_REQUIRED_PERMISSIONS = 1;
    private static final Strategy STRATEGY = Strategy.P2P_CLUSTER;
    private static final String SERVICE_ID = "com.group12.journeysharing";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline);
        initialize();
    }


    private void initialize() {

        fromTIL = findViewById(R.id.fromTIL);
        toTIL = findViewById(R.id.toTIL);
        timeTIL = findViewById(R.id.timeTIL);
        searchJourneyButton = findViewById(R.id.searchJourneyButton);
        offlineRecyclerView = findViewById(R.id.offlineRecyclerView);
        timeTIL.getEditText().setFocusable(false);
        searchJourneyButton.setOnClickListener(this);
        timeTIL.getEditText().setOnClickListener(this);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        from = "";
        to = "";
        time = "";

        connectionsClient = Nearby.getConnectionsClient(this);
        offlineJourneys = new ArrayList<>();
        offlineAdapter = new OfflineAdapter(this, offlineJourneys, connectionsClient, connectionLifecycleCallback);
        offlineRecyclerView.setAdapter(offlineAdapter);
        offlineRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        SharedPreferences sharedPref = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        user = new Gson().fromJson(sharedPref.getString("user", null), User.class);
        offlineAdapter.setOnItemClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == searchJourneyButton.getId()) {
            if(equalsIgnoreCase(searchJourneyButton.getText(), "Search"))
            {
                progressBar.setVisibility(View.VISIBLE);
                fromTIL.setVisibility(View.GONE);
                toTIL.setVisibility(View.GONE);
                timeTIL.setVisibility(View.GONE);
                searchJourneyButton.setText("Cancel");

                from = fromTIL.getEditText().getText().toString();
                to = toTIL.getEditText().getText().toString();
                time = timeTIL.getEditText().getText().toString();
                createdBy = user.getFullName();

                codeName = from + ";" + to + ";" + time + ";" + createdBy;
                startAdvertising();
                startDiscovery();

            }
            else if(equalsIgnoreCase(searchJourneyButton.getText(), "Cancel"))
            {
                fromTIL.setVisibility(View.VISIBLE);
                toTIL.setVisibility(View.VISIBLE);
                timeTIL.setVisibility(View.VISIBLE);
                searchJourneyButton.setText("Search");
                stopNearbyConnections();
                progressBar.setVisibility(View.GONE);
            }
        }
        else if(v.getId() == timeTIL.getEditText().getId())
        {
            showDateTimePicker();
        }
    }

    public void startAdvertising() {
        Toast.makeText(this, "In startAdvertising()", Toast.LENGTH_SHORT).show();
        AdvertisingOptions advertisingOptions = new AdvertisingOptions.Builder().setStrategy(STRATEGY).build();
        connectionsClient.startAdvertising(codeName, SERVICE_ID, connectionLifecycleCallback, advertisingOptions)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        // We're advertising!
                        Toast.makeText(OfflineActivity.this, "Advertising successful", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("ADVERTISING FAILURE: ", e.getMessage());
                        // We were unable to start advertising.
                        stopNearbyConnections();
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(OfflineActivity.this, "Unable to start advertising", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void startDiscovery() {
        DiscoveryOptions discoveryOptions = new DiscoveryOptions.Builder().setStrategy(STRATEGY).build();
        connectionsClient.startDiscovery(SERVICE_ID, endpointDiscoveryCallback, discoveryOptions)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        // We're discovering!
                        Toast.makeText(OfflineActivity.this, "Discovery started successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // We're unable to start discovering.
                                Log.d("DISCOVERY FAILURE: ", e.getMessage());
                                Toast.makeText(OfflineActivity.this, "Unable to start discovery", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                                stopNearbyConnections();
                            }
                        });
    }


    private final EndpointDiscoveryCallback endpointDiscoveryCallback = new EndpointDiscoveryCallback() {
        @Override
        public void onEndpointFound(@NonNull String endpointId, @NonNull final DiscoveredEndpointInfo info) {
            // An endpoint was found. We request a connection to it.

            String[] data = info.getEndpointName().split(";");

            Log.d("::::::", data[0]);
            Log.d("::::::", data[1]);
            Log.d("::::::", data[2]);
            Log.d("::::::", data[3]);
            OfflineJourney offlineJourney = new OfflineJourney();
            offlineJourney.setCodeName(info.getEndpointName());
            offlineJourney.setEndPointId(endpointId);
            offlineJourney.setFrom(data[0]);
            offlineJourney.setTo(data[1]);
            offlineJourney.setTime(data[2]);
            offlineJourney.setCreatedBy(data[3]);

            offlineJourneys.add(offlineJourney);
            offlineAdapter.notifyDataSetChanged();

            progressBar.setVisibility(View.GONE);
        }

        @Override
        public void onEndpointLost(@NonNull String endpointId) {
            // A previously discovered endpoint has gone away.
            Toast.makeText(OfflineActivity.this, endpointId + " lost", Toast.LENGTH_SHORT).show();
            stopNearbyConnections();
            progressBar.setVisibility(View.GONE);
        }
    };

    private final ConnectionLifecycleCallback connectionLifecycleCallback = new ConnectionLifecycleCallback() {
        @Override
        public void onConnectionInitiated(@NonNull final String endpointId, ConnectionInfo connectionInfo) {
            // Automatically accept the connection on both sides.

            String data[] = connectionInfo.getEndpointName().split(";");

            new AlertDialog.Builder(OfflineActivity.this)
                    .setTitle("Accept connection to " + data[3])
                    .setMessage("Confirm the code matches on both devices: " + connectionInfo.getAuthenticationToken())
                    .setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            connectionsClient.acceptConnection(endpointId, payloadCallback);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            connectionsClient.rejectConnection(endpointId);
                        }
                    })
                    .show();
            Toast.makeText(OfflineActivity.this, "Connection initiated", Toast.LENGTH_SHORT).show();
//                    connectionsClient.acceptConnection(endpointId, payloadCallback);
        }

        @Override
        public void onConnectionResult(@NonNull String endpointId, ConnectionResolution result) {

            progressBar.setVisibility(View.GONE);

            stopNearbyConnections();
            switch (result.getStatus().getStatusCode()) {
                case ConnectionsStatusCodes.STATUS_OK:
                    Toast.makeText(OfflineActivity.this, "Connected", Toast.LENGTH_SHORT).show();
                    // We're connected! Can now start sending and receiving data.

                    String data = "qwertyuiop";
                    byte[] bytes = data.getBytes();
                    Payload bytesPayload = Payload.fromBytes(bytes);
                    connectionsClient.sendPayload(endpointId, bytesPayload);
//                    Toast.makeText(OfflineActivity.this, "Received: " + bytesPayload.toString(), Toast.LENGTH_SHORT).show();

                    break;
                case ConnectionsStatusCodes.STATUS_CONNECTION_REJECTED:
                    Toast.makeText(OfflineActivity.this, "Connection Rejected", Toast.LENGTH_SHORT).show();
                    // The connection was rejected by one or both sides.
                    break;
                case ConnectionsStatusCodes.STATUS_ERROR:
                    Toast.makeText(OfflineActivity.this, "Connection Error", Toast.LENGTH_SHORT).show();
                    // The connection broke before it was able to be accepted.
                    break;
                default:
                    // Unknown status code
            }
        }

        @Override
        public void onDisconnected(@NonNull String endpointId) {
            // We've been disconnected from this endpoint. No more data can be
            // sent or received.
            Toast.makeText(OfflineActivity.this, "Disconnected", Toast.LENGTH_SHORT).show();
            stopNearbyConnections();
            progressBar.setVisibility(View.GONE);
        }
    };

    private final PayloadCallback payloadCallback = new PayloadCallback() {
        @Override
        public void onPayloadReceived(@NonNull String s, @NonNull Payload payload) {
            Toast.makeText(OfflineActivity.this, "Payload Received", Toast.LENGTH_SHORT).show();
            byte[] receivedBytes = payload.asBytes();
            String string = new String(receivedBytes);
            if (receivedBytes != null) {
                Toast.makeText(OfflineActivity.this, "Received: " + string, Toast.LENGTH_SHORT).show();
            }
            stopNearbyConnections();
        }

        @Override
        public void onPayloadTransferUpdate(@NonNull String s, @NonNull PayloadTransferUpdate payloadTransferUpdate) {

        }
    };

    public void stopNearbyConnections()
    {
        connectionsClient.stopAdvertising();
        connectionsClient.stopDiscovery();
        connectionsClient.stopAllEndpoints();

    }

    @Override
    protected void onStart() {
        super.onStart();

        if (!hasPermissions(this, REQUIRED_PERMISSIONS)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(REQUIRED_PERMISSIONS, REQUEST_CODE_REQUIRED_PERMISSIONS);
            }
        }
    }
    /** Returns true if the app was granted all the permissions. Otherwise, returns false. */
    private static boolean hasPermissions(Context context, String... permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @CallSuper
    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode != REQUEST_CODE_REQUIRED_PERMISSIONS) {
            return;
        }

        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                finish();
                return;
            }
        }
        recreate();
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopNearbyConnections();
    }

    public void showDateTimePicker() {
        final Calendar currentDate = Calendar.getInstance();
        calendar = Calendar.getInstance();

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(year, month, dayOfMonth);

                new TimePickerDialog(OfflineActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);
                        Log.d("========", "The chosen one " + calendar.getTimeInMillis());

                        SimpleDateFormat outputDateFormat = new SimpleDateFormat("EEE, d MMM HH:mm a");

                        time = outputDateFormat.format(calendar.getTimeInMillis());

                        timeTIL.getEditText().setText(time);
                    }
                }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), false).show();
            }
        }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE));

        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() + 864000000); // max 10 days in the future
        datePickerDialog.show();
    }

    @Override
    public void onItemClick(int position, View v) {
        Toast.makeText(this, "Clicked", Toast.LENGTH_SHORT).show();
                connectionsClient.requestConnection(offlineJourneys.get(position).getCodeName(), offlineJourneys.get(position).getEndPointId(), connectionLifecycleCallback);
    }
}
