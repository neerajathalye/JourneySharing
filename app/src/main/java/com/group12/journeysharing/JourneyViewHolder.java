package com.group12.journeysharing;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.group12.journeysharing.R;

/**
 * Created by Neeraj Athalye on 21-Mar-19.
 */
public class JourneyViewHolder extends RecyclerView.ViewHolder {

    TextView fromTextView, toTextView, timeTextView;

    public JourneyViewHolder(@NonNull View itemView) {
        super(itemView);

        fromTextView = itemView.findViewById(R.id.fromTextView);
        toTextView = itemView.findViewById(R.id.toTextView);
        timeTextView = itemView.findViewById(R.id.timeTextView);
    }
}
