package com.example.itemorganizer;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.itemorganizer.HomePage.HomePage;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import static android.os.Environment.getExternalStoragePublicDirectory;

public class CameraActivity extends AppCompatActivity {

    ImageView imageView;
    String pathToFile;
    Button pictureBtn;
    Button addbtn;
    EditText item_name;
    EditText item_desc;
    EditText item_tags;
    File image;

    private final static String TAG = CameraActivity.class.toString();
    private HashMap<String, String> id_names;
    private static final String URL = "items/add/";

    private final static String URL = "family/info/members/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        imageView = (ImageView) findViewById(R.id.image);
        if (Build.VERSION.SDK_INT >= 23){
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
        }

        pictureBtn = (Button) findViewById(R.id.PictureBtn);
        addbtn = (Button) findViewById(R.id.add_item);

        item_name = findViewById(R.id.add_item_name);
        item_desc = findViewById(R.id.add_item_desc);
        item_tags = findViewById(R.id.add_item_tags);


        pictureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePhoto();
            }
        });
        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 submitItem(view);
            }
        });
    }

    @Override
    public void onStart(){
        super.onStart();
        this.id_names = getMembers();

        //display members
    }

    private HashMap<String,String> getMembers(){
        HashMap<String,String> id_names = new HashMap<>();
        BackendItem backendItem = new BackendItem(UserSingleton.IP + URL, BackendReq.GET);
        BackendReq.send_req(backendItem);

        try{
            JSONObject raw_data = new JSONObject(backendItem.getResponse());
            JSONArray keys  = raw_data.names();

            for (int i=0; i<keys.length(); i++){
                String key = keys.getString(i);
                id_names.put(key, raw_data.getJSONObject(key).getString("name"));
            }
        } catch (Exception e){
            Log.e(TAG, e.toString());
        }
        return id_names;
    }

    public void submitItem(View view) {

        String name = item_name.getText().toString();
        String desc = item_desc.getText().toString();
        String tags = item_tags.getText().toString();

        if( name != null && desc != null && tags!= null && bitmap != null){
            if(submitItem(name, desc, tags, bitmap)){
                Log.d(TAG, name + "   " + desc + "   " + tags);
                Intent intent = new Intent(this, HomePage.class);
                startActivity(intent);
            }
            else{
                Toast toast = Toast.makeText(CameraActivity.this, "Connection to backend failed",
                        Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL,0,0);
                toast.show();
            }
        }
        else{
            Toast toast = Toast.makeText(CameraActivity.this, "Please complete all fields",
                    Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL,0,0);
            toast.show();
        }
    }

    private Boolean submitItem(String name, String desc, String tags, Bitmap bitmap){
        BackendItem backendItem = new BackendItem(UserSingleton.IP + URL, BackendReq.POST);

        if (backendItem.getResponse_code() == 200){
            return true;
        }
        else{
            return false;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
        {
            if (requestCode == 1)
            {
                Bitmap bitmap = BitmapFactory.decodeFile(pathToFile);
                imageView.setImageBitmap(bitmap);
            }
        }
    }

    private void takePhoto(){
        //Call Intent
        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //Ensure that there is an app that can handle our intent
        if (takePicture.resolveActivity(getPackageManager()) != null) {
            File photo = null;
            photo = getPhotoFile();
            if (photo != null)
            {
                pathToFile = photo.getAbsolutePath();
                Uri photoURI = FileProvider.getUriForFile( CameraActivity.this, "com.example.itemorganizer.fileprovider", photo);
                takePicture.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePicture,1);
            }
        }
    }

    private File getPhotoFile(){

        String name = new SimpleDateFormat( "yyyyMMdd_HHmmss").format(new Date());
        // GetExternalStoragePublicDirectory function taken from
        // https://developer.android.com/training/camera/photobasics
        File storageDir = getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = null;
        try {
            image = File.createTempFile(name, ".jpg", storageDir);
        }
        catch (IOException e)
        {
            Log.d("Test","Exception: " + e.toString());
        }
        return image;
    }

}
