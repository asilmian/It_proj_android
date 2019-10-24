package com.example.itemorganizer.HomePage;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.TextView;

import com.example.itemorganizer.Family.SingleFamilyView;
import com.example.itemorganizer.R;
import com.example.itemorganizer.UserSingleton;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class FamilyRAdapter extends RecyclerView.Adapter<FamilyRAdapter.FamViewHolder> {

    //adapter variables
    private static final String TAG = "FamilyRAdapter";
    private ArrayList<ArrayList<String>> families;
    private Context context;

    //holder class
    public static class FamViewHolder extends RecyclerView.ViewHolder {

        //holder variables
        public TextView tx;
        public CardView cardView;
        public String token;

        public FamViewHolder(View v) {
            super(v);
            tx = v.findViewById(R.id.famName);
            cardView = v.findViewById(R.id.itemViewCardView);
        }
    }

    //constructor
    public FamilyRAdapter(ArrayList<ArrayList<String>> families, Context context) {
        this.families = families;
        this.context = context;
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
    public void onBindViewHolder(@NonNull final FamViewHolder viewHolder, int i) {

        Log.d(TAG, "onBindViewHolder: called with i: " + i);

        viewHolder.tx.setText(families.get(i).get(0)); //set name on card
        viewHolder.token = families.get(i).get(1); //set

        int activeColor = Color.parseColor("#FF3094FF");
        if (viewHolder.token.equals(UserSingleton.getInstance().getFamilyToken())) {
            //Change color
            viewHolder.cardView.setCardBackgroundColor(activeColor);
        }
        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SingleFamilyView.class);
                intent.putExtra("family_token", viewHolder.token);
                context.startActivity(intent);
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
    public void addAndNotify(ArrayList<String> added) {
        families.add(families.size(), added);
        notifyItemInserted(families.size());
    }
}
