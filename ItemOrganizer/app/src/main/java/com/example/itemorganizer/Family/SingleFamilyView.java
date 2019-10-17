package com.example.itemorganizer.Family;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
    private static final String SWITCH = "family/switch/";

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

    //gets family information except member information.
    private void getFamilyInformation(String family_token){
        BackendItem item = new BackendItem(UserSingleton.IP + URL, BackendItem.POST);

        item.setHeaders(new HashMap<String, String>());

        createBody(item, family_token);

        try {
            new ViewFamilyTask().execute(item);
        } catch (Exception e){
            Log.e(TAG, "getFamilyInformation: ",e);
            Log.e(TAG, item.getResponse_code().toString());
        }
    }

    //creates a body with "token": token
    private void createBody(BackendItem item, String family_token){
        JSONObject object = new JSONObject();
        try{
            object.accumulate("token", family_token);
            item.setBody(object.toString());
        } catch (JSONException e){
            Log.e(TAG, "createBody: ",e);
        }
    }


    private class ViewFamilyTask extends AsyncTask<BackendItem, Void, BackendItem> {
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
            showInformation(item.getResponse());
        }
    }

    //show complete family information.
    private void showInformation(String response){

        try{
            JSONObject object = new JSONObject(response);
            name.setText(object.getString("name"));
            inviteCode.setText(object.getString("token"));

            //show members
            JSONArray memjson = object.getJSONArray("members");
            for(int i=0; i <memjson.length(); i++){
                mAdapter.addAndNotify(memjson.getString(i));
            }
        } catch (JSONException e){
            Log.e(TAG, "showInformation: ",e);
        }

        spinner.setVisibility(View.GONE);
    }


///////////////////////////////////////////////////////////////////////////////////////////////////
    //switch family.
    private void changeCurrentFamily() {
        secondarySpinner.setVisibility(View.VISIBLE);

        BackendItem item = new BackendItem(UserSingleton.IP + SWITCH, BackendItem.POST);
        item.setHeaders(new HashMap<String, String>());
        createSwitchBody(item, family_token);
        Log.d(TAG, item.getBody());
        try {
            new SwitchFamilyTask().execute(item);
        } catch (Exception e){
            Log.e(TAG, "changeCurrentFamily: ",e);
            Log.e(TAG, item.getResponse_code().toString());
        }
    }

    private void createSwitchBody(BackendItem item, String family_token){
        JSONObject object = new JSONObject();
        try{
            object.accumulate("family", family_token);
            item.setBody(object.toString());
        } catch (JSONException e){
            Log.e(TAG, "createBody: ",e);
        }
    }

    //switches users current family to family looking at on screen.
    private class SwitchFamilyTask extends AsyncTask<BackendItem, Void, BackendItem> {
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
            goToHomePage(item.getResponse_code());
        }
    }

    //intent to go to homePage if response was successful.
    private void goToHomePage(int response_code){
        if(response_code != 200){
            Toast toast = Toast.makeText(SingleFamilyView.this, "Please try again",
                    Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
            secondarySpinner.setVisibility(View.INVISIBLE);
        }
        else {
            UserSingleton.getInstance().setFamilyToken(family_token);
            Intent intent = new Intent(getApplicationContext(), HomePage.class);
            startActivity(intent);
        }
    }
}
