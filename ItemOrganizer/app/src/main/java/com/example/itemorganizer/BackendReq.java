package com.example.itemorganizer;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Map;

/***
 * Class to make Backend item post to url.
 */
public class BackendReq {


    private static final String TAG = "BackendReq";


    public static BackendItem send_req(BackendItem item) {
        try {
            return new HTTPAsyncTask().execute(item).get();
        } catch (Exception e) {
            Log.e(TAG, "send_req: ", e);
            return item;
        }
    }

    public static void httpReq(BackendItem item) throws IOException {
        URL url = new URL(item.getUrl());

        //create HttpURLConnection
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();


        try {
            conn.setRequestMethod(item.getMethod());
        } catch (ProtocolException e) {
            Log.e(TAG, "httpReq: ", e);
        }

        conn.setConnectTimeout(10000); //set timeout to 10 secs

        //set the headers
        for (Map.Entry<String, String> entry : item.getHeaders().entrySet()) {
            conn.setRequestProperty(entry.getKey(), entry.getValue());
        }


        //add content to the body
        if (item.getBody() != null) {
            try {
                setPostRequestContent(conn, item.getBody());
            } catch (Exception e) {
                Log.e(TAG, "unable to write to body   " + e.toString());
            }
            Log.d("post body:", item.getBody());
            Log.d("conn", conn.getOutputStream().toString());
        }


        //make POST request to the given URL
        conn.connect();

        item.setResponse_code(conn.getResponseCode());
        if (item.getResponse_code() >= 400) {
            item.setResponse("No response");
        } else {
            item.setResponse(readInputStream(conn.getInputStream()));
        }
        conn.disconnect();

    }


    //throws protcol exception sometimes, debug with johno
    private static String readInputStream(InputStream response) {
        String result = "";
        String tmp;
        BufferedReader br = new BufferedReader(new InputStreamReader(response));
        try {
            while ((tmp = br.readLine()) != null) {
                result += tmp + "\n";
            }
            br.close();
            return result;
        } catch (Exception e) {
            Log.e(TAG, "readInputStream: ", e);
        }
        return result;
    }

    private static void setPostRequestContent(HttpURLConnection conn, String body) throws IOException {

        OutputStream os = conn.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
        writer.write(body);
        Log.i(BackendReq.class.toString(), body);
        writer.flush();
        writer.close();
        os.close();
    }


    //Only use when want UI to hang, or wait for request.
    private static class HTTPAsyncTask extends AsyncTask<BackendItem, Void, BackendItem> {
        @Override
        protected BackendItem doInBackground(BackendItem... items) {
            // params comes from the execute() call: params[0] is the url.
            try {
                httpReq(items[0]);
            } catch (IOException e) {
                Log.e(BackendReq.class.toString(), e.toString());
                items[0].setResponse_code(777);
                items[0].setResponse("Connection failed to backend or request is invalid");
            }
            return items[0];
        }
    }

}
