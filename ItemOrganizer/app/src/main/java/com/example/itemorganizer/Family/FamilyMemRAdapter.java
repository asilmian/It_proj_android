package com.example.itemorganizer.Family;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.itemorganizer.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FamilyMemRAdapter  extends RecyclerView.Adapter<FamilyMemRAdapter.FamilyMemHolder> {
    private static final String TAG = "MemberRAdapter";

    private ArrayList<String> members;

    public class FamilyMemHolder extends RecyclerView.ViewHolder{
        public TextView name;
        public FamilyMemHolder(View v) {
            super(v);
            name = v.findViewById(R.id.memberName);
        }
    }

    public FamilyMemRAdapter(ArrayList<String> members) {
        this.members = members;
    }

    @NonNull
    @Override
    public FamilyMemRAdapter.FamilyMemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.member_card_view, parent, false);

        FamilyMemHolder vh = new FamilyMemHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull FamilyMemHolder holder, int position) {
        final String member = members.get(position);

        Log.d(TAG, "onBindViewHolder: called with i: " + position);

        holder.name.setText(member);
    }

    @Override
    public int getItemCount() {
        return members.size();
    }

    public void addAndNotify(String added) {
        members.add(members.size(), added);
        notifyItemInserted(members.size());
    }
}
