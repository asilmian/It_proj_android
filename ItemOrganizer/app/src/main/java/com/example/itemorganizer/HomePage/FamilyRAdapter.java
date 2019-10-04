package com.example.itemorganizer.HomePage;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.itemorganizer.R;


import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class FamilyRAdapter extends RecyclerView.Adapter<FamilyRAdapter.FamViewHolder> {

    private static final String TAG = "FamilyRAdapter";

    private ArrayList<String> families;

    public static class FamViewHolder extends RecyclerView.ViewHolder{
        public TextView tx;
        public CardView cardView;
        public FamViewHolder(View v){
            super(v);
            tx = v.findViewById(R.id.famName);
            cardView = v.findViewById(R.id.card_view);
        }
    }

    public FamilyRAdapter(ArrayList<String> families){
        this.families = families;
    }

    @Override
    public FamilyRAdapter.FamViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fam_card_view, parent, false);

        FamViewHolder vh = new FamViewHolder(v);
        return vh;
    }


    // called each time a new item is added to the RecyclerView container
    @Override
    public void onBindViewHolder(@NonNull FamViewHolder viewHolder, int i) {

        Log.d(TAG, "onBindViewHolder: called with i: " + i);

        viewHolder.tx.setText(families.get(i));

        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Waddup", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: size: " + families.size());
        return families.size();
    }


    /*
     *   Public exposed functionality
     */
    public void addAndNotify(String added){
        families.add(families.size(), added);
        notifyItemInserted(families.size());
    }
}
