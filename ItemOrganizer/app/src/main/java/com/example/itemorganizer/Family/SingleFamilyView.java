package com.example.itemorganizer.Family;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.itemorganizer.AddItem.MemberRAdapter;
import com.example.itemorganizer.BackendItem;
import com.example.itemorganizer.BackendReq;
import com.example.itemorganizer.HomePage.HomePage;
import com.example.itemorganizer.R;
import com.example.itemorganizer.UserSingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/***
 * This activity represents a the viewing of a single family.
 * Allows user to see more information about the chosen family
 * and set it to default if they want.
 */
public class SingleFamilyView extends AppCompatActivity {

    private final static String TAG = "SingleFamilyView";

    private TextView name;
    private TextView inviteCode;
    private Button current;
    private RecyclerView members;
    private ProgressBar spinner;
    private ProgressBar secondarySpinner;
    private FamilyMemRAdapter mAdapter;

    private String family_token;
    private static final String URL = "family/info/"; // careful this not working
    private static final String MembersURL = "family/info/members";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_family_view);

        name = findViewById(R.id.single_family_view_name);
        inviteCode = findViewById(R.id.single_family_view_invite_code);

        //init recycler view
        members = findViewById(R.id.single_family_view_members);
        members.setHasFixedSize(true);
        members.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
        mAdapter = new FamilyMemRAdapter(new ArrayList<String>());
        members.setAdapter(mAdapter);

        spinner = findViewById(R.id.single_family_view_spinner);
        spinner.setVisibility(View.VISIBLE);
        secondarySpinner = findViewById(R.id.single_family_view_second_spinner);
        secondarySpinner.setVisibility(View.GONE);

        //set listener on change current family button
        current = findViewById(R.id.single_family_view_current);
        current.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeCurrentFamily();
            }
        });

        Intent intent = getIntent();
        family_token = intent.getStringExtra("family_token");
        getFamilyInformation(family_token);
    }

    //switch family.
    private void changeCurrentFamily() {
        //if token.equals(UserSingleton.getinstance().getCurrFamily(){
        // return
        //else send request to do this shit
        Intent intent = new Intent(getApplicationContext(), HomePage.class);
        startActivity(intent);
    }


    //gets family information except member information.
    private void getFamilyInformation(String family_token){
        BackendItem item = new BackendItem(UserSingleton.IP + URL, BackendItem.POST);

        item.setHeaders(new HashMap<String, String>());

        createBody(item, family_token);

        try {
            item = new ViewFamilyTask().execute(item).get();
            showInformation(item.getResponse());
        } catch (Exception e){
            Log.e(TAG, "getFamilyInformation: ",e);
        }
    }

    //creates a body with "family_token": token
    private void createBody(BackendItem item, String family_token){
        JSONObject object = new JSONObject();
        try{
            object.accumulate("family_token", family_token);
            item.setBody(object.toString());
        } catch (JSONException e){
            Log.e(TAG, "createBody: ",e);
        }
    }

    //task to get family information (not members)
    private static class ViewFamilyTask extends AsyncTask<BackendItem, Void, BackendItem> {
        @Override
        protected BackendItem doInBackground(BackendItem... items) {
            // params comes from the execute() call: params[0] is the url.
            try {
                BackendReq.httpReq(items[0]);
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            return items[0];
        }

        @Override
        protected void onPostExecute(BackendItem item) {
            super.onPostExecute(item);

        }
    }


    //shows partial family information and then send request for
    // member information
    private void showInformation(String response){
        try{
            JSONObject object = new JSONObject(response);
            name.setText(object.getString("name"));
            inviteCode.setText(object.getString("invite_code"));
        } catch (JSONException e){
            Log.e(TAG, "showInformation: ",e);
        }

        BackendItem item = new BackendItem(UserSingleton.IP + MembersURL, BackendItem.POST);
        item.setHeaders(new HashMap<String, String>());
        createBody(item, family_token);

        spinner.setVisibility(View.GONE);

        secondarySpinner.setVisibility(View.VISIBLE);

        try {
            new ViewMembersTask().execute(item);
        }catch (Exception e){
            Log.e(TAG, "showInformation: ",e);
        }

    }

    //task to send a request to get Members to
    private class ViewMembersTask extends AsyncTask<BackendItem, Void, BackendItem> {
        @Override
        protected BackendItem doInBackground(BackendItem... items) {
            // params comes from the execute() call: params[0] is the url.
            try {
                BackendReq.httpReq(items[0]);
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            return items[0];
        }

        @Override
        protected void onPostExecute(BackendItem item) {
            super.onPostExecute(item);
            showMembers(item.getResponse());
        }
    }

    //puts family member names on the recycler view
    private void showMembers(String response){
        ArrayList<String> memberNames = extractMemberNames(response);

        for (String name: memberNames){
            mAdapter.addAndNotify(name);
        }

        secondarySpinner.setVisibility(View.GONE);
    }

    //extracts the names of the family members from the response and returns
    // an array list with the names
    private ArrayList<String> extractMemberNames(String responseBody) {
        ArrayList<String> memberNames = new ArrayList<>();

        try {
            JSONObject raw_data = new JSONObject(responseBody);
            JSONArray keys = raw_data.names();

            for (int i = 0; i < keys.length(); i++) {
                String key = keys.getString(i);
                memberNames.add(raw_data.getJSONObject(key).getString("name"));
            }
        } catch (Exception e) {
            Log.e(TAG, "extractMemberNames: ",e);
        }
        return memberNames;
    }
}
