package com.example.nozamaandroid;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity
{
    public static String TAG = "ProductApp";
    Products f = new Products();
    Context context;

    ArrayList<String> listItemName = new ArrayList<>();
    ArrayList<String> listItemDetail = new ArrayList<>();
    ArrayList<String> listItemId = new ArrayList<>();

    Products products;
    ListView listView;
    String nameKey = "nameKey";
    String detailKey = "detailKey";
    String idKey = "idKey";
    DatabaseReference dref;
    ArrayAdapter<String> adapter;
    public String details;

    Map<String, Object> productMap = new HashMap<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.synchronizeProducts);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        listView = findViewById(R.id.synchronizeProducts);

        this.setTitle("NozamaGo");

        db.collection("products")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String getFireStoreFieldName = document.getString("Product Name");
                                String getFireStoreFieldDetails = document.getString("Product Details");
                                String getFireStoreId = document.getId();

                                Log.d(TAG, document.getId() + " => " + document.getData());
                                Log.i(TAG, "What is value: " + getFireStoreFieldName);
                                listItemName.add(getFireStoreFieldName);
                                listItemDetail.add(getFireStoreFieldDetails);
                                listItemId.add(getFireStoreId);
                            }
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_dropdown_item_1line,listItemName);
                            listView.setAdapter(adapter);
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

        // Old code for the FireBase Database, it's a little different from FireStore, since it uses snapshots
            // It's a little bit more complicated to use, but the same idea applies.

        /*dref=FirebaseDatabase.getInstance().getReference();
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
*/
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

    private void clickOnList()
    {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                try
                {
                    // Here we want to initiate the product class so we can pass in data between views, it also gets the
                    // It also gets the position in the listview by using onitemclick adapter view click listener, which has a built in position
                    // which we can find.  I have made several logs and some toasts to help me to see if I would get the correct values.

                    Toast.makeText(MainActivity.this, "Product: " + listView.getItemAtPosition(position), Toast.LENGTH_SHORT).show();
                    final Products f = new Products();
                    final Intent appInfo = new Intent(MainActivity.this, ProductDetails.class);
                    f.setProdName(listView.getItemAtPosition(position).toString());
                    Log.i(TAG, "what is string details: " + listItemDetail.get(position));
                    f.setProdDetails(listItemDetail.get(position));
                    f.setProdId(listItemId.get(position));
                    //Log.i(TAG, "DREF: " + dref.child("products").child("prodDetails"));
                    Log.i(TAG, "f.getProdName is: " + f.getProdName());
                    appInfo.putExtra(nameKey, f.getProdName().toString());
                    appInfo.putExtra(detailKey, f.getProdDetails().toString());
                    appInfo.putExtra(idKey, f.getProdId());
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


