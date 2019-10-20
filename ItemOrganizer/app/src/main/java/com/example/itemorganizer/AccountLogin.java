package com.example.itemorganizer;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.itemorganizer.HomePage.HomePage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;

public class AccountLogin extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private static final String TAG = "AccountLogin";

    private EditText ePass;
    private EditText eEmail;
    private ProgressBar spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_login);

        ePass = findViewById(R.id.password_login);
        eEmail = findViewById(R.id.email_login);
        spinner = findViewById(R.id.login_progressBar);
        spinner.setVisibility(View.INVISIBLE);

        //initialize firebase
        mAuth = FirebaseAuth.getInstance();
    }

    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            spinner.setVisibility(View.VISIBLE);
            setUserToken();
        }

    }

    public void openSignup(View view) {

        Intent intent = new Intent(this, AccountSignup.class);
        startActivity(intent);
    }


    public void openHomepage(View view) {

        //get password and email from screen
        String password = ePass.getText().toString();
        String email = eEmail.getText().toString();
        //check if fields are empty
        if (password.equals("") || email.equals("")){

            Toast toast = Toast.makeText(AccountLogin.this, "Please complete all fields",
                    Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
            return;
        }

        spinner.setVisibility(View.VISIBLE);
        //create task to sign into firebase Auth
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success
                            Log.d(TAG, "signInWithEmail:success");
                            setUserToken();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());

                            //issue: make toast appear on top
                            Toast.makeText(AccountLogin.this, "Login Failed",
                                    Toast.LENGTH_SHORT).show();
                            spinner.setVisibility(View.INVISIBLE);
                        }
                    }
                });

    }

    //go to main page
    private void goToHomePage() {
        Intent intent = new Intent(this, HomePage.class);
        startActivity(intent);
    }

    //initializes User singleton and sets/refreshes the user token.
    private void setUserToken() {
        mAuth.getCurrentUser().getIdToken(true)
                .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                    public void onComplete(@NonNull Task<GetTokenResult> task) {
                        if (task.isSuccessful()) {
                            String idToken = task.getResult().getToken();
                            UtilityFunctions.setUserToken(idToken);
                            goToHomePage();
                        } else {
                            spinner.setVisibility(View.INVISIBLE);
                            Toast toast = Toast.makeText(AccountLogin.this, "Connection to firebase failed",
                                    Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
                            toast.show();
                        }
                    }
                });
    }
}
