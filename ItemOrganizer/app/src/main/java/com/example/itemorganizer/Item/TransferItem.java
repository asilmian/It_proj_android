package com.example.itemorganizer.Item;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.example.itemorganizer.BackendItem;
import com.example.itemorganizer.BackendReq;
import com.example.itemorganizer.BuildConfig;
import com.example.itemorganizer.HomePage.FamilyRAdapter;
import com.example.itemorganizer.R;
import com.example.itemorganizer.UserSingleton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class TransferItem extends AppCompatActivity {

    private Button transfer;
    private RecyclerView recyclerView;
    private TransferItemFamAdapter mAdapter;
    private ProgressBar spinner;

    private String item_token;

    private final static String TAG = "TransferItem";

    private final static String URL = UserSingleton.IP + "user/info/families/other/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_item);

        item_token = getIntent().getStringExtra("item_token");

        transfer = findViewById(R.id.transferItemTransfer);
        transfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        spinner = findViewById(R.id.tranferItemProgressBar);
        spinner.setVisibility(View.VISIBLE);

        //init recycler view
        recyclerView = findViewById(R.id.transferItemRecycler);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        this.mAdapter = new TransferItemFamAdapter(new ArrayList<ArrayList<String>>(), this, spinner, item_token);
        recyclerView.setAdapter(mAdapter);

        showTransferFams();
    }


    private void showTransferFams() {
        //get fams
        BackendItem get_req = new BackendItem(URL, BackendItem.GET);
        get_req.setHeaders(new HashMap<String, String>());
        Log.d(TAG, get_req.getHeaders().toString());

        try {
            new GetTransferFams().execute(get_req);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }


    //extracts fam names from response
    private ArrayList<ArrayList<String>> getFamNames(BackendItem req) {
        ArrayList<ArrayList<String>> result = new ArrayList<>();
        try {
            JSONObject raw_data = new JSONObject(req.getResponse());
            JSONArray keys = raw_data.names();

            for (int i = 0; i < keys.length(); i++) {
                String key = keys.getString(i);
                ArrayList<String> family = new ArrayList<>();
                family.add(raw_data.getJSONObject(key).getString("name")); //store name in pos0
                family.add(raw_data.getJSONObject(key).getString("token")); //store token in pos1
                result.add(family);
            }
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return result;
    }

    private class GetTransferFams extends AsyncTask<BackendItem, Void, BackendItem> {
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
