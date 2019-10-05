package com.example.itemorganizer.Family;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.itemorganizer.BackendItem;
import com.example.itemorganizer.BackendReq;
import com.example.itemorganizer.HomePage.HomePage;
import com.example.itemorganizer.R;
import com.example.itemorganizer.UserSingleton;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class NewFamily extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText eFname;
    private final static String TAG = NewFamily.class.toString();
    private final static String URL = UserSingleton.IP +  "family/create/" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_family);
        eFname = findViewById(R.id.familyName);

        mAuth = FirebaseAuth.getInstance();
    }

    //create family
    public void createFamily(View view){
        if(sendBackend()){
            goToHomePage();
        }
        else{
            Toast toast = Toast.makeText(NewFamily.this, "Connection to backend failed",
                    Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL,0,0);
            toast.show();
        }

        //send details to create new family
    }


    private boolean sendBackend(){
        BackendItem backendItem = new BackendItem(URL, BackendReq.POST);

        HashMap<String,String> headers = new HashMap<>();
        headers.putIfAbsent("Content-Type", "application/json");
        backendItem.setHeaders(headers);

        //make body
        makeSignUpBody(backendItem);

        BackendReq.send_req(backendItem);

        if (backendItem.getResponse_code().equals(200)){
            return true;
        }
        else{
            Log.d(TAG, "new_family_response: "+ backendItem.getResponse());
            return false;
        }
    }


    private void makeSignUpBody(BackendItem backendItem){
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("name", eFname.getText().toString());
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
