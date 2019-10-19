package com.example.itemorganizer.Item;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.itemorganizer.BackendItem;
import com.example.itemorganizer.BackendReq;
import com.example.itemorganizer.HomePage.HomePage;
import com.example.itemorganizer.R;
import com.example.itemorganizer.UserSingleton;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class TransferItemFamAdapter extends RecyclerView.Adapter<TransferItemFamAdapter.TransViewHolder> {

    private static final String TAG = "FamilyRAdapter";
    private ArrayList<ArrayList<String>> families;
    private Context context;
    private ProgressBar spinner;
    private String item_token;

    private final static String URL = UserSingleton.IP + "item/transfer/";

    //holder class
    public static class TransViewHolder extends RecyclerView.ViewHolder {

        //holder variables
        public TextView tx;
        public CardView cardView;
        public String token;

        public TransViewHolder(View v) {
            super(v);
            tx = v.findViewById(R.id.famName);
            cardView = v.findViewById(R.id.itemViewCardView);
        }
    }

    //constructor
    public TransferItemFamAdapter(ArrayList<ArrayList<String>> families, Context context, ProgressBar spinner, String item_token) {
        this.families = families;
        this.context = context;
        this.spinner = spinner;
        this.item_token = item_token;
    }

    @Override
    public TransferItemFamAdapter.TransViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fam_card_view, parent, false);

        TransViewHolder vh = new TransViewHolder(v);
        return vh;
    }


    // called each time a new item is added to the RecyclerView container
    @Override
    public void onBindViewHolder(@NonNull final TransViewHolder viewHolder, int i) {

        Log.d(TAG, "onBindViewHolder: called with i: " + i);

        viewHolder.tx.setText(families.get(i).get(0)); //set name on card
        viewHolder.token = families.get(i).get(1); //set


        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transferItem(viewHolder.token);
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


    private void transferItem(String family_token){
        spinner.setVisibility(View.VISIBLE);

        BackendItem item = new BackendItem(URL, BackendItem.POST);
        item.setHeaders(new HashMap<String, String>());

        createBody(item, family_token);

        BackendReq.send_req(item);

        if (item.getResponse_code() == 200){
            goToHomepage();
        }
        else{
            spinner.setVisibility(View.GONE);
        }
    }

    private void createBody(BackendItem item, String family_token){
        try{
            JSONObject object = new JSONObject();
            object.accumulate("item_token", this.item_token);
            object.accumulate("family_token", family_token);

            item.setBody(object.toString());
        } catch (Exception e){
            Log.e(TAG, "createBody: ",e);
        }
    }

    private void goToHomepage(){
        Intent intent = new Intent(context, HomePage.class);
        context.startActivity(intent);
    }
}
