package com.example.nozamaandroid.DAL;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.nozamaandroid.Adaptor.AdaptorProduct;
import com.example.nozamaandroid.HomeView;
import com.example.nozamaandroid.Models.Products;
import com.example.nozamaandroid.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DALProduct extends AppCompatActivity {

    public static String TAG = "DALProduct";
    EditText dbName, dbValue;
    DatabaseReference dref = FirebaseDatabase.getInstance().getReference("products");
    String nameKey = "nameKey", detailKey = "detailKey";
    String getFireStoreFieldName, getFireStoreFieldDetails, getFireStoreId;
    Products product;
    ArrayList<Products> productsArrayList;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Map<String, Object> productMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle saveInstance) {
        super.onCreate(saveInstance);
        setContentView(R.layout.addproduct_detail);
        dbName = findViewById(R.id.dbName);
        dbValue = findViewById(R.id.dbValue);
    }

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
                                getFireStoreFieldName = document.getString("name");
                                getFireStoreFieldDetails = document.getString("Product Details");
                                getFireStoreId = document.getId();
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

    public void saveData(View view) {
        try {
            final Products products = new Products();

            // FireStoreDatabase initialize
            CollectionReference usersCollectionRef = db.collection("users");

            productMap.put("name", dbName.getText().toString());
            productMap.put("Product Details", dbValue.getText().toString());

            // Add a new document with a generated ID
            db.collection("products")
                    .add(productMap)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                            products.setProdName(dbName.getText().toString());
                            products.setProdDetails(dbValue.getText().toString());

                            Log.i(TAG, "What is products: " + products.getProdName());
                            Log.d(TAG, "What is products: " + products.getProdName());

                            Intent intent = new Intent(DALProduct.this, HomeView.class);
                            intent.putExtra(nameKey, products.getProdName());
                            intent.putExtra(detailKey, products.getProdDetails());
                            startActivity(intent);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error adding document", e);
                        }
                    });

            Log.e(TAG, "What is get text: " + dbName.getText());


        } catch (Error e) {
            Log.e(TAG, "Exception: " + e);
        }
    }
}
