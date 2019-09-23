package com.example.itemorganizer;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/***
 * Class to make Backend item post to url.
 */
public class JsonPost {


    public static final Integer FAILED = 777;


    public  static BackendItem send_req(BackendItem item){
        try {
            return new HTTPAsyncTask().execute(item).get();
        }catch (Exception e){
            Log.d("JsonPost", e.toString());
            item.setResponse_code(FAILED);
            return item;
        }
    }

    private static BackendItem httpPost(BackendItem item) throws IOException {
        URL url = new URL(item.getUrl());

        //create HttpURLConnection
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");

        conn.setConnectTimeout(10000); //set timeout to 10 secs

        //set the headers
        for (Map.Entry<String, String> entry : item.getHeaders().entrySet()){
            conn.setRequestProperty(entry.getKey(), entry.getValue());
        }


        //add content to the body
        if (item.getBody() != null) {
            setPostRequestContent(conn, item.getBody());
            Log.d("post body:", item.getBody());
        }


        //make POST request to the given URL
        conn.connect();

        item.setResponse_code(conn.getResponseCode());
        item.setResponse(conn.getResponseMessage());

        return item;

    }


    private static void setPostRequestContent(HttpURLConnection conn, String body) throws IOException {

        OutputStream os = conn.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
        writer.write(body);
        Log.i(JsonPost.class.toString(), body);
        writer.flush();
        writer.close();
        os.close();
    }


    //fix memory leak
    private static class HTTPAsyncTask extends AsyncTask<BackendItem, Void, BackendItem> {
        @Override
        protected BackendItem doInBackground(BackendItem... items) {
            // params comes from the execute() call: params[0] is the url.
            try {
                return httpPost(items[0]);
            } catch (IOException e) {
                Log.e(JsonPost.class.toString(), e.toString());
                items[0].setResponse_code(FAILED);
                items[0].setResponse("Connection Failed");
                return items[0];
            }
        }
    }

}
