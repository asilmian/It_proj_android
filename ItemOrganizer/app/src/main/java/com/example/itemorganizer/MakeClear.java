package com.example.itemorganizer;


import android.widget.EditText;
import android.widget.TextView;

public class MakeClear{

    public static void clearView(EditText... args){
        for (EditText button: args){
            button.setText("", TextView.BufferType.EDITABLE);
        }
    }
}
