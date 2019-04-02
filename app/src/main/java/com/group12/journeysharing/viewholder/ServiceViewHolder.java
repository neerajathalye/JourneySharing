package com.group12.journeysharing.viewholder;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.group12.journeysharing.R;

/**
 * Created by Neeraj Athalye on 01-Apr-19.
 */
public class ServiceViewHolder extends RecyclerView.ViewHolder {

    public TextView serviceNameTextView, fromTextView, deviceNameTextView, addressTextView;
    public ConstraintLayout serviceContainer;

    public ServiceViewHolder(@NonNull View itemView) {
        super(itemView);

        serviceNameTextView = itemView.findViewById(R.id.serviceNameTextView);
        fromTextView = itemView.findViewById(R.id.fromTextView);
        deviceNameTextView = itemView.findViewById(R.id.deviceNameTextView);
        addressTextView = itemView.findViewById(R.id.addressTextView);
        serviceContainer = itemView.findViewById(R.id.serviceContainer);
    }
}
