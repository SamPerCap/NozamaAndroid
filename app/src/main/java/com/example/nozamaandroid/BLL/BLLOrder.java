package com.example.nozamaandroid.BLL;

import com.example.nozamaandroid.DAL.DALOrder;
import com.example.nozamaandroid.Models.Order;

public class BLLOrder {
    DALOrder dalOrder = new DALOrder();
    public void addOrder(Order order) {
        dalOrder.addOrder(order);
    }
}
