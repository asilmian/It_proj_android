package com.example.itemorganizer;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class NewFamily extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_family);
    }

    //create family
    public void createFamily(View view){
        EditText editText = findViewById(R.id.familyName);
        String message = editText.getText().toString();
        //clears text box
        editText.setText(" ", TextView.BufferType.EDITABLE);
        //send details to create new family
    }
}
