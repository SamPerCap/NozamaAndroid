package com.example.nozamaandroid.Models;

import android.util.Log;

import java.io.Serializable;

public class Products implements Serializable {
    String TAG = "Products class";
    private String prodId;
    private String prodName;
    private String prodDetails;
    private Float ratings;
    private int amount;
    private String pictureId;

    public String getPictureId() {
        return pictureId;
    }

    public void setPictureId(String pictureId) {
        this.pictureId = pictureId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Products() {
    }

    public Products(String prodId, String prodName, String prodDetails) {
        this.prodId = prodId;
        this.prodName = prodName;
        this.prodDetails = prodDetails;
    }

    public String getProdId() {
        return prodId;
    }

    public void setProdId(String prodId) {
        this.prodId = prodId;
    }

    public void setProdName(String prodName) {
        Log.i(TAG, "what is prodName inside Products: " + prodName);
        this.prodName = prodName;
    }

    public String getProdName() {
        Log.i(TAG, "what is prodName inside getProdName: " + prodName);
        return prodName;
    }

    public void setProdDetails(String prodDetails) {
        this.prodDetails = prodDetails;
    }

    public String getProdDetails() {
        return prodDetails;
    }

    public Float getRating() {
        return ratings;
    }

    public void setRating(Float ratings) {
        this.ratings = ratings;
    }
}
