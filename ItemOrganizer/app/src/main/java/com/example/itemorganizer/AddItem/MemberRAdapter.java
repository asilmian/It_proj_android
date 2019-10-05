package com.example.itemorganizer.AddItem;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.itemorganizer.R;


import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class MemberRAdapter extends RecyclerView.Adapter<MemberRAdapter.MemberViewHolder> {

    private static final String TAG = "MemberRAdapter";

    private ArrayList<ArrayList<String>> members;

    public static class MemberViewHolder extends RecyclerView.ViewHolder{
        public ToggleButton text;
        public MemberViewHolder(View v){
            super(v);
            text = v.findViewById(R.id.memberName);
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

        final ArrayList<String> member = members.get(i);

        Log.d(TAG, "onBindViewHolder: called with i: " + i);

        viewHolder.text.setText(members.get(i).get(1));
        viewHolder.text.setTextOff(members.get(i).get(1)); //Set name
        viewHolder.text.setTextOn(members.get(i).get(1)); //Set name


        viewHolder.text.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                member.add(2, Boolean.toString(isChecked));
            }
        });

    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: size: " + members.size());
        return members.size();
    }

    public ArrayList<String> getCheckedItems() {
        ArrayList<String> checkedItems = new ArrayList<>();
        for (int i = 0; i < getItemCount(); i++) {
            ArrayList<String> member = members.get(i);
            if (member.size() > 2 && member.get(2).equals("true")){
                checkedItems.add(member.get(0));
            }
        }
        return checkedItems;
    }


    /*
     *   Public exposed functionality
     */
    public void addAndNotify(ArrayList<String> added){
        members.add(members.size(), added);
        notifyItemInserted(members.size());
    }
}
