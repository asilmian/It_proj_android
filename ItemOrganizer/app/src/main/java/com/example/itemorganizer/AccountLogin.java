package com.example.itemorganizer;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AccountLogin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_login);
    }
    public void openSignup(View view){

        Intent intent = new Intent(this, AccountSignup.class);
        startActivity(intent);
    }

    public void openHomepage(View view){

        if (autheticate("name", "password"))
        {
            //change familyLogIn to ItemHomepage
            Intent intent = new Intent(this, FamilyLogIn.class);
            startActivity(intent);
        }

    }
    public boolean autheticate (String username, String password)
    {

        return true;

    }
}
