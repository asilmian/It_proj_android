package com.example.itemorganizer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.security.KeyStore;

public class login extends AppCompatActivity {

    EditText username   = (EditText)findViewById(R.id.username_edit);
    EditText password = (EditText)findViewById(R.id.password_edit);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button loginBtn  = (Button) findViewById(R.id.login_btn);
        Button signupBtn = (Button) findViewById(R.id.signup_btn);




        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSignup();
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Authentication
                String userStr = username.getText().toString();
                String passStr = password.getText().toString();
                if (autheticate(userStr, passStr))
                {
                    openHomepage();
                }
            }
        });
    }
    public void openSignup(){

        Intent intent = new Intent(this, signup.class);
        startActivity(intent);
    }

    public void openHomepage(){

        Intent intent = new Intent(this, homepage.class);
        startActivity(intent);
    }

    //Returns true if valid log in
    // False if invalid.
    public boolean autheticate (String username, String password)
    {
        //Authentication with backend stuff goes here,
        // maybe add hashing of passwords etc. for extra security.

        return true;

    }
}
