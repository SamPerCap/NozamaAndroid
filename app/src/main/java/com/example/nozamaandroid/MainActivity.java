package com.example.nozamaandroid;

import android.app.ListActivity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.nozamaandroid.DALProducts.AddProduct;
import com.example.nozamaandroid.DALUsers.AddUser;
import com.example.nozamaandroid.Models.ProductsMockData;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{
    public static String TAG = "ProductApp";

    ArrayList<String> listItems = new ArrayList<>();
    ListView listView;
    ProductsMockData mockData;

    ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Needs to initialize before creating an instance (Should be onCreate)
        FirebaseApp.initializeApp(this);
        DatabaseReference dref;
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
}


