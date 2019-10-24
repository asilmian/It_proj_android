package com.example.itemorganizer.Family;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.itemorganizer.BackendItem;
import com.example.itemorganizer.BackendReq;
import com.example.itemorganizer.HomePage.HomePage;
import com.example.itemorganizer.R;
import com.example.itemorganizer.UserSingleton;
import com.example.itemorganizer.UtilityFunctions;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

public class NewFamily extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText eFname;
    private final static String TAG = NewFamily.class.toString();
    private final static String URL = UserSingleton.IP + "family/create/";
    private ProgressBar spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_family);
        eFname = findViewById(R.id.familyName);
        spinner = findViewById(R.id.newFamilyProgressBar);
        spinner.setVisibility(View.GONE);
        mAuth = FirebaseAuth.getInstance();
        UtilityFunctions.clearView(eFname);
    }

    //create family
    public void createFamily(View view) {
        spinner.setVisibility(View.VISIBLE);

        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            Log.e(TAG, "createFamily: ", e);
        }
        if (sendBackend()) {
            goToHomePage();
        } else {
            Toast toast = Toast.makeText(NewFamily.this, "Connection to backend failed",
                    Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
        }
    }


    //send update to backend
    private boolean sendBackend() {
        BackendItem backendItem = new BackendItem(URL, BackendItem.POST);

        HashMap<String, String> headers = new HashMap<>();
        headers.putIfAbsent("Content-Type", "application/json");
        backendItem.setHeaders(headers);

        //make body
        makeSignUpBody(backendItem);

        try {
            backendItem = new NewFamilyTask().execute(backendItem).get();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }

        if (backendItem.getResponse_code().equals(200)) {
            return true;
        } else {
            Log.d(TAG, "new_family_response: " + backendItem.getResponse());
            return false;
        }
    }


    private void makeSignUpBody(BackendItem backendItem) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("name", eFname.getText().toString());
            backendItem.setBody(jsonObject.toString());
        } catch (JSONException e) {
            Log.e(TAG, e.toString());
        }
    }

    private class NewFamilyTask extends AsyncTask<BackendItem, Void, BackendItem> {
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
        }
    }

    private void goToHomePage() {
        UserSingleton.getInstance().setFamilyToken(null);
        Intent intent = new Intent(this, HomePage.class);
        startActivity(intent);
    }
}
