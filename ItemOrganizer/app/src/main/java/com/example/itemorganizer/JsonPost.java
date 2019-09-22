package com.example.itemorganizer;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class JsonPost {

    private String myUrl;
    private JSONObject jsonObject;

    public JsonPost(String myUrl, JSONObject jsonObject){
        this.myUrl = myUrl;
        this.jsonObject = jsonObject;
    }

    public String execute(){
        try {
            return new HTTPAsyncTask().execute(this.myUrl).get();
        }catch (Exception e){
            Log.d("JsonPost", e.toString());
        }
        return "failed";
    }

    private String httpPost() throws IOException, JSONException {
        URL url = new URL(this.myUrl);

        //create HttpURLConnection
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("User-Agent", "Android App");


        //add content to the body
        setPostRequestContent(conn, this.jsonObject);
        Log.d("json", this.jsonObject.toString());
        //make POST request to the given URL
        conn.connect();
        //return response message
        return conn.getResponseMessage()+"";

    }


    private void setPostRequestContent(HttpURLConnection conn, JSONObject jsonObject) throws IOException {

        OutputStream os = conn.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
        writer.write(jsonObject.toString());
        Log.i(MainActivity.class.toString(), jsonObject.toString());
        writer.flush();
        writer.close();
        os.close();
    }


    //fix memory leak
    private class HTTPAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            // params comes from the execute() call: params[0] is the url.
            try {
                try {
                    return httpPost();
                } catch (JSONException e) {
                    e.printStackTrace();
                    return "Error!";
                }
            } catch (IOException e) {
                return e.toString();
            }
        }
    }

}
