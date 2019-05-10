package com.example.nozamaandroid.DAL;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.nozamaandroid.Models.Order;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class DALOrder   {
    Map<String, Object> productMap = new HashMap<>();
    public static String TAG = "ProductApp";
    public void addOrder(Order order) {
        try
        {


            // FireStoreDatabase initialize
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            productMap.put("user id", order.getIdUser());

           // productMap.put("Products", order.getProducts());
db.collection("orders").add(order).
        addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
    @Override
    public void onSuccess(DocumentReference documentReferesnce) {
        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReferesnce.getId());



    }
})
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                Log.w(TAG, "Error adding document", e);
            }
        });;
            // Add a new document with a generated ID
          /*  db.collection("order")
                    .add(productMap)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());



                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {
                            Log.w(TAG, "Error adding document", e);
                        }
                    });

            Log.e(TAG, "What is user id: " + order.getIdUser());
*/


        }
        catch(Error e)
        {
            Log.e(TAG, "Exception: " + e);
        }
    }
}
