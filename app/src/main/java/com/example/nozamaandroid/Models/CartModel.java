package com.example.nozamaandroid.Models;

import android.util.Log;

import com.example.nozamaandroid.HomeView;

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
        boolean changed = false;
        for (int i = 0; i <cartList.size() ; i++) {
            if(cartList.get(i).getProdId()== products.getProdId())
            {
                products.setAmount(cartList.get(i).getAmount()+1);
                Log.d(HomeView.TAG, "addProductToCart same product: " +  products.getProdName()+ ":"
                + products.getAmount());
                changed =true;

                cartList.set(i,products);
            }
        }
        if(!changed) {
            cartList.add(products);
        }
    }

    public void clearList() {
        cartList.clear();
    }

    public void removeItem(int postion) {
        cartList.remove(postion);
    }
// return true if product deleted
    public boolean changeAmount(int postion, int i) {

        if(cartList.get(postion).amount+i ==0)
        {
            cartList.remove(postion);
            return  true;

        }
        cartList.get(postion).setAmount(cartList.get(postion).getAmount()+i);
        return false;
    }
}
