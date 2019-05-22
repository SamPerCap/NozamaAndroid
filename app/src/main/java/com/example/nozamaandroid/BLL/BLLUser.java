package com.example.nozamaandroid.BLL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

import com.example.nozamaandroid.DAL.DALImage;
import com.example.nozamaandroid.DAL.DALUser;
import com.example.nozamaandroid.Models.Users;
import com.example.nozamaandroid.Shared.OnResponse;

import java.io.ByteArrayOutputStream;

public class BLLUser {
    DALImage dalImage = new DALImage();
    DALUser dalUser = new DALUser();
    String TAG = "BLLUser";

    public void createUser(Users user, Bitmap currentImage, OnResponse onResponse) {
        Log.d(TAG, "Starting uploadPictureToFB");
        Log.d(TAG, "What is the current userId: " + user.getUserId());
        Bitmap bitmap = currentImage;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 60, baos);
        byte[] data = baos.toByteArray();
        Log.d(TAG, "upload to storage byte: " + data.toString());
        dalUser.createUser(user, data, onResponse);
    }

    public void getUserById(String uid, OnResponse response) {
        dalUser.getUserById(uid, response);
    }

    public void getImageById(String uid, OnResponse response) {
        dalImage.setUserImage(uid, response);
    }

    public void updateUser(Users currentUser, String uid) {
        dalUser.updateUser(currentUser, uid);
    }

    public void removeAccount(String userId, OnResponse response) {
        dalUser.removeAccount(userId, response);
    }
}
