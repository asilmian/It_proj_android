package com.example.itemorganizer.Family;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.itemorganizer.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SingleFamilyMemRAdapter extends RecyclerView.Adapter<SingleFamilyMemRAdapter.SFMHolder> {

    private static final String TAG = "SingleFamilyMemRAdapter";

    private ArrayList<String> names;

    //holder class
    public class SFMHolder extends RecyclerView.ViewHolder{
        public TextView name;
        public ImageView avatar;

        public SFMHolder(View v){
            super(v);
            name = v.findViewById(R.id.famMemberSingleName);
            avatar = v.findViewById(R.id.famMemberSinglePic);
        }
    }

    public SingleFamilyMemRAdapter(ArrayList<String> members) {
        this.names = members;
    }

    @NonNull
    @Override
    public SingleFamilyMemRAdapter.SFMHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fam_member_single_card_view, parent, false);

        SFMHolder vh = new SFMHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull SFMHolder holder, int position) {
        final String name = names.get(position);

        Log.d(TAG, "onBindViewHolder: called with i: " + position);
        holder.name.setText(name);
    }

    @Override
    public int getItemCount() {
        return names.size();
    }

    public void addAndNotify(String added) {
        names.add(names.size(), added);
        notifyItemInserted(names.size());
    }


}
