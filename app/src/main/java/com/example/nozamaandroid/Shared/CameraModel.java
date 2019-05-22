package com.example.nozamaandroid.Shared;

import android.graphics.Bitmap;

public class CameraModel {

    private static CameraModel instance;
    public Bitmap preImage;

    public static CameraModel getInstance() {
        if (instance == null) {
            synchronized (CameraModel.class) {
                if (instance == null) {
                    instance = new CameraModel();
                }
            }
        }
        return instance;
    }


    public void changePreImage(Bitmap bitmap) {
        preImage = bitmap;
    }
}
