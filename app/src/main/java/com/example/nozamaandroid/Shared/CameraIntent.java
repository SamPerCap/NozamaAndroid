package com.example.nozamaandroid.Shared;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.nozamaandroid.R;

public class CameraIntent extends AppCompatActivity {
    String TAG = "CameraIntent";
    private static CameraIntent instance;
    String messageToCamara, imageChange;
    private static final int PERMISSION_REQUEST_CODE = 1;
    public Bitmap preImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        messageToCamara = getString(R.string.activityClass);
        imageChange = getString(R.string.imageChange);
        image();
    }


    public static CameraIntent getInstance() {
        if (instance == null) {
            synchronized (CameraIntent.class) {
                if (instance == null) {
                    instance = new CameraIntent();
                }
            }
        }
        return instance;
    }

    public void image() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_DENIED) {
                    Log.d(TAG, "permission denied to CAMERA - requesting it");
                    String[] permissions = {Manifest.permission.CAMERA};
                    requestPermissions(permissions, PERMISSION_REQUEST_CODE);
                }
            }
            startActivityForResult(cameraIntent, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PERMISSION_REQUEST_CODE && resultCode == RESULT_OK) {
            Bundle b = data.getExtras();
            preImage = (Bitmap) b.get("data");
            changePreImage(preImage);
            Log.d(TAG, "finish camera");
            finish();
        } else {
            finish();
        }
    }

    public void changePreImage(Bitmap bitmap) {
        preImage = bitmap;
    }

}