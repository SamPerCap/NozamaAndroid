package com.example.nozamaandroid.Models;

import java.util.ArrayList;

public class Order {
    String idUser;
    String orderId;
    String userAddress;
    ArrayList<String> productsId = new ArrayList<>();
    Boolean statusOfDelivery;

    public Boolean getStatusOfDelivery() {
        return statusOfDelivery;
    }

    public void setStatusOfDelivery(Boolean statusOfDelivery) {
        this.statusOfDelivery = statusOfDelivery;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }


    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }


    public String getIdUser() {
        return idUser;
    }

    public ArrayList<String> getProductsId() {
        return productsId;
    }

    public void setProductsId(ArrayList<String> productsId) {
        this.productsId = productsId;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }


}
