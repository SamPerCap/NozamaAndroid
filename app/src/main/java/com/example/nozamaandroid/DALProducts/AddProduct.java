package com.example.nozamaandroid.DALProducts;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.nozamaandroid.HomeView;
import com.example.nozamaandroid.Models.Products;
import com.example.nozamaandroid.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class AddProduct extends AppCompatActivity {

    public static String TAG = "ProductApp";
    EditText dbName, dbValue;
    DatabaseReference dref;
    String nameKey = "nameKey";
    String detailKey = "detailKey";


    Map<String, Object> productMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle saveInstance)
    {
        super.onCreate(saveInstance);
        setContentView(R.layout.addproduct_detail);
        dbName = findViewById(R.id.dbName);
        dbValue = findViewById(R.id.dbValue);

        // FirebaseDatabase
        dref = FirebaseDatabase.getInstance().getReference("products");
    }

    /*private void addProductName()
    {
        dbName.getText().toString();
        dbValue.getText().toString();

        String prodName = dbName.getText().toString();
        String dataBase = dbValue.getText().toString();

        if ( !TextUtils.isEmpty(prodName) &&  !TextUtils.isEmpty(dataBase) )
        {
            try
            {
                // get the unique id
                String id = dref.push().getKey();

                // Send the data with id and name and value
                Products prods = new Products(id,prodName,dataBase);

                dref.child(id).setValue(prods);
            }
            catch (DatabaseException d)
            {
                Log.e(TAG,"Something Went Wrong: " + d);
            }
            goToMain();
        }
        else {
            Toast.makeText(this, "Please type in a name of the DB and the value", Toast.LENGTH_SHORT).show();
        }
    }*/

    private void goToMain()
    {
        Intent intent = new Intent(this, HomeView.class);
        startActivity(intent);
    }

    public void saveData(View view)
    {
        try
        {
            final Products products = new Products();

            // FireStoreDatabase initialize
            FirebaseFirestore db = FirebaseFirestore.getInstance();
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

                            Log.i(TAG, "What is products: " + products.getProdName().toString());
                            Log.d(TAG, "What is products: " + products.getProdName().toString());

                            Intent intent = new Intent(AddProduct.this,HomeView.class);
                            intent.putExtra(nameKey, products.getProdName());
                            intent.putExtra(detailKey, products.getProdDetails());
                            startActivity(intent);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {
                            Log.w(TAG, "Error adding document", e);
                        }
                    });

            Log.e(TAG, "What is get text: " + dbName.getText());



        }
        catch(Error e)
        {
            Log.e(TAG, "Exception: " + e);
        }
    }
}
