package com.example.itemorganizer.HomePage;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.itemorganizer.BackendItem;
import com.example.itemorganizer.BackendReq;
import com.example.itemorganizer.Family.FamilyLogIn;
import com.example.itemorganizer.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class FamilyFragment extends Fragment {

    private final static String TAG = FamilyFragment.class.toString();

    private RecyclerView recyclerView;
    private FamilyRAdapter mAdapter;

    private final static String URL = "http://167.71.243.144:5000/user/info/families";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_family, container, false);


        FloatingActionButton fab = view.findViewById(R.id.join_new_family);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), FamilyLogIn.class));
            }
        });

        initRecyclerView(view);
        showUserFams();
        return view;
    }

    private void initRecyclerView(View view){
        recyclerView = view.findViewById(R.id.family_recycler);

        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        this.mAdapter = new FamilyRAdapter(new ArrayList<String>());
        recyclerView.setAdapter(mAdapter);
    }

    private void showUserFams(){
        //get fams
        BackendItem get_req = new BackendItem(URL, BackendReq.GET);
        get_req.setHeaders(new HashMap<String, String>());
        Log.d(TAG,get_req.getHeaders().toString());
        BackendReq.send_req(get_req);

        ArrayList<String> names = getFamNames(get_req);


        //put fams on display
        for (String name : names){
            this.mAdapter.addAndNotify(name);
        }
    }


    private ArrayList<String> getFamNames(BackendItem req){
        ArrayList<String> result = new ArrayList<>();
        try{
            JSONObject raw_data = new JSONObject(req.getResponse());
            JSONArray keys  = raw_data.names();

            for (int i=0; i<keys.length(); i++){
                String key = keys.getString(i);
                result.add(raw_data.getJSONObject(key).getString("name"));
            }
        } catch (Exception e){
            Log.e(TAG, e.toString());
        }
        return result;
    }
}