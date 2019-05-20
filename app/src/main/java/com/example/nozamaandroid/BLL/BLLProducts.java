package com.example.nozamaandroid.BLL;

import com.example.nozamaandroid.Shared.OnResponse;
import com.example.nozamaandroid.DAL.DALProduct;
import com.example.nozamaandroid.Models.Products;

import java.util.ArrayList;

public class BLLProducts {
    DALProduct dalProduct = new DALProduct();

    public ArrayList<Products> readProductsFromDatabase() {
        return dalProduct.readProductsFromDatabase();
    }

    public void getImageById(String pictureId, OnResponse onResponse) {
        dalProduct.setImageviewById(pictureId, onResponse);
    }

}
