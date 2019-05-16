package com.example.nozamaandroid.BLL;

import com.example.nozamaandroid.Shared.ImageResponse;
import com.example.nozamaandroid.DAL.DALProduct;
import com.example.nozamaandroid.Models.Products;

import java.util.ArrayList;

public class BLLProducts {
    DALProduct dalProduct = new DALProduct();

    public ArrayList<Products> readProductsFromDatabase(){
       return dalProduct.readProductsFromDatabase();
    }

    public void getImageById(String pictureId, ImageResponse imageResponse) {
        dalProduct.setImageviewById(pictureId, imageResponse);
    }

}
