package com.example.nozamaandroid.BLL;

import com.example.nozamaandroid.DAL.DALProductImage;

public class BLLProductImage {
    DALProductImage dalProductImage = new DALProductImage();
    public boolean getImageById(String pictureId) {
        return dalProductImage.getImageById(pictureId);
    }
}
