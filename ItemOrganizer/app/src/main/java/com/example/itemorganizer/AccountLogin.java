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

public class AccountLogin extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private static final String TAG = "AccountLogin";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_login);
        mAuth = FirebaseAuth.getInstance();
    }

    public void onStart(){
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null){
            goToHomePage();
        }
    }

    public void openSignup(View view){

        Intent intent = new Intent(this, AccountSignup.class);
        startActivity(intent);
    }


    public void openHomepage(View view){

        //get password and email from screen
        EditText editText = findViewById(R.id.password_edit);
        String password = editText.getText().toString();

        editText = findViewById(R.id.email_edit);
        String email = editText.getText().toString();

        //check if fields are empty

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            goToHomePage();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(AccountLogin.this, "Login Failed",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
    private void goToHomePage(){
        //goes to MainActivity instead right now.
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
