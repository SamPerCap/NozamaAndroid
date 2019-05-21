package com.example.nozamaandroid.DAL;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.nozamaandroid.Models.Order;
import com.example.nozamaandroid.Shared.IMyCallBack;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DALOrder {
    private Map<String, Object> orderMap = new HashMap<>();
    public static String TAG = "DALOrder";
    private ArrayList<Order> listOfOrders;
    private ArrayList<Order> listOfOrdersOnWay;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();


    public void addOrder(final Order order) {
        try {
            // FireStoreDatabase initialize
            orderMap.put("user id", order.getIdUser());
            db.collection("orders").add(order).
                    addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReferesnce) {

                            Log.d(TAG, "DocumentSnapshot added with ID: " + documentReferesnce.getId());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e(TAG, "Error adding document", e);
                        }
                    });

        } catch (Error e) {
            Log.e(TAG, "Exception: " + e);
        }
    }

    public void getOrder(final String userID, final IMyCallBack myCallBack) {

        listOfOrders = new ArrayList<>();
        listOfOrdersOnWay = new ArrayList<>();
        Log.d(TAG, "Getting orders from database");
        db.collection("orders")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d(TAG, document.getString("idUser") + " =? " + userID);
                        if (Objects.equals(document.getString("idUser"), userID)) {
                            Log.d(TAG, document.getId());
                            ArrayList<String> getFireStoreProductsId = (ArrayList<String>) document.get("products");
                            Boolean getFireStoreStatus = document.getBoolean("statusOfDelivery");
                            String getFireStoreUserId = document.getString("idUser");
                            String getFireStoreOrderId = document.getId();
                            Order order = new Order();

                            order.setOrderId(getFireStoreOrderId);
                            order.setIdUser(getFireStoreUserId);
                            order.setProductsId(getFireStoreProductsId);
                            order.setStatusOfDelivery(getFireStoreStatus);

                            listOfOrders.add(order);
                            if (!getFireStoreStatus)
                                listOfOrdersOnWay.add(order);
                        }
                    }
                    Log.d(TAG, "onComplete: " + listOfOrders);
                    myCallBack.onCallBack(listOfOrders, listOfOrdersOnWay);
                } else {
                    Log.d(TAG, "Error getting documents.", task.getException());
                }
            }
        });
    }
}
