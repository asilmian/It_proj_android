package com.example.itemorganizer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button EnterBtn = (Button) findViewById(R.id.enter_btn);
        EnterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openLogin();
            }
        });
    }
    public void openLogin(){

        Intent intent = new Intent(this, login.class);
        startActivity(intent);
    }
}
