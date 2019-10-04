package com.example.itemorganizer;


import android.widget.EditText;
import android.widget.TextView;


//class to contain static usable function
public class UtilityFunctions {

    public static void clearView(EditText... args){
        for (EditText button: args){
            button.setText("", TextView.BufferType.EDITABLE);
        }
    }


    public static void setUserToken(String token){
        UserSingleton user = UserSingleton.getInstance();
        user.setUserToken(token);
    }
}