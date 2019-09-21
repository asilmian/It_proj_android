package com.example.itemorganizer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class FamilyLogIn extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family_log_in);
    }

    //go to new family page
    public void goNewFamily(View view) {
        Intent intent = new Intent(this, NewFamily.class);
        startActivity(intent);
    }

    //send family Token to verify
    public void sendToken(View view){
        EditText editText = findViewById(R.id.familyToken);
        String message = editText.getText().toString();
        //clear text box
        editText.setText(" ", TextView.BufferType.EDITABLE);
        //send token
    }
}
