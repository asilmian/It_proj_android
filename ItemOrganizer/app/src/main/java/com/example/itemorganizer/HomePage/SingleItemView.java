package com.example.itemorganizer.HomePage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.itemorganizer.BackendItem;
import com.example.itemorganizer.BackendReq;
import com.example.itemorganizer.GlideApp;
import com.example.itemorganizer.R;
import com.example.itemorganizer.UserSingleton;
import com.example.itemorganizer.UtilityFunctions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

public class SingleItemView extends AppCompatActivity {

    private ImageView image;
    private TextView name;
    private TextView desc;
    private TextView tags;

    private Button delete;
    private Button changeP;

    private final static String URL = "item/info/";
    private final static String TAG = "SingleItemView";

    private String imageRef;
    private String token;



    private ProgressBar spinner;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_item_view);

        image = findViewById(R.id.singleItemViewImage);
        name = findViewById(R.id.singleItemViewName);
        desc = findViewById(R.id.singleItemViewDescription);
        tags = findViewById(R.id.singleItemViewTags);

        delete = findViewById(R.id.singleItemViewDelete);
        changeP = findViewById(R.id.singleItemViewcp);
        spinner = findViewById(R.id.singleItemViewSpinner);

        storageReference = FirebaseStorage.getInstance().getReference();

        spinner.setVisibility(View.VISIBLE);
        Intent intent = getIntent();
        token = intent.getStringExtra("token");
        sendItemReq();
    }



    private void sendItemReq(){
        BackendItem item = new BackendItem(UserSingleton.IP + URL, BackendItem.POST);
        item.setHeaders(new HashMap<String, String>());
        createBody(item);
        try{
            new GetItemView().execute(item);
        } catch (Exception e) {
            Log.e(TAG, "sendItemReq: ",e);
        }

    }

    private void createBody(BackendItem item){
        JSONObject object = new JSONObject();
        try{
            object.accumulate("item_token", this.token);
        }catch (JSONException e){
            Log.e(TAG, "createBody: ",e);
        }
        item.setBody(object.toString());
    }

    private void onRecieve(String response){
        try{
            JSONObject object = new JSONObject(response);
            name.setText(object.getString("name"));
            desc.setText(object.getString("description"));
            tags.setText(UtilityFunctions.convertTags(object.getJSONArray("tags")));
            imageRef = object.getString("image");
        }catch (JSONException e){
            Log.e(TAG, "onRecieve: ",e);
        }

        if (imageRef != null){
            GlideApp.with(this.getApplicationContext())
                    .load(storageReference.child(imageRef))
                    .centerCrop()
                    .into(this.image);
        }
        spinner.setVisibility(View.GONE);
    }


    private class GetItemView extends AsyncTask<BackendItem, Void, BackendItem> {

        //set progress onPrexecute

        @Override
        protected BackendItem doInBackground(BackendItem... items) {
            // params comes from the execute() call: params[0] is the url.
            try {
                BackendReq.httpReq(items[0]);
            } catch (IOException e) {
                Log.e(BackendReq.class.toString(), e.toString());
                items[0].setResponse_code(777);
                items[0].setResponse("Request is invalid, never sent to server");
            }
            return items[0];
        }

        @Override
        protected void onPostExecute(BackendItem item) {
            super.onPostExecute(item);
            Log.d(TAG, item.getResponse());
            onRecieve(item.getResponse());
        }
    }
}
