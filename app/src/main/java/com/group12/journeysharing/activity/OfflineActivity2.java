package com.group12.journeysharing.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceInfo;
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceRequest;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.group12.journeysharing.R;
import com.group12.journeysharing.WiFiDirectBroadcastReceiver;
import com.group12.journeysharing.adapter.ServiceAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OfflineActivity2 extends AppCompatActivity implements View.OnClickListener, WifiP2pManager.PeerListListener, WifiP2pManager.ConnectionInfoListener, WifiP2pManager.ActionListener {

    FirebaseAuth firebaseAuth;
    Button wifiStateButton, discoverButton;
    RecyclerView serviceRecyclerView;
    public TextView connectionStatusTextView;
    WifiP2pDnsSdServiceInfo serviceInfo;

    WifiManager wifiManager;
    WifiP2pManager mManager;
    WifiP2pManager.Channel mChannel;
    BroadcastReceiver mReceiver;
    IntentFilter mIntentFilter;
    ServiceAdapter serviceAdapter;

    Map<String, String>  record;
    ArrayList<Map<String, String>> records;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline2);

        initialize();
        startRegistration();

    }

    private void startRegistration() {

        Map<String, String>  data = new HashMap<>();
        data.put("from", "Dublin");
        data.put("to", "Cork");
        data.put("time", "4th April");
        data.put("email", firebaseAuth.getCurrentUser().getEmail());

        // Service information.  Pass it an instance name, service type
        // _protocol._transportlayer , and the map containing
        // information other devices will want once they connect to this one.
        WifiP2pDnsSdServiceInfo serviceInfo = WifiP2pDnsSdServiceInfo.newInstance("_journey_sharing", "_presence._tcp", data);

        // Add the local service, sending the service info, network channel,
        // and listener that will be used to indicate success or failure of
        // the request.
        mManager.addLocalService(mChannel, serviceInfo, this);
    }

    @Override
    public void onSuccess() {
        // Command successful! Code isn't necessarily needed here,
        // Unless you want to update the UI or add logging statements.
        connectionStatusTextView.setText("Local Service Added");

        discoverService();

    }

    @Override
    public void onFailure(int reason) {
        // Command failed.  Check for P2P_UNSUPPORTED, ERROR, or BUSY
        switch (reason)
        {
            case WifiP2pManager.ERROR:
                connectionStatusTextView.setText("Error");
                break;
            case WifiP2pManager.P2P_UNSUPPORTED:
                connectionStatusTextView.setText("P2P Unsupported");
                break;
            case WifiP2pManager.BUSY:
                connectionStatusTextView.setText("Busy");
                break;
            case WifiP2pManager.NO_SERVICE_REQUESTS:
                connectionStatusTextView.setText("No Service Requests");
                break;
        }

    }

    public void discoverService() {

        WifiP2pManager.DnsSdTxtRecordListener txtListener = new WifiP2pManager.DnsSdTxtRecordListener() {

            /* Callback includes:
             * fullDomain: full domain name: e.g "printer._ipp._tcp.local."
             * record: TXT record dta as a map of key/value pairs.
             * device: The device running the advertised service.
             */
            @Override
            public void onDnsSdTxtRecordAvailable(
                    String fullDomain, Map record, WifiP2pDevice device) {
                Log.d("In discoverService(): ", "DnsSdTxtRecord available -" + record.toString());
//                record.put(device.deviceAddress, record.get("buddyname"));
            }
        };

        WifiP2pManager.DnsSdServiceResponseListener servListener = new WifiP2pManager.DnsSdServiceResponseListener() {
            @Override
            public void onDnsSdServiceAvailable(String instanceName, String registrationType,
                                                WifiP2pDevice resourceType) {

                // Update the device name with the human-friendly version from
                // the DnsTxtRecord, assuming one arrived.
//                resourceType.deviceName = buddies
//                        .containsKey(resourceType.deviceAddress) ? buddies
//                        .get(resourceType.deviceAddress) : resourceType.deviceName;

                // Add to the custom adapter defined specifically for showing
                // wifi devices.

                records.add(record);
                serviceAdapter.notifyItemInserted(records.size());


                Log.d("onDnsSdServiceAvailable", "(): " + instanceName);


            }
        };

        mManager.setDnsSdResponseListeners(mChannel, servListener, txtListener);

//        serviceRequest = WifiP2pDnsSdServiceRequest.newInstance();
//        mManager.addServiceRequest(mChannel, serviceRequest,
//            new WifiP2pManager.ActionListener() {
//                @Override
//                public void onSuccess() {
//                    // Success!
//                }
//
//                @Override
//                public void onFailure(int code) {
//                    // Command failed.  Check for P2P_UNSUPPORTED, ERROR, or BUSY
//                }
//            });


    }


    @Override
    public void onClick(View v) {

        if(v.getId() == wifiStateButton.getId())
        {

        }
        else if(v.getId() == discoverButton.getId())
        {

        }
    }

    @Override
    public void onPeersAvailable(WifiP2pDeviceList peers) {

    }

    @Override
    public void onConnectionInfoAvailable(WifiP2pInfo info) {

    }

    public void initialize() {
        firebaseAuth = FirebaseAuth.getInstance();
        record = new HashMap<>();
        records = new ArrayList<>();
        wifiStateButton = findViewById(R.id.wifiStateButton);
        discoverButton = findViewById(R.id.discoverButton);
        serviceRecyclerView = findViewById(R.id.serviceRecyclerView);
        serviceAdapter = new ServiceAdapter(OfflineActivity2.this, records);
        serviceRecyclerView.setAdapter(serviceAdapter);
        serviceRecyclerView.setLayoutManager(new LinearLayoutManager(OfflineActivity2.this));
        connectionStatusTextView = findViewById(R.id.connectionStatusTextView);
        wifiManager= (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        mManager= (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel=mManager.initialize(this,getMainLooper(),null);
        mReceiver=new WiFiDirectBroadcastReceiver(mManager, mChannel, this);
        mIntentFilter=new IntentFilter();
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
        wifiStateButton.setOnClickListener(this);
        discoverButton.setOnClickListener(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mReceiver,mIntentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mReceiver);
    }


}
