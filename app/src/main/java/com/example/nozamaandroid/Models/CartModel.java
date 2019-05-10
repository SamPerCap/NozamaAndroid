package com.example.nozamaandroid.Models;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.util.Log;

import com.example.nozamaandroid.HomeView;

import java.util.ArrayList;

public class CartModel {
    public ObservableList<Products> cartList = new ObservableArrayList<>();
    ArrayList<Products> arrayCartList = new ArrayList<>();
    private static volatile CartModel instance;
    String TAG = "CartModel";

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

    public ArrayList<Products> getProductInCart() {
        for (Products p : cartList)
            arrayCartList.add(p);

        return arrayCartList;
    }


    public void addProductToCart(Products products) {
        boolean changed = false;
        for (int i = 0; i < cartList.size(); i++) {
            if (cartList.get(i).getProdId() == products.getProdId()) {
                products.setAmount(cartList.get(i).getAmount() + 1);
                Log.d(TAG, "addProductToCart same product: " + products.getProdName() + ":"
                        + products.getAmount());
                changed = true;

                cartList.set(i, products);
            }
        }
        if (!changed) {
            cartList.add(products);
        }
    }

    public void clearList() {
        cartList.clear();
    }

    public void removeItem(int postion) {
        cartList.remove(postion);
    }

    public void changeAmount(int postion, int i) {
        cartList.get(postion).setAmount(cartList.get(postion).getAmount() + i);
    }
}
