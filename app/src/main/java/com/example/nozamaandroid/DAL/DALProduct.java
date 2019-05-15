package com.example.nozamaandroid.DAL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ImageView;

import com.example.nozamaandroid.Models.Products;
import com.example.nozamaandroid.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class DALProduct {

    Products product;
    ArrayList<Products> productsArrayList;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
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
                                String getFireStoreFieldDetails = document.getString("Product Details");
                                String getFireStoreId = document.getId();
                                product = new Products();
                                product.setProdName(getFireStoreFieldName);
                                product.setProdDetails(getFireStoreFieldDetails);
                                product.setProdId(getFireStoreId);
                                product.setPictureId(document.getString("pictureId"));
                                productsArrayList.add(product);
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                Log.i(TAG, "What is value: " + getFireStoreFieldName);
                            }
                            Log.d(TAG, "onComplete: " + productsArrayList);
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
        return productsArrayList;
    }


    public void setImageviewById(String pictureId, final ImageView imageView) {
        if (pictureId != null) {
            mStorageRef.child("product-pictures/" + pictureId).
                    getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    // Use the bytes to display the image
                    Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    imageView.setImageBitmap(bm);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                    imageView.setImageResource(R.drawable.cake);
                }
            });
        } else {
            imageView.setImageResource(R.drawable.cake);
        }
    }
}
