package com.example.itemorganizer;

import android.content.Intent;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                goAccountLogin(findViewById(android.R.id.content));
            }
        }, 1000);   //1 second
    }

    public void goAccountLogin(View view) {
        Intent intent = new Intent(this, AccountLogin.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

    }
}
