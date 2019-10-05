package com.example.itemorganizer.HomePage;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

<<<<<<< HEAD
import com.example.itemorganizer.AddItem.AddItemActivity;
=======
import com.example.itemorganizer.BackendItem;
import com.example.itemorganizer.BackendReq;
import com.example.itemorganizer.CameraActivity;
>>>>>>> master
import com.example.itemorganizer.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ItemFragment extends Fragment {


    private final static String TAG = FamilyFragment.class.toString();

    private RecyclerView recyclerView;
    private ItemRAdapter mAdapter;

    private final static String URL = "http://167.71.243.144:5000/user/info/families";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item, container, false);


        FloatingActionButton fab = view.findViewById(R.id.add_new_item);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), AddItemActivity.class));
            }
        });

        initRecyclerView(view);
        showItems();
        return view;
    }

    private void initRecyclerView(View view){
        recyclerView = view.findViewById(R.id.item_recycler);

        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        this.mAdapter = new ItemRAdapter(new ArrayList<ArrayList<String>>());
        recyclerView.setAdapter(mAdapter);
    }

    private void showItems(){
        //get fams
        BackendItem get_req = new BackendItem(URL, BackendReq.GET);
        get_req.setHeaders(new HashMap<String, String>());
        Log.d(TAG,get_req.getHeaders().toString());
        BackendReq.send_req(get_req);
        //Items = []
        ArrayList<ArrayList<String>> items = getItemData(get_req);


        //put items on display
        for (ArrayList<String> item : items){
            this.mAdapter.addAndNotify(item);
        }
    }


    private ArrayList<ArrayList<String>> getItemData(BackendItem req){
        //Array of Data
        ArrayList<ArrayList<String>> result = new ArrayList<>();

        try{
            JSONObject raw_data = new JSONObject(req.getResponse());
            JSONArray keys  = raw_data.names();

            for (int i=0; i<keys.length(); i++){
                String key = keys.getString(i);
                ArrayList<String> tempData = new ArrayList<>();

                tempData.add(raw_data.getJSONObject(key).getString("name")); // First element
                tempData.add(raw_data.getJSONObject(key).getString("desc")); // Second Element
                tempData.add(raw_data.getJSONObject(key).getString("tags")); // Third Element
                tempData.add(raw_data.getJSONObject(key).getString("image")); // Fourth Element

                result.add(tempData);
            }
        } catch (Exception e){
            Log.e(TAG, e.toString());
        }
        return result;
    }
}