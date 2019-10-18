package com.example.itemorganizer.HomePage;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.itemorganizer.AccountLogin;
import com.example.itemorganizer.AddItem.AddItemActivity;
import com.example.itemorganizer.BackendItem;
import com.example.itemorganizer.BackendReq;
import com.example.itemorganizer.R;
import com.example.itemorganizer.UserSingleton;
import com.example.itemorganizer.UtilityFunctions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class ItemFragment extends Fragment {


    private final static String TAG = FamilyFragment.class.toString();

    private RecyclerView recyclerView;
    private ItemRAdapter mAdapter;
    private EditText eSearchT;
    private ProgressBar spinner;

    private final static String ITEM_URL = "item/list/";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item, container, false);
        if (UserSingleton.getInstance().getUserToken() == null) {
            Intent intent = new Intent(view.getContext(), AccountLogin.class);
            startActivity(intent);
        }

        eSearchT = view.findViewById(R.id.searchText);
        eSearchT.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mAdapter.getFilter().filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        spinner = view.findViewById(R.id.item_frag_loading_bar);
        spinner.setVisibility(View.VISIBLE);

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

    private void initRecyclerView(View view) {
        recyclerView = view.findViewById(R.id.item_recycler);

        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        this.mAdapter = new ItemRAdapter(new ArrayList<ArrayList<String>>(), this.getContext());
        recyclerView.setAdapter(mAdapter);
    }

    private void showItems() {
        spinner.setVisibility(View.VISIBLE);
        BackendItem get_req = new BackendItem(UserSingleton.IP + ITEM_URL, BackendItem.GET);
        get_req.setHeaders(new HashMap<String, String>());
        Log.d(TAG, get_req.getHeaders().toString());


        try {
            new GetItemInfoTask().execute(get_req);
        } catch (Exception e) {
            Log.e(TAG, "showItems: ", e);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    private class GetItemInfoTask extends AsyncTask<BackendItem, Void, BackendItem> {
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
            ArrayList<ArrayList<String>> viewItems = getItemData(item.getResponse());
            //put items on display
            for (ArrayList<String> viewItem : viewItems) {
                mAdapter.addAndNotify(viewItem);
            }

            spinner.setVisibility(View.GONE);
        }
    }

    //uses response to get item data
    private ArrayList<ArrayList<String>> getItemData(String data) {
        //Array of Data
        ArrayList<ArrayList<String>> result = new ArrayList<>();

        try {
            JSONObject raw_data = new JSONObject(data);
            JSONArray keys = raw_data.names();

            for (int i = 0; i < keys.length(); i++) {
                String key = keys.getString(i);
                ArrayList<String> tempData = new ArrayList<>();

                tempData.add(raw_data.getJSONObject(key).getString("name")); // First element
                tempData.add(raw_data.getJSONObject(key).getString("description")); // Second Element
                tempData.add(UtilityFunctions.convertTags(raw_data.getJSONObject(key).getJSONArray("tags"))); // Third Element
                tempData.add(raw_data.getJSONObject(key).getString("image"));// Fourth Element
                tempData.add(key);

                result.add(tempData);
            }
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return result;
    }


}