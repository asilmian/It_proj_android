package com.example.itemorganizer.Family;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

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
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;


public class FamilyLogIn extends AppCompatActivity {

    private EditText eToken;
    private FirebaseAuth mAuth;
    private final static String TAG = FamilyLogIn.class.toString();
    private final static String URL = UserSingleton.IP + "family/join/";

    private ProgressBar spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family_log_in);
        eToken= findViewById(R.id.familyToken);
        spinner = findViewById(R.id.famLogInProg);

        mAuth = FirebaseAuth.getInstance();
    }

    //go to new family page
    public void goNewFamily(View view) {
        Intent intent = new Intent(this, NewFamily.class);
        startActivity(intent);
    }

    //send family Token to verify
    public void sendToken(View view){
        spinner.setVisibility(View.VISIBLE);
        int result = sendBackend();
        spinner.setVisibility(View.GONE);

        if(result == 1){
            goToHomePage();
        }
        else if (result == 0){
            Toast toast = Toast.makeText(FamilyLogIn.this, "Connection to backend failed",
                    Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL,0,0);
            toast.show();
        }

    }


    //send details to join family
    private int sendBackend(){
        BackendItem backendItem = new BackendItem(URL, BackendItem.POST);

        //add required headers
        HashMap<String,String> headers = new HashMap<>();
        headers.putIfAbsent("Content-Type", "application/json");
        backendItem.setHeaders(headers);

        //make body
        makeSignUpBody(backendItem);
        try{

            backendItem = new FamilyLoginTask().execute(backendItem).get();


            if (backendItem.getResponse_code().equals(200)){
                return 1;
            }

            //if token is invalid
            else if (backendItem.getResponse_code().equals((404))){
                Toast toast = Toast.makeText(FamilyLogIn.this, "Invalid Token",
                        Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL,0,0);
                toast.show();
                Log.d(TAG, "new_family_response: "+ backendItem.getResponse());
                return 2;
            }
            else {
                Log.d(TAG, backendItem.getResponse_code().toString());
                return 0;
            }
        }catch (Exception e){
            Log.e(FamilyLogIn.TAG, e.toString());
        }
        return 0;
    }

    private class FamilyLoginTask extends AsyncTask<BackendItem, Void, BackendItem> {
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


    //make join family body
    private void makeSignUpBody(BackendItem backendItem){
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("family_token", eToken.getText().toString());
            backendItem.setBody(jsonObject.toString());
        }catch (JSONException e){
            Log.e(TAG, e.toString());
        }
    }


    private void goToHomePage(){
        Intent intent = new Intent(this, HomePage.class);
        startActivity(intent);
    }
}
