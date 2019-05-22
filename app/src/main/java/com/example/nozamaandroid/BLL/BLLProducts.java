package com.example.nozamaandroid.BLL;

import android.graphics.Bitmap;
import android.util.Log;

import com.example.nozamaandroid.DAL.DALImage;
import com.example.nozamaandroid.Shared.OnResponse;
import com.example.nozamaandroid.DAL.DALProduct;
import com.example.nozamaandroid.Models.Products;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class BLLProducts {
    DALProduct dalProduct = new DALProduct();
    DALImage dalImage = new DALImage();
    String TAG = "BLLProduct";

    public ArrayList<Products> readProductsFromDatabase() {
        return dalProduct.readProductsFromDatabase();
    }

    public void addProduct(Products products, Bitmap preImage, OnResponse onResponse){
        Log.d(TAG,"Starting upload picture to FB");
        Log.d(TAG, "Current product id: " + products.getProdId());
        Bitmap bitmap = preImage;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 60, baos);
        byte[] data = baos.toByteArray();
        Log.d(TAG,"upload to storage byte: " + data.toString());
        dalProduct.addProduct(products, data, onResponse);
    }
    public void getImageById(String pictureId, OnResponse onResponse) {
        dalImage.setImageviewById(pictureId, onResponse);
    }

}
