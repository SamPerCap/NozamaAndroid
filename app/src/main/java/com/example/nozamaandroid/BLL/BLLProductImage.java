package com.example.nozamaandroid.BLL;

import android.widget.ImageView;

import com.example.nozamaandroid.DAL.DALProductImage;
import com.google.firebase.storage.StorageException;

public class BLLProductImage  {
    DALProductImage dalProductImage = new DALProductImage();
    public void getImageById(String pictureId, final ImageView imageView) {
         dalProductImage.setImageviewById(pictureId,imageView);
    }
}
