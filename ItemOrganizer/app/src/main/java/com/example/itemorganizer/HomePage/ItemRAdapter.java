package com.example.itemorganizer.HomePage;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.itemorganizer.R;


import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class ItemRAdapter extends RecyclerView.Adapter<ItemRAdapter.ItemViewHolder> {

    private static final String TAG = "ItemRAdapater";

    private ArrayList<ArrayList<String>> items;

    public static class ItemViewHolder extends RecyclerView.ViewHolder{
        public TextView name;
        public TextView desc;
        public TextView tags;
        public ImageView image;
        public CardView cardView;
        public ItemViewHolder(View v){
            super(v);
            name = v.findViewById(R.id.item_card_name);
            desc = v.findViewById(R.id.item_card_description);
            tags = v.findViewById(R.id.item_card_tags);
            image = v.findViewById(R.id.item_card_image);
            cardView = v.findViewById(R.id.card_view);
        }
    }

    public ItemRAdapter(ArrayList<ArrayList<String>>  items){
        this.items = items;
    }

    @Override
    public ItemRAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_card_view, parent, false);

        ItemViewHolder vh = new ItemViewHolder(v);
        return vh;
    }


    // called each time a new item is added to the RecyclerView container
    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder viewHolder, int i) {

        Log.d(TAG, "onBindViewHolder: called with i: " + i);

        viewHolder.name.setText(items.get(i).get(0)); //Name
        viewHolder.desc.setText(items.get(i).get(1)); //Description
        viewHolder.tags.setText(items.get(i).get(2)); //Tags
        viewHolder.image.setImageURI(items.get(i).get(3)); //Img url


        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Waddup", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: size: " + items.size());
        return items.size();
    }


    /*
     *   Public exposed functionality
     */
    public void addAndNotify(ArrayList<String> added){
        items.add(items.size(), added);
        notifyItemInserted(items.size());
    }
}
