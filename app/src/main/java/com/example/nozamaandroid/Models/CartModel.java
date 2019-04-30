package com.example.nozamaandroid.Models;

import java.util.ArrayList;

public class CartModel {
    ArrayList<Products>  cartList= new ArrayList<Products>();

    private static volatile CartModel instance;

    //singleton
    public static CartModel getInstance() {
        if (instance == null) {
            synchronized (CartModel.class) {
                if (instance == null) {
                    instance = new CartModel();
                }
            }
        }
        return instance;
    }

    public ArrayList<Products> getProductInCart()
    {
       return cartList;
    }

    public void addProductToCart(Products products)
    {
        cartList.add(products);
    }
}
