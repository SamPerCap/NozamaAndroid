package com.example.nozamaandroid.Shared;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.example.nozamaandroid.DAL.DALUser;
import com.example.nozamaandroid.HomeView;
import com.example.nozamaandroid.Models.UserModel;
import com.example.nozamaandroid.R;
import com.example.nozamaandroid.UserCreation;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CamaraIntent extends AppCompatActivity {
    String TAG = "CamaraIntent";
    UserModel userModel = UserModel.getInstance();
    String messageToCamara;
    String imageChange ;
    String CreateUserKey = UserCreation.class.getName();
    private static final int PERMISSION_REQUEST_CODE = 1;
    Bitmap image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       messageToCamara = getString(R.string.activityClass);
        imageChange = getString(R.string.imageChange);
        image();
    }




    public void image()
    {
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
            //cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
            startActivityForResult(cameraIntent, PERMISSION_REQUEST_CODE);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PERMISSION_REQUEST_CODE && resultCode == RESULT_OK) {


            Bundle b = data.getExtras();
            image = (Bitmap) b.get("data");
           userModel.changeImage(image);
           changeActivity();
        }
        else
        {
            finish();

        }
    }
    private void changeActivity()
    {
        Intent intent = getIntent();
        String activity = intent.getStringExtra(messageToCamara);
        if(activity.equals(CreateUserKey)) {
            Log.d(TAG, " go to: " + CreateUserKey);
            Intent camaraintent = new Intent(this, UserCreation.class);
            camaraintent.putExtra(messageToCamara,imageChange);
            startActivity(camaraintent);
            finish();
        }

    }


}