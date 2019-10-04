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

import com.example.itemorganizer.Family.FamilyLogIn;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class AccountSignup extends AppCompatActivity {

    private final String TAG = "AccountSignup";

    private FirebaseAuth mAuth;

    private EditText eName;
    private EditText ePass;
    private EditText eConfPass;
    private EditText eEmail;
    private final String url = "http://167.71.243.144:5000/signup/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_signup);
        mAuth = FirebaseAuth.getInstance();

        //get buttons
        eName = findViewById(R.id.name_edit);
        eEmail = findViewById(R.id.email_login);
        ePass = findViewById(R.id.password_login);
        eConfPass = findViewById(R.id.confirm_pass);
    }

    @Override
    public void onStart(){
        super.onStart();

        //make sure not logged in
        FirebaseUser currentUser = mAuth.getCurrentUser();
        assert (currentUser == null);
    }

    public void createUser(View view) {
        String password = ePass.getText().toString();
        String conf_pass = eConfPass.getText().toString();


        //if passwords do not match
        if (!password.equals(conf_pass)) {
            Toast.makeText(AccountSignup.this, "Passwords do not match",
                    Toast.LENGTH_SHORT).show();

            UtilityFunctions.clearView(eConfPass, ePass);
            return;
        }

        String email = eEmail.getText().toString();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //User created successfully
                            Log.d(TAG, "createUserWithEmail:success");
                            //send to server
                            sendToServer();
                        } else {
                            // If sign in fails, display a message to the user.
                            // firebase has 6 letter passwords so add that in the hint
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(AccountSignup.this, task.getException().toString(),
                                    Toast.LENGTH_SHORT).show();
                        }


                    }
                });
    }


    //sends new user to backend, if backend connection fails, deletes user from firebase
    private void sendToServer(){

        //get user Id Token
        mAuth.getCurrentUser().getIdToken(true)
                .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                    public void onComplete(@NonNull Task<GetTokenResult> task) {
                        if (task.isSuccessful()) {
                            String idToken = task.getResult().getToken();
                            UtilityFunctions.setUserToken(idToken);
                            if(sendBackendSignup()){
                                openFamilyPage();
                            }
                            else{

                                //delete user from firebase
                                mAuth.getCurrentUser().delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task_inner) {
                                        if (task_inner.isSuccessful()){
                                            Toast toast = Toast.makeText(AccountSignup.this, "Connection to backend failed",
                                                    Toast.LENGTH_SHORT);
                                            toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL,0,0);
                                            toast.show();
                                        }
                                        else{
                                            Exception e = task_inner.getException();
                                            Log.e(TAG, e.toString());
                                        }
                                    }
                                });
                            }

                        } else {
                            Exception e = task.getException();
                            Log.e(TAG, e.toString());
                        }
                    }
                });
    }

    private void openFamilyPage(){

        Intent intent = new Intent(this, FamilyLogIn.class);
        startActivity(intent);
    }


    //returns true if successfully connected to backend
    private Boolean sendBackendSignup(){
        BackendItem backendItem = new BackendItem(this.url, BackendReq.POST);

        //create headers
        HashMap<String,String> headers = new HashMap<>();
        headers.putIfAbsent("Content-Type", "application/json");
        backendItem.setHeaders(headers);

        //make body
        makeSignUpBody(backendItem);

        //send request
        backendItem = BackendReq.send_req(backendItem);


        if (backendItem.getResponse_code().equals(200)){
            return true;
        }
        else{
            Log.d(TAG, "signUp response: "+ backendItem.getResponse());
            return false;
        }

    }


    //create post request body and send store in backendItem
    private void makeSignUpBody(BackendItem backendItem){
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("name", eName.getText().toString());
            jsonObject.accumulate("email", eEmail.getText().toString());
            backendItem.setBody(jsonObject.toString());
        }catch (JSONException e){
            Log.e(TAG, e.toString());
        }
    }

}
