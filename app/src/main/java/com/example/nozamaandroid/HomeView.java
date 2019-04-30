package com.example.nozamaandroid;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.nozamaandroid.Adaptor.AdaptorProduct;
import com.example.nozamaandroid.Cart.CartView;
import com.example.nozamaandroid.DALProducts.AddProduct;
import com.example.nozamaandroid.DALUsers.AddUser;
import com.example.nozamaandroid.Models.Products;
import com.example.nozamaandroid.Models.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HomeView extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static String TAG = "ProductApp";
    Products products;
    Toolbar toolbar;
    Context context;
    ImageButton imageButton;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawer;
    NavigationView navigationView;

    ArrayList<String> listItemName = new ArrayList<>();
    ArrayList<String> listItemDetail = new ArrayList<>();
    ArrayList<String> listItemId = new ArrayList<>();

    ListView listView;
    String nameKey = "nameKey";
    String detailKey = "detailKey";
    String idKey = "idKey";
    String userKey = "userKey", passwordKey = "passwordKey";
    DatabaseReference dref;
    ArrayAdapter<String> adapter;
    public String details;
    FirebaseFirestore db;

    Map<String, Object> productMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        products = new Products();
        productMap = new HashMap<>();
        setContentView(R.layout.activity_home_view2);
        Log.d(TAG,"View has been setted. Lets setup the items");
        setupItems();
        //Side nav bar code
        setupSideNavBar();
        //Firebase code
        setupDataBase();


        clickOnList();

        getUser();
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
                Log.d(TAG, "onItemClick: "+view);
                try
                {
                    // Here we want to initiate the product class so we can pass in data between views, it also gets the
                    // It also gets the position in the listview by using onitemclick adapter view click listener, which has a built in position
                    // which we can find.  I have made several logs and some toasts to help me to see if I would get the correct values.

                    Toast.makeText(HomeView.this, "Product: " + listView.getItemAtPosition(position), Toast.LENGTH_SHORT).show();
                    final Products f = new Products();
                    final Intent appInfo = new Intent(HomeView.this, ProductDetails.class);
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

    private void getUser()
    {
        Users user = new Users();

        try {
            String getUser = getIntent().getExtras().getString(userKey, user.getUserName());
            String getPassword = getIntent().getExtras().getString(passwordKey, user.getPassword());

            Log.d(TAG, "getUser: " + getUser + " Password:" + getPassword);
            Toast.makeText(this, "You are logged in as: " + getUser, Toast.LENGTH_SHORT).show();
        }
        catch(Exception e)
        {
            Log.d(TAG, "Exception with getting user: " + e);
            Toast.makeText(this, "No one is currently logged in, please login", Toast.LENGTH_SHORT).show();
        }
    }

    public void loginView(View view)
    {
        Intent intent = new Intent(HomeView.this, LoginActivity.class);
        startActivity(intent);
    }

    private void setupSideNavBar() {
        Log.d(TAG,"Setting up xthe side navigation bar");
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Open cart view
                Intent intent = new Intent(HomeView.this, CartView.class);
                startActivity(intent);
                Snackbar.make(view, "Open cart view", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

    }

    private void setupDataBase() {
        Log.d(TAG,"Setting up the database settings");
        db = FirebaseFirestore.getInstance();
        db.collection("products")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<Products> productsArrayList=  new ArrayList<Products>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String getFireStoreFieldName = document.getString("Product Name");
                                String getFireStoreFieldDetails = document.getString("Product Details");
                                String getFireStoreId = document.getId();
                                Products products = new Products();
                                products.setProdName(getFireStoreFieldName);
                                products.setProdId(getFireStoreId);
                                productsArrayList.add(products);

                                Log.d(TAG, document.getId() + " => " + document.getData());
                                Log.i(TAG, "What is value: " + getFireStoreFieldName);
                                listItemName.add(getFireStoreFieldName);
                                listItemDetail.add(getFireStoreFieldDetails);
                                listItemId.add(getFireStoreId);
                            }
                          /*  ArrayAdapter<String> adapter = new ArrayAdapter<String>(HomeView.this,android.R.layout.simple_dropdown_item_1line,listItemName);
                            listView.setAdapter(adapter);*/
                            Log.d(TAG, "onComplete: "+productsArrayList);
                            AdaptorProduct adapterProduct = new AdaptorProduct(HomeView.this, productsArrayList );
                            listView.setAdapter(adapterProduct);
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    private void setupItems(){
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        imageButton = findViewById(R.id.imageButton);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        listView = findViewById(R.id.synchronizeProducts);
    }
    @Override
    public void onBackPressed() {
        drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id){
            case R.id.nav_books:
                break;
            case R.id.nav_clothes:
                break;
            case R.id.nav_decoration:
                break;
            case R.id.nav_electrical_appliance:
                break;
            case R.id.nav_account:
                break;
            case R.id.nav_log_out:
                break;
        }

        drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
