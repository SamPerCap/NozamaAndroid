package com.example.nozamaandroid;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.nozamaandroid.DALProducts.AddProduct;
import com.example.nozamaandroid.DALUsers.AddUser;
import com.example.nozamaandroid.Models.Products;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{
    public static String TAG = "ProductApp";
    Products f = new Products();
    Context context;
    ArrayList<String> listItems = new ArrayList<>();
    ArrayList<String> listItems2 = new ArrayList<>();
    ListView listView;
    String prodKey = "nameKey";
    String prodKey2 = "detailKey";
    DatabaseReference dref;
    ArrayAdapter<String> adapter;
    public String details;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Needs to initialize before creating an instance (Should be onCreate)
        FirebaseApp.initializeApp(this);

        this.setTitle("NozamaGo");

        listView = findViewById(R.id.synchronizeProducts);

        dref=FirebaseDatabase.getInstance().getReference();
        dref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                //adapter.notifyDataSetChanged();
                try{
                    for (DataSnapshot prod: dataSnapshot.getChildren())
                    {
                        Log.i(TAG,"What is PROD: " + prod.child("prodDetails").getValue() + " " + prod.child("prodName").getValue());
                        Log.i(TAG, "User: " + prod.child("userName").getValue());
                        listItems.add(prod.child("prodName").getValue().toString());
                        listItems2.add(prod.child("prodDetails").getValue().toString());

                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_dropdown_item_1line,listItems);
                    listView.setAdapter(adapter);
                }
                catch(Exception e)
                {
                    Log.e(TAG, "Exception: " + e);
                }
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                listItems.remove(dataSnapshot.getValue(String.class));
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        clickOnList();
    }

    public void openUserView(View view)
    {
        Intent intent = new Intent(this, AddProduct.class);
        startActivity(intent);
        //Bundle bundle = new Bundle();
    }

    public void openUser(View view)
    {
        Intent intent = new Intent(this, AddUser.class);
        startActivity(intent);
    }

    private void addData(Intent x, Products f) {
        prodKey = "key";
        Log.d(TAG, "adding Data to details");

    }

    private void clickOnList()
    {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                try
                {
                    Toast.makeText(MainActivity.this, "Product: " + listView.getItemAtPosition(position), Toast.LENGTH_SHORT).show();
                    final Products f = new Products();
                    final Intent appInfo = new Intent(MainActivity.this, ProductDetails.class);
                    f.setProdName(listView.getItemAtPosition(position).toString());
                    Log.i(TAG, "what is string details: " + listItems2.get(position));
                    f.setProdDetails(listItems2.get(position));
                    Log.i(TAG, "DREF: " + dref.child("products").child("prodDetails"));
                    Log.i(TAG, "f.getProdName is: " + f.getProdName());
                    appInfo.putExtra(prodKey, f.getProdName().toString());
                    appInfo.putExtra(prodKey2, f.getProdDetails().toString());
                    startActivity(appInfo);
                }
                catch (Exception e)
                {
                    Log.i(TAG, "Opening Product Details error" + e );
                }
            }
        });
    }
}


