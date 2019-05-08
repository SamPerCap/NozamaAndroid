package com.example.nozamaandroid.Models;

import java.util.ArrayList;

public class Order {
    String idUser;

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public ArrayList<Products> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Products> products) {
        this.products = products;
    }

    ArrayList<Products> products = new ArrayList<Products>();
}
