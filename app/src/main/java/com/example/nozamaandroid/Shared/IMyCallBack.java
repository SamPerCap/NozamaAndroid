package com.example.nozamaandroid.Shared;

import com.example.nozamaandroid.Models.Order;

import java.util.ArrayList;

public interface IMyCallBack {

    void onCallBack(ArrayList<Order> orderOnWay, ArrayList<Order> ordersInTotal);
}
