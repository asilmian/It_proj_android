package com.example.itemorganizer;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.itemorganizer.HomePage.HomePage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GetTokenResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


public class FamilyLogIn extends AppCompatActivity {

    private EditText eToken;
    private FirebaseAuth mAuth;
    private final static String TAG = FamilyLogIn.class.toString();
    private final static String URL = "http://167.71.243.144:5000/family/join";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family_log_in);
        eToken= findViewById(R.id.familyToken);

        mAuth = FirebaseAuth.getInstance();
    }

    //go to new family page
    public void goNewFamily(View view) {
        Intent intent = new Intent(this, NewFamily.class);
        startActivity(intent);
    }

    //send family Token to verify
    public void sendToken(View view){
        mAuth.getCurrentUser().getIdToken(true)
                .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                    @Override
                    public void onComplete(@NonNull Task<GetTokenResult> task) {
                        if(task.isSuccessful()){
                            //get user token
                            String token = task.getResult().getToken();
                            Integer result = sendBackend(token);
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
                        else{
                            Log.e(TAG, task.getResult().toString());
                        }
                    }
                });
    }


    //send details to join family
    private Integer sendBackend(String token){
        BackendItem backendItem = new BackendItem(URL);

        //add required headers
        HashMap<String,String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + token);
        backendItem.setHeaders(headers);

        //make body
        makeSignUpBody(backendItem);

        BackendPost.send_req(backendItem);

        //if token is valid
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
            Log.d(TAG, backendItem.getResponse());
            return 0;
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
