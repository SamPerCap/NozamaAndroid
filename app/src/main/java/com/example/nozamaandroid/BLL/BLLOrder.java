package com.example.nozamaandroid.BLL;

import com.example.nozamaandroid.DAL.DALOrder;
import com.example.nozamaandroid.Models.Order;
import com.example.nozamaandroid.Shared.IMyCallBack;

import java.util.ArrayList;

public class BLLOrder {
    DALOrder dalOrder = new DALOrder();

    public void addOrder(Order order) {
        dalOrder.addOrder(order);
    }

    public void getOrder(String userId, IMyCallBack myCallBack) {
        dalOrder.getOrder(userId, myCallBack);
    }
}
