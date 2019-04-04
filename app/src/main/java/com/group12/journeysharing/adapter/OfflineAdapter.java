package com.group12.journeysharing.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.nearby.connection.ConnectionLifecycleCallback;
import com.google.android.gms.nearby.connection.ConnectionsClient;
import com.group12.journeysharing.R;
import com.group12.journeysharing.model.OfflineJourney;

import java.util.ArrayList;

/**
 * Created by Neeraj Athalye on 01-Apr-19.
 */
public class OfflineAdapter extends RecyclerView.Adapter<OfflineAdapter.OfflineViewHolder> {

    private Context context;
    private ArrayList<OfflineJourney> offlineJourneys;
    private ConnectionsClient connectionsClient;
    private static ClickListener clickListener;

    public static class OfflineViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView fromTextView, toTextView, timeTextView, createdByTextView;
        public CardView container;

        public OfflineViewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);

            fromTextView = itemView.findViewById(R.id.fromTextView);
            toTextView = itemView.findViewById(R.id.toTextView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
            createdByTextView = itemView.findViewById(R.id.createdByTextView);
//            container = itemView.findViewById(R.id.container);
        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(getAdapterPosition(), v);
        }
    }

    public OfflineAdapter(Context context, ArrayList<OfflineJourney> offlineJourneys, ConnectionsClient connectionsClient, ConnectionLifecycleCallback connectionLifecycleCallback) {
        this.context = context;
        this.offlineJourneys = offlineJourneys;
        this.connectionsClient = connectionsClient;
    }

    @NonNull
    @Override
    public OfflineViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.offline_row, viewGroup, false);
        return new OfflineViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull OfflineViewHolder offlineViewHolder, int i) {
        offlineViewHolder.fromTextView.setText(offlineJourneys.get(i).getFrom());
        offlineViewHolder.toTextView.setText(offlineJourneys.get(i).getTo());
        offlineViewHolder.timeTextView.setText(offlineJourneys.get(i).getTime());
        offlineViewHolder.createdByTextView.setText(offlineJourneys.get(i).getCreatedBy());
    }

//    @Override
//    public void onBindViewHolder(@NonNull OfflineViewHolder offlineViewHolder, final int i) {
//
//        offlineViewHolder.fromTextView.setText(offlineJourneys.get(i).getFrom());
//        offlineViewHolder.toTextView.setText(offlineJourneys.get(i).getTo());
//        offlineViewHolder.timeTextView.setText(offlineJourneys.get(i).getTime());
//        offlineViewHolder.createdByTextView.setText(offlineJourneys.get(i).getCreatedBy());
//
//        offlineViewHolder.container.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show();
//                connectionsClient.requestConnection(offlineJourneys.get(i).getCodeName(), offlineJourneys.get(i).getEndPointId(), connectionLifecycleCallback);
//            }
//        });
//
//    }

    @Override
    public int getItemCount() {
        return offlineJourneys.size();
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        OfflineAdapter.clickListener = clickListener;
    }


    public interface ClickListener {
        void onItemClick(int position, View v);
//        void onItemLongClick(int position, View v);
    }
}
