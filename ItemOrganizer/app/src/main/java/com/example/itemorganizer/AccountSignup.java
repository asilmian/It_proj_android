package com.example.itemorganizer;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;

import org.json.JSONException;
import org.json.JSONObject;

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
        eEmail = findViewById(R.id.email_edit);
        ePass = findViewById(R.id.password_edit);
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

            MakeClear.clearView(eConfPass, ePass);
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
                            Toast.makeText(AccountSignup.this, "Connection Failed",
                                    Toast.LENGTH_SHORT).show();
                        }


                    }
                });
    }


    //issues: goes to family page even with failed post to server
    //issues: also delete user in firebase if connection fails.
    private void sendToServer(){

        //get user Id Token
        mAuth.getCurrentUser().getIdToken(true)
                .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                    public void onComplete(@NonNull Task<GetTokenResult> task) {
                        if (task.isSuccessful()) {
                            String idToken = task.getResult().getToken();

                            //if token successfully send to backend
                            if(sendJsonPost(idToken)){
                                openFamilyPage();
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


    private Boolean sendJsonPost(String idToken){
        try {
            JSONObject data = buildJsonObject(idToken);
            JsonPost post = new JsonPost(this.url, data);
            String response = post.execute();
            Log.d(TAG, "signUp response: "+ response);
            if (response.equals("Unable to retrieve web page. URL may be invalid.")){
                return false;
            }
            return true;
        }catch (JSONException e){
            Log.e(TAG, e.toString());
        }
        return false;
    }

    private JSONObject buildJsonObject(String idToken) throws JSONException {

        JSONObject jsonObject = new JSONObject();
        jsonObject.accumulate("name", eName.getText().toString());
        jsonObject.accumulate("token", idToken);
        jsonObject.accumulate("email",  eEmail.getText().toString());
        return jsonObject;
    }
}
