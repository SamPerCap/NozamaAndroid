package com.example.nozamaandroid.Models;

import android.content.Context;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.util.Log;
import android.widget.Toast;

import com.example.nozamaandroid.Adaptor.AdapterCart;
import com.example.nozamaandroid.HomeView;

import java.util.ArrayList;

public class CartModel {
    AdapterCart adapterCart;
    public ObservableList<Products> cartList = new ObservableArrayList<>();
    ArrayList<Products> arrayCartList = new ArrayList<>();
    private static CartModel instance;
    int one = 1;
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
            if (!arrayCartList.contains(p))
                arrayCartList.add(p);
        return arrayCartList;
    }


    public void addProductToCart(Products products) {

        if (cartList.contains(products)) {
            changeAmount(true, products);

        } else {
            products.setAmount(1);
            cartList.add(products);
        }

    }

    public void checkIfProductId(Products _products, Context context) {
        ArrayList<String> productsID = new ArrayList<>();
        /*
         *For each product inside the list of products in the cart,
         *get the ID of each one and add it to an array of Strings.
         *Compare if ids matches.
         */
        for (Products products : getProductInCart())
            productsID.add(products.getProdId());

        if (productsID.contains(_products.getProdId())) {
            changeAmount(true, _products);
            Toast.makeText(context, "The product is already on the cart", Toast.LENGTH_SHORT).show();
        } else {
            addProductToCart(_products);
            Toast.makeText(context, "The product has been added to the cart.", Toast.LENGTH_SHORT).show();
        }
    }

    public void clearList() {
        cartList.clear();
        adapterCart.clear();
    }

    public void removeItem(int postion) {
        cartList.remove(postion);
        Log.d(TAG, "Item removed from list");
    }

    public void changeAmount(Boolean increase, Products product) {
        int productAmount = product.getAmount();
        if (increase) {
            product.setAmount(productAmount + one);
        } else {
            product.setAmount(productAmount - one);
        }
    }
}
