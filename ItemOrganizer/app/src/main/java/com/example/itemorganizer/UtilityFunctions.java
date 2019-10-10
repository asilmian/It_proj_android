package com.example.itemorganizer;


import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;

import java.util.ArrayList;


//class to contain static usable function
public class UtilityFunctions {

    private static String TAG = UtilityFunctions.class.toString();

    public static void clearView(EditText... args) {
        for (EditText button : args) {
            button.setText("", TextView.BufferType.EDITABLE);
        }
    }


    public static void setUserToken(String token) {
        UserSingleton user = UserSingleton.getInstance();
        user.setUserToken(token);
    }

    public static JSONArray convert(String[] array) {
        JSONArray output = new JSONArray();
        for (String str : array) {
            output.put(str);
        }
        return output;
    }

    public static JSONArray convert(ArrayList<String> array) {
        JSONArray output = new JSONArray();
        for (String str : array) {
            output.put(str);
        }
        return output;
    }

    public static String convertTags(JSONArray jsonArray) {
        String result = "";
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                result += jsonArray.getString(i) + ",";
            }
        } catch (Exception e) {
            Log.e(TAG, "convertTags: ", e);
        }

        return result.substring(0, result.length() - 1);
    }


}
