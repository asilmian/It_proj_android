package com.example.itemorganizer.HomePage;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.itemorganizer.GlideApp;
import com.example.itemorganizer.R;
import com.example.itemorganizer.UserSingleton;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class ItemRAdapter extends RecyclerView.Adapter<ItemRAdapter.ItemViewHolder> {

    private StorageReference storageReference;
    private static final String TAG = "ItemRAdapater";
    private ArrayList<ArrayList<String>> items;
    private Context context;

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView tags;
        public ImageView image;
        public CardView cardView;
        public String itemToken;

        public ItemViewHolder(View v) {
            super(v);
            name = v.findViewById(R.id.itemViewName);
            tags = v.findViewById(R.id.itemViewTags);
            image = v.findViewById(R.id.itemViewImage);
            cardView = v.findViewById(R.id.itemViewCardView);
        }
    }

    public ItemRAdapter(ArrayList<ArrayList<String>> items, Context context) {
        this.items = items;
        this.context = context;
        storageReference = FirebaseStorage.getInstance().getReference();
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
    public void onBindViewHolder(@NonNull final ItemViewHolder viewHolder, int i) {

        Log.d(TAG, "onBindViewHolder: called with i: " + i);

        viewHolder.name.setText(items.get(i).get(0)); //Name
        viewHolder.tags.setText(items.get(i).get(2)); //Tags

        //use glideapp to display image from fire storage
        GlideApp.with(viewHolder.image)
                .load(storageReference.child(items.get(i).get(3))) //imageReference
                .centerCrop()
                .into(viewHolder.image);

        viewHolder.itemToken = items.get(i).get(4);// token

        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewItem(viewHolder.itemToken);
            }
        });

    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: size: " + items.size());
        return items.size();
    }

    public void viewItem(String itemToken){
        Intent intent = new Intent(this.context, SingleItemView.class);
        intent.putExtra("token", itemToken);
        this.context.startActivity(intent);
    }

    /*
     *   Public exposed functionality
     */
    public void addAndNotify(ArrayList<String> added) {
        items.add(items.size(), added);
        notifyItemInserted(items.size());
    }
}
