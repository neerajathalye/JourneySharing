package com.group12.journeysharing.adapter;

import android.content.Context;
import android.net.wifi.p2p.WifiP2pManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.group12.journeysharing.R;
import com.group12.journeysharing.viewholder.ServiceViewHolder;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Neeraj Athalye on 01-Apr-19.
 */
public class ServiceAdapter extends RecyclerView.Adapter<ServiceViewHolder> {

    Context context;
    ArrayList<Map<String, String>> records;

    public ServiceAdapter(Context context, ArrayList<Map<String, String>> records) {
        this.context = context;
        this.records = records;
    }

    @NonNull
    @Override
    public ServiceViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.service_row, viewGroup, false);

        return new ServiceViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceViewHolder serviceViewHolder, int i) {


    }

    @Override
    public int getItemCount() {
        return records.size();
    }
}
