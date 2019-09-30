package com.example.itemorganizer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GetTokenResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class NewFamily extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText eFname;
    private final static String TAG = NewFamily.class.toString();
    private final static String URL = "http://167.71.243.144:5000/family/create" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_family);
        eFname = findViewById(R.id.familyName);

        mAuth = FirebaseAuth.getInstance();
    }

    //create family
    public void createFamily(View view){
        mAuth.getCurrentUser().getIdToken(true)
                .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
            @Override
            public void onComplete(@NonNull Task<GetTokenResult> task) {
                if(task.isSuccessful()){
                    String token = task.getResult().getToken();
                    if(sendBackend(token)){
                        goToHomePage();
                    }
                    else{
                        Toast toast = Toast.makeText(NewFamily.this, "Connection to backend failed",
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

        //send details to create new family
    }


    private boolean sendBackend(String token){
        BackendItem backendItem = new BackendItem(URL);

        HashMap<String,String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + token);
        backendItem.setHeaders(headers);

        //make body
        makeSignUpBody(backendItem);

        BackendPost.send_req(backendItem);

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
