package com.example.nozamaandroid.BLL;

import com.example.nozamaandroid.DAL.DALProductImage;

public class BLLProductImage {
    DALProductImage dalUserImage = new DALProductImage();
    public boolean getImageById(String pictureId) {
        return  dalUserImage.getImageById(pictureId);
    }
}
