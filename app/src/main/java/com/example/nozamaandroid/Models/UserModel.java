package com.example.nozamaandroid.Models;

import android.graphics.Bitmap;

public class UserModel {
    private static UserModel instance;
    public Bitmap currentImage;
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
    public void changeImage(Bitmap bitmap)
    {
        currentImage = bitmap;
    }

}
