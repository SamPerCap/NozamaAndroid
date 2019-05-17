package com.example.nozamaandroid.BLL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

import com.example.nozamaandroid.DAL.DALUser;
import com.example.nozamaandroid.Models.Users;
import com.example.nozamaandroid.Shared.OnResponse;

import java.io.ByteArrayOutputStream;

public class BLLUser {
    DALUser dalUser = new DALUser();
    String TAG = "BLLUser";
    String filePath;










    public String getFilePath(String _filePath, ImageView pictureView) {
        try {
            if (_filePath != null) {
                Log.d(TAG, "is this here?");
                Bitmap bit = BitmapFactory.decodeFile(_filePath);
                Log.d(TAG, "is bit null: " + bit);
                if (bit != null) {
                    pictureView.setImageBitmap(bit);
                    Log.d(TAG, "image is this now: " + _filePath);
                    filePath = _filePath;
                }
            }
        } catch (Exception ex) {
            Log.d(TAG, "locateItems: images " + ex);
        }
        Log.d(TAG, "let's see what is image: " + _filePath);
        return filePath;
    }


    public void createUser(Users user, Bitmap currentImage, OnResponse onResponse) {
        Log.d(TAG, "Starting uploadPictureToFB");
        Log.d(TAG, "What is the current userId: " + user.getUserId());
        Bitmap bitmap = currentImage;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        Log.d(TAG, "upload to storage byte: " + data.toString());
        dalUser.createUser(user, data,onResponse);
    }

    public void getUserById(String uid, OnResponse response) {
        dalUser.getUserById(uid, response);
    }

    public void getImageById(String uid, OnResponse response) {
        dalUser.setUserImage(uid,response);
    }
}
