package com.example.itemorganizer.HomePage;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.itemorganizer.BackendItem;
import com.example.itemorganizer.BackendReq;
import com.example.itemorganizer.Family.FamilyLogIn;
import com.example.itemorganizer.R;
import com.example.itemorganizer.UserSingleton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
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
    private ProgressBar spinner;

    private final static String URL = UserSingleton.IP + "user/info/families/";


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
        spinner = view.findViewById(R.id.family_frag_prog_bar);
        spinner.setVisibility(View.VISIBLE);
        initRecyclerView(view);
        showUserFams();
        return view;
    }

    private void initRecyclerView(View view) {
        recyclerView = view.findViewById(R.id.privacy_recycler);

        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        this.mAdapter = new FamilyRAdapter(new ArrayList<ArrayList<String>>(), getContext());
        recyclerView.setAdapter(mAdapter);
    }

    private void showUserFams() {
        //get fams
        BackendItem get_req = new BackendItem(URL, BackendItem.GET);
        get_req.setHeaders(new HashMap<String, String>());
        Log.d(TAG, get_req.getHeaders().toString());

        try {
            new GetFamilyMembers().execute(get_req);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }


    private ArrayList<ArrayList<String>> getFamNames(BackendItem req) {
        ArrayList<ArrayList<String>> result = new ArrayList<>();
        try {
            JSONObject raw_data = new JSONObject(req.getResponse());
            JSONArray keys = raw_data.names();

            for (int i = 0; i < keys.length(); i++) {
                String key = keys.getString(i);
                ArrayList<String> family = new ArrayList<>();
                family.add(raw_data.getJSONObject(key).getString("name")); //store name in pos1
                family.add(raw_data.getJSONObject(key).getString("token")); //store token in pos1
                result.add(family);
            }
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return result;
    }

    private class GetFamilyMembers extends AsyncTask<BackendItem, Void, BackendItem> {
        @Override
        protected BackendItem doInBackground(BackendItem... items) {
            // params comes from the execute() call: params[0] is the url.
            try {
                BackendReq.httpReq(items[0]);
            } catch (IOException e) {
                Log.e(BackendReq.class.toString(), e.toString());
                items[0].setResponse_code(777);
                items[0].setResponse("Connection failed to backend or request is invalid");
            }
            return items[0];
        }

        @Override
        protected void onPostExecute(BackendItem item) {
            super.onPostExecute(item);
            ArrayList<ArrayList<String>> famNames = getFamNames(item);
            for (ArrayList<String> family : famNames) {
                mAdapter.addAndNotify(family);
            }
            spinner.setVisibility(View.GONE);
        }
    }
}