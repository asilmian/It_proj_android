package com.example.itemorganizer;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.itemorganizer.MainActivity;
import com.example.itemorganizer.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.os.Environment.getExternalStoragePublicDirectory;

public class Camera extends AppCompatActivity {

    ImageView imageView;
    String pathToFile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button pictureBtn = (Button) findViewById(R.id.PictureBtn);
        imageView = (ImageView) findViewById(R.id.image);
        if (Build.VERSION.SDK_INT >= 23){
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
        }
        pictureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePhoto();
            }
        });

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
            File Photo = null;
            Photo = getPhotoFile();
            if (Photo != null)
            {
                pathToFile = Photo.getAbsolutePath();
                Uri photoURI = FileProvider.getUriForFile( Camera.this, "com.example.cameratest.fileprovider", Photo);
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
