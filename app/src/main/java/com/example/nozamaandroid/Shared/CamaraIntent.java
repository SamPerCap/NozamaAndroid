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
    String CreateUserKey = DALUser.class.getName();
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

            //mImageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(mCurrentPhotoPath));
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
        if(activity.toLowerCase().equals(CreateUserKey)) {
            Log.d(TAG, " go to: " + CreateUserKey);
            Intent camaraintent = new Intent(this, HomeView.class);
            camaraintent.putExtra(messageToCamara,imageChange);
            startActivity(camaraintent);
            finish();
        }

    }
    /*private void saveFileInLocalFolder() {
        FileOutputStream outputPhoto = null;
        try {
            File f = getOutputMediaFile();
            outputPhoto = new FileOutputStream(f);
            image
                    .compress(Bitmap.CompressFormat.PNG, 100, outputPhoto);
            Log.d(TAG, "Photo taken - size: " + f.length() );
            Log.d(TAG, "     Location: " + f.getAbsolutePath());
            filePath = f.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (outputPhoto != null) {
                    outputPhoto.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private File getOutputMediaFile(){

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {


            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_DENIED) {

                Log.d(TAG, "permission denied to WRITE_EXTERNAL_STORAGE - requesting it");
                String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};

                requestPermissions(permissions, PERMISSION_REQUEST_CODE);
            }
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {

            File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES), getResources().getString(R.string.app_name));

            // Create the storage directory if it does not exist
            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {
                    Log.d(TAG, "failed to create directory");
                    return null;
                }
            }


            // Create a media file name
            String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date());
            String postfix = "jpg";
            String prefix = "IMG";

            File mediaFile = new File(mediaStorageDir.getPath() +
                    File.separator + prefix +
                    "_" + timeStamp + "." + postfix);

            return mediaFile;
        }
        Log.d(TAG, "Permission for writing NOT granted");
        return null;
    }
*/

}