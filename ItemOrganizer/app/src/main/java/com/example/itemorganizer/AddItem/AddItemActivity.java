package com.example.itemorganizer.AddItem;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.itemorganizer.BackendItem;
import com.example.itemorganizer.BackendReq;
import com.example.itemorganizer.BuildConfig;
import com.example.itemorganizer.HomePage.HomePage;

import com.example.itemorganizer.R;
import com.example.itemorganizer.UserSingleton;
import com.example.itemorganizer.UtilityFunctions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import static android.os.Environment.getExternalStoragePublicDirectory;

public class AddItemActivity extends AppCompatActivity {

    ImageView imageView;
    String pathToFile;
    Button pictureBtn;
    Button addbtn;
    EditText item_name;
    EditText item_desc;
    EditText item_tags;
    File image;
    FirebaseStorage fire_storage;
    StorageReference storageRef;
    String ref;

    private RecyclerView recyclerView;
    private MemberRAdapter mAdapter;
    ProgressBar spinner;
    ProgressBar secondarySpinner;

    private Boolean uploadedPic;

    private final static String TAG = AddItemActivity.class.toString();
    private HashMap<String, String> id_names;

    private static final String ADD_URL = "item/add/";
    private static final String GET_IMAGE_REF = "item/add/ref/";
    private final static String MEMBERS_URL = "family/info/members/";
    private final static String DETECTION = "detection/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_item);
        imageView = findViewById(R.id.image);
        requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);

        pictureBtn = findViewById(R.id.PictureBtn);
        addbtn = findViewById(R.id.submit_item_button);

        item_name = findViewById(R.id.add_item_name);
        item_desc = findViewById(R.id.add_item_desc);
        item_tags = findViewById(R.id.add_item_tags);
        spinner = findViewById(R.id.addItemProgBar);
        secondarySpinner = findViewById(R.id.addItemSecondarBar);
        uploadedPic = false;


        pictureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePhoto();
            }
        });
        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitItemCheck(view);
            }
        });
        //initializes recycler view
        initRecyclerView(this.findViewById(android.R.id.content));
        showMembers();
        //get new item reference from backend
        setRef();
    }

    @Override
    public void onStart() {
        super.onStart();

        //initialize firestore
        fire_storage = FirebaseStorage.getInstance();
        storageRef = fire_storage.getReference();

        UtilityFunctions.clearView(item_desc, item_name, item_tags);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////


    // Code for Privacy Settings
    private void initRecyclerView(View view) {
        recyclerView = view.findViewById(R.id.privacy_recycler);

        recyclerView.setHasFixedSize(true);
        //might be wrong
        RecyclerView.LayoutManager mManager = new GridLayoutManager(getApplicationContext(), 3);
        recyclerView.setLayoutManager(mManager);
        this.mAdapter = new MemberRAdapter(new ArrayList<ArrayList<String>>());
        recyclerView.setAdapter(mAdapter);
    }

    //returns all members in users current family
    private void showMembers() {
        BackendItem backendItem = new BackendItem(UserSingleton.IP + MEMBERS_URL, BackendItem.GET);
        backendItem.setHeaders(new HashMap<String, String>());

        try {
            new GetMembersTask().execute(backendItem);
        } catch (Exception e) {
            Log.e(TAG, "showMembers: ", e);
        }
    }

    //gets a new reference (item image name) from the backend
    private void setRef() {
        BackendItem item = new BackendItem(UserSingleton.IP + GET_IMAGE_REF, BackendItem.GET);
        item.setHeaders(new HashMap<String, String>());

        try {
            new GetReferenceTask().execute(item);
        } catch (Exception e) {
            Log.e(TAG, "setRef: ", e);
        }

    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    //functions to access the backedn ML object recognition model
    public void getDesc(View view) {
        if (image == null) {
            return;
        }
        spinner.setVisibility(View.VISIBLE);
        StorageReference newRef = storageRef.child(ref);
        Uri file = Uri.fromFile(image);
        UploadTask uploadTask = newRef.putFile(file);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                uploadedPic = true;
                BackendItem item = new BackendItem(UserSingleton.IP + DETECTION, BackendItem.POST);
                item.setHeaders(new HashMap<String, String>());

                //generate add item body
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.accumulate("image", ref);
                    item.setBody(jsonObject.toString());
                } catch (JSONException e) {
                    Log.e(TAG, e.toString());
                }
                Log.d(TAG, item.getBody());

                try {
                    new GetSuggestionsTask().execute(item);
                } catch (Exception e) {
                    Log.e(TAG, "onSuccess: ", e);
                }

            }
        });
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //checks if there is enough information to check items
    public void submitItemCheck(View view) {

        String name = item_name.getText().toString();
        String desc = item_desc.getText().toString();
        String tags = item_tags.getText().toString();

        if (!name.equals("") && !desc.equals("") && !tags.equals("") && (image != null)
                && mAdapter.getCheckedItems().size() > 0 && !ref.equals("")) {
            submitItem();
        } else {
            Toast toast = Toast.makeText(AddItemActivity.this, "Please complete all fields",
                    Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
        }
    }


    //submits an item to the backend.
    private void submitItem() {
        spinner.setVisibility(View.VISIBLE);
        //give time to set spinner visible
        try {
            Thread.sleep(100);
        } catch (Exception e) {
            Log.e(TAG, "submitItem: ", e);
        }

        //get new refernce in firebase storage
        if (BuildConfig.DEBUG && ref.equals("")) {
            throw new AssertionError();
        }

        if (uploadedPic) {
            sendItemInfo();
        } else {
            final StorageReference newRef = storageRef.child(ref);
            Uri file = Uri.fromFile(image);

            //upload file to storage
            UploadTask uploadTask = newRef.putFile(file);
            // Register observers to listen for when the download is done or if it fails
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                    Log.e(TAG, "upload failed   " + exception.toString());

                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    //send info to backend
                    sendItemInfo();
                }
            });
        }

        // code to implement progress bar.
