package com.example.nozamaandroid.Models;

import android.graphics.Bitmap;

import com.example.nozamaandroid.BLL.BLLUser;
import com.example.nozamaandroid.Shared.OnResponse;

public class UserModel {
    BLLUser bllUser = new BLLUser();
    private static UserModel instance;
    public Bitmap preImage;
    public static UserModel getInstance() {
        if (instance == null) {
            synchronized (UserModel.class) {
                if (instance == null) {
                    instance = new UserModel();
                }
            }
        }
        return instance;
    }
    public void changePreImage(Bitmap bitmap)
    {
        preImage = bitmap;
    }

    public void getUserById(String uid, OnResponse response) {
       bllUser.getUserById(uid, response);
    }
}
