package com.example.itemorganizer;

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

public class MemberRAdapter extends RecyclerView.Adapter<MemberRAdapter.MemberViewHolder> {

    private static final String TAG = "MemberRAdapter";

    private ArrayList<ArrayList<String>> members;

    public static class MemberViewHolder extends RecyclerView.ViewHolder{
        public TextView tx;
        public CardView cardView;
        public String id;
        public MemberViewHolder(View v){
            super(v);
            tx = v.findViewById(R.id.memberName);
            cardView = v.findViewById(R.id.card_view);
        }
    }

    public MemberRAdapter(ArrayList<ArrayList<String>> members){
        this.members = members;
    }

    @Override
    public MemberRAdapter.MemberViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.member_card_view, parent, false);

        MemberViewHolder vh = new MemberViewHolder(v);
        return vh;
    }


    // called each time a new item is added to the RecyclerView container
    @Override
    public void onBindViewHolder(@NonNull final MemberViewHolder viewHolder, int i) {

        Log.d(TAG, "onBindViewHolder: called with i: " + i);

        viewHolder.id = (members.get(i).get(0)); //Get and Store ID
        viewHolder.tx.setText(members.get(i).get(1)); //Set name

        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), viewHolder.id  , Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: size: " + members.size());
        return members.size();
    }


    /*
     *   Public exposed functionality
     */
    public void addAndNotify(ArrayList<String> added){
        members.add(members.size(), added);
        notifyItemInserted(members.size());
    }
}
