package com.example.itemorganizer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class AccountSignup extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_signup);
    }
    public void openFamily(View view){
        Intent intent = new Intent(this, FamilyLogIn.class);
        startActivity(intent);
    }

}