//        uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
//                double progress = 100.0 * (taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
//                System.out.println("Upload is " + progress + "% done");
//                int currentprogress = (int) progress;
//                progressBar.setProgress(currentprogress);
//            }
//        }).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {
//                System.out.println("Upload is paused");
//            }
//        });
    }

    //send item add information to the backend
    private void sendItemInfo() {

        //get needed info
        String name = item_name.getText().toString();
        String desc = item_desc.getText().toString();
        String tags = item_tags.getText().toString();

        //create backend item for POST
        BackendItem backendItem = new BackendItem(UserSingleton.IP + ADD_URL, BackendItem.POST);
        HashMap<String, String> headers = new HashMap<>();
        backendItem.setHeaders(headers);

        //create POST body
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("name", name);
            jsonObject.accumulate("description", desc);
            jsonObject.accumulate("tags", UtilityFunctions.convert(tags.split(","))); //convert tags to array.
            jsonObject.accumulate("image", ref);
            jsonObject.accumulate("visibility", UtilityFunctions.convert(mAdapter.getCheckedItems()));
            backendItem.setBody(jsonObject.toString());
        } catch (JSONException e) {
            Log.e(TAG, "sendItemInfo: ", e);
        }

        Log.d(TAG, backendItem.getBody());

        BackendReq.send_req(backendItem);

        if (backendItem.getResponse_code() == 200) {
            goToHomePage();
        } else {
            Toast toast = Toast.makeText(AddItemActivity.this, "Connection to backend failed",
                    Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
        }
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////

    /* Camera take photo activity and helper functions */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                Bitmap bitmap = BitmapFactory.decodeFile(pathToFile);
                imageView.setImageBitmap(bitmap);
            }
        }
    }

    private void takePhoto() {
        //Call Intent
        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //Ensure that there is an app that can handle our intent
        if (takePicture.resolveActivity(getPackageManager()) != null) {
            File photo = null;
            photo = getPhotoFile();
            this.image = photo;
            if (photo != null) {
                pathToFile = photo.getAbsolutePath();
                Uri photoURI = FileProvider.getUriForFile(AddItemActivity.this, "com.example.itemorganizer.fileprovider", photo);
                takePicture.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePicture, 1);

            }
        }
    }

    private File getPhotoFile() {

        String name = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        // GetExternalStoragePublicDirectory function taken from
        // https://developer.android.com/training/camera/photobasics
        File storageDir = getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = null;
        try {
            image = File.createTempFile(name, ".jpg", storageDir);
        } catch (IOException e) {
            Log.d("Test", "Exception: " + e.toString());
        }
        return image;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////

    private void goToHomePage() {
        Intent intent = new Intent(this, HomePage.class);
        startActivity(intent);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    // task to retrieve members from the backend.
    // displays members to recycler view then,
    // needs recycler to be already init.
    private class GetMembersTask extends AsyncTask<BackendItem, Void, BackendItem> {
        @Override
        protected BackendItem doInBackground(BackendItem... items) {
            // params comes from the execute() call: params[0] is the url.
            try {
                BackendReq.httpReq(items[0]);
            } catch (IOException e) {
                Log.e(BackendReq.class.toString(), e.toString());
                items[0].setResponse_code(777);
                items[0].setResponse("Connection failed to backend or request is invalid");
            }
            return items[0];
        }

        @Override
        protected void onPostExecute(BackendItem backendItem) {
            super.onPostExecute(backendItem);
            setMembersToRecycler(getMembers(backendItem.getResponse()));
        }
    }


    //returns all members in users current family from response body
    private HashMap<String, String> getMembers(String responseBody) {
        HashMap<String, String> id_names = new HashMap<>();

        try {
            JSONObject raw_data = new JSONObject(responseBody);
            JSONArray keys = raw_data.names();

            for (int i = 0; i < keys.length(); i++) {
                String key = keys.getString(i);
                id_names.put(key, raw_data.getJSONObject(key).getString("name"));
            }
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return id_names;
    }

    //sets members to recycler view along with global option
    private void setMembersToRecycler(HashMap<String, String> id_names) {
        // add global
        ArrayList<String> global = new ArrayList<>();
        global.add("global");
        global.add("All");
        this.mAdapter.addAndNotify(global);

        //get
        ArrayList<String> member_ids = new ArrayList<>(id_names.keySet());

        //put add names
        for (String id : member_ids) {
            ArrayList<String> tempMember = new ArrayList<>();
            tempMember.add(id);                 //ID as position [0]
            tempMember.add(id_names.get(id)); // Name as position [1] in array
            this.mAdapter.addAndNotify(tempMember);
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////

    private class GetSuggestionsTask extends AsyncTask<BackendItem, Void, BackendItem> {
        private String TAG = "GetSuggestionTask";

        @Override
        protected BackendItem doInBackground(BackendItem... items) {
            // params comes from the execute() call: params[0] is the url.
            try {
                BackendReq.httpReq(items[0]);
            } catch (IOException e) {
                Log.e(TAG, "doInBackground: ", e);
                items[0].setResponse_code(777);
                items[0].setResponse("Connection failed to backend or request is invalid");
            }
            return items[0];
        }

        @Override
        protected void onPostExecute(BackendItem backendItem) {
            super.onPostExecute(backendItem);
            setSuggestedTags(backendItem.getResponse());
            spinner.setVisibility(View.GONE);

        }
    }


    private void setSuggestedTags(String response) {
        String tagStr = "";

        //get tags from response
        try {
            JSONObject raw_data = new JSONObject(response);
            JSONArray tags = raw_data.getJSONArray("tags");
            tagStr = UtilityFunctions.convertTags(tags);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }

        item_tags.setText(tagStr, TextView.BufferType.EDITABLE);
    }

    /////////////////////////////////////////////////////////////////////////////////////////////

    /***
     * Task to get a new reference from the backend to store the image
     * in firebase storage
     */
    private class GetReferenceTask extends AsyncTask<BackendItem, Void, BackendItem> {
        private String TAG = "GetReferenceTask";

        @Override
        protected BackendItem doInBackground(BackendItem... items) {
            // params comes from the execute() call: params[0] is the url.
            try {
                BackendReq.httpReq(items[0]);
            } catch (IOException e) {
                Log.e(TAG, "doInBackground: ", e);
                items[0].setResponse_code(777);
                items[0].setResponse("Connection failed to backend or request is invalid");
            }
            return items[0];
        }

        @Override
        protected void onPostExecute(BackendItem backendItem) {
            super.onPostExecute(backendItem);
            secondarySpinner.setVisibility(View.GONE);
            updateReference(backendItem.getResponse());
        }
    }

    private void updateReference(String raw_data) {
        String result = "";
        try {
            JSONObject jsonObject = new JSONObject(raw_data);
            JSONArray keys = jsonObject.names();
            String key = keys.getString(0);
            String number = jsonObject.getString(key);
            result = key + "/" + number + ".jpg";

        } catch (Exception e) {
            Log.e(TAG, e.toString());
            Log.e(TAG, "Reference not returned");
        }
        this.ref = result;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

}
