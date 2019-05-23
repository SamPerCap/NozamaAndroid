package com.example.nozamaandroid.DAL;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.nozamaandroid.Shared.OnResponse;
import com.example.nozamaandroid.Models.Products;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DALProduct {

    private Products product;
    DALImage dalImage = new DALImage();
    private ArrayList<Products> productsArrayList;
    private Map<String, Object> productMap = new HashMap<>();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    public static String TAG = "DALProduct";

    public ArrayList<Products> readProductsFromDatabase() {
        productsArrayList = new ArrayList<>();
        Log.d(TAG, "Getting products from database");
        db.collection("products")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String getFireStoreFieldName = document.getString("name");
                                String getFireStoreFieldDetails = document.getString("details");
                                String getFireStoreFieldCategory = document.getString("category");
                                Double getFireStorePrice = document.getDouble("price");
                                //String getFireStorePictureId = document.getString("pictureId");
                                String getFireStoreId = document.getId();
                                product = new Products();
                                product.setPictureId(document.getString("pictureId"));
                                product.setProdName(getFireStoreFieldName);
                                product.setProdDetails(getFireStoreFieldDetails);
                                product.setProdId(getFireStoreId);
                                product.setCategory(getFireStoreFieldCategory);
                                product.setPrice(getFireStorePrice);
                                //product.setPictureId(getFireStorePictureId);
                                productsArrayList.add(product);
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                Log.i(TAG, "What is value: " + getFireStoreFieldName);
                            }
                            Log.d(TAG, "onComplete: " + productsArrayList);
                        } else {
                            Log.d(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
        return productsArrayList;
    }


    public void addProduct(final Products productToAdd, final byte[] preImage, final OnResponse onResponse) {
        try {

            // FireStoreDatabase initialize
            productMap.put("name", productToAdd.getProdName());
            productMap.put("price", productToAdd.getPrice());
            productMap.put("details", productToAdd.getProdDetails());
            productMap.put("category", productToAdd.getCategory());
            // Add a new document with a generated ID
            db.collection("products")
                    .add(productMap)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                            productToAdd.setProdId(documentReference.getId());
                            dalImage.uploadImage(null, preImage, onResponse, productToAdd);

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error adding document", e);
                        }
                    });

            Log.e(TAG, "What is get text: " + productToAdd.getProdName());


        } catch (Error e) {
            Log.e(TAG, "Exception: " + e);
        }

    }
}
