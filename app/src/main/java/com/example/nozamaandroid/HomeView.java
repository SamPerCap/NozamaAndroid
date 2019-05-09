package com.example.nozamaandroid;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nozamaandroid.Adaptor.AdaptorProduct;
import com.example.nozamaandroid.Cart.CartView;
import com.example.nozamaandroid.DALProducts.AddProduct;
import com.example.nozamaandroid.DALUsers.AddUser;
import com.example.nozamaandroid.Models.CartModel;
import com.example.nozamaandroid.Models.Products;
import com.example.nozamaandroid.Models.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.collection.LLRBNode;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class HomeView extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    CartModel cartModel;
    public static String TAG = "ProductApp";
    Products products;
    Toolbar toolbar;
    Context context;
    ImageButton imageButton;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawer;
    NavigationView navigationView;
    ArrayList<Products> productsArrayList =  new ArrayList<Products>();;
    ArrayList<String> listItemName = new ArrayList<>();
    ArrayList<String> listItemDetail = new ArrayList<>();
    ArrayList<String> listItemId = new ArrayList<>();
    ArrayList<Products> filteredArrayList = new ArrayList<>();
    EditText searchBar;
    String[] options = new String[]{"Show detail",
        "Add to cart"};
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    ListView listView;
    String nameKey = "nameKey";
    RatingBar ratingBar;
    String detailKey = "detailKey";
    String idKey = "idKey";
    String Keyword;
    String userKey = "userKey", passwordKey = "passwordKey", addressKey = "addressKey";
    DatabaseReference dref;
    AdaptorProduct adapterProduct;
    public String details;
    FirebaseFirestore db;
    Map<String, Object> productMap;
    TextView cartCount;
    MenuItem menuItemLogin;
    MenuItem menuItemAccount;
    private static final int PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        products = new Products();
        productMap = new HashMap<>();
        setContentView(R.layout.activity_home_view2);
        Log.d(TAG,"View has been setted. Lets setup the items");
        cartModel = CartModel.getInstance();
        setupItems();
        //Side nav bar code
        setupSideNavBar();
        //Firebase code
        setupDataBase();
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                Keyword = s.toString().toLowerCase();
                filteredArrayList.clear();
                for(Products product: productsArrayList){
                    if(product.getProdName().toLowerCase().contains(Keyword))
                        filteredArrayList.add(product);
                    adapterProduct = new AdaptorProduct(HomeView.this, filteredArrayList);
                    listView.setAdapter(adapterProduct);
                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        askPremision();

        getUser();
        clickOnList();
        mAuth = FirebaseAuth.getInstance();
    }

    public void openUserView(View view)
    {
        Intent intent = new Intent(this, AddProduct.class);
        startActivity(intent);
        //Bundle bundle = new Bundle();
    }

    public void openCreateUser()
    {
        Intent intent = new Intent(this, AddUser.class);
        startActivity(intent);
    }

    private void clickOnList()
    {
        Log.d(TAG, "clickOnList: ");
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(HomeView.this);
                builder.setItems(options, new DialogInterface.OnClickListener()
                {
                    /*
                     * Give the user an option to either choose an image that already exists on the phone
                     * or to take a picture
                     * */
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        if(options[which].equals(options[0]))
                        {
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
                                appInfo.putExtra(nameKey, productsArrayList.get(position).getProdName());
                                appInfo.putExtra(detailKey, productsArrayList.get(position).getProdDetails());
                                appInfo.putExtra(idKey, productsArrayList.get(position).getProdId());
                                startActivity(appInfo);
                                Log.d(TAG, "cart size: "+ cartModel.getProductInCart().size());

                            }
                            catch (Exception e)
                            {
                                Log.i(TAG, "Opening Product Details error" + e );
                            }
                        }
                        if(options[which].equals(options[1]))
                        {
                            Products product = productsArrayList.get(position);
                            product.setAmount(1);
                            Log.d(TAG, "onClick: "+ product.getProdName());


                           cartModel.addProductToCart(product);
                            cartCount.setVisibility(View.VISIBLE);
                           cartCount.setText(cartModel.getProductInCart().size()+"");
                        }
                    }
                });
                builder.show();

            }
        });
    }

    private void getUser()
    {
        Users user = new Users();

        try {
            String getUser = getIntent().getExtras().getString(userKey, user.getEmail());
            String getPassword = getIntent().getExtras().getString(passwordKey, user.getPassword());
            String getAddress = getIntent().getExtras().getString(addressKey, user.getAddress());
            Log.d(TAG, "getUser: " + getUser + " Password:" + getPassword + " address: " + getAddress);
            Toast.makeText(this, "You are logged in as: " + getUser, Toast.LENGTH_SHORT).show();
        }
        catch(Exception e)
        {
            Log.d(TAG, "Exception with getting user: " + e);
            Toast.makeText(this, "No one is currently logged in, please login", Toast.LENGTH_SHORT).show();
        }
    }

    public void loginView()
    {
        Intent intent = new Intent(HomeView.this, LoginActivity.class);
        startActivity(intent);
    }

    public void setupSideNavBar()
    {
        Log.d(TAG,"Setting up the side navigation bar");
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

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String getFireStoreFieldName = document.getString("Product Name");
                                String getFireStoreFieldDetails = document.getString("Product Details");
                                String getFireStoreId = document.getId();
                                Products products = new Products();
                                products.setProdName(getFireStoreFieldName);
                                products.setProdDetails(getFireStoreFieldDetails);
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
                            adapterProduct = new AdaptorProduct(HomeView.this, productsArrayList );
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
        cartCount = findViewById(R.id.countCartSize);
        searchBar = findViewById(R.id.searchBox);

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
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id)
        {
            case R.id.nav_books:
                break;
            case R.id.nav_clothes:
                break;
            case R.id.nav_decoration:
                break;
            case R.id.nav_electrical_appliance:
                break;
            case R.id.nav_account:
                if ( currentUser == null )
                {
                    openCreateUser();
                }
                else
                {
                    openAccountDetails();
                }
                break;
            case R.id.nav_log_out:
                if ( currentUser == null )
                {
                    loginView();
                }
                else
                {
                    currentUser = null;
                    FirebaseAuth.getInstance().signOut();
                    getMenuItem();
                    menuItemLogin.setTitle("Login");
                    menuItemAccount.setTitle("Create an account");
                    Toast.makeText(this, "You have logged out, thank you and please come again. :-)", Toast.LENGTH_LONG).show();

                    TextView textView = findViewById(R.id.currentUserName);
                    textView.setText("Guest");
                }

                break;
        }

        drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void openAccountDetails()
    {
        Intent intent = new Intent(this, UserAccountDetails.class);
        startActivity(intent);
    }

    private void getMenuItem()
    {
        menuItemLogin = navigationView.getMenu().findItem(R.id.nav_log_out);
        menuItemAccount = navigationView.getMenu().findItem(R.id.nav_account);
    }

    @Override
    public void onStart() {
        super.onStart();
        db = FirebaseFirestore.getInstance();
        // Check if user is signed in (non-null) and update UI accordingly.
        currentUser = mAuth.getCurrentUser();
        getMenuItem();

        if ( currentUser == null)
        {
            Log.d(TAG, "No one is logged in");
            Toast.makeText(this, "You are currently not logged in.", Toast.LENGTH_SHORT).show();
            menuItemLogin.setTitle("Login");
            menuItemAccount.setTitle("Create an account");
        }
        else
        {
            getUserFirestore();
            Log.d(TAG, "Who is the current user: " + currentUser.getEmail());
            Toast.makeText(this, currentUser.getEmail() + " is currently logged in.", Toast.LENGTH_SHORT).show();
            menuItemLogin.setTitle("Logout");
            menuItemAccount.setTitle("Account details");
        }
    }

    private void getUserFirestore()
    {
        db = FirebaseFirestore.getInstance();
        currentUser = mAuth.getCurrentUser();
        Log.d(TAG,"user id; " + currentUser.getUid());
        DocumentReference docRef = db.collection("users").document(currentUser.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        // This way we can get the fields from firestore and save them to a string.
                        String getFireStoreFieldUserName = document.getString("Username");
                        TextView textView = findViewById(R.id.currentUserName);
                        textView.setText("User: " + getFireStoreFieldUserName);
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    private void askPremision()
    {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {


            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_DENIED
                    || checkSelfPermission(Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_DENIED
                    || checkSelfPermission(Manifest.permission.SEND_SMS)
                    == PackageManager.PERMISSION_DENIED
                    || checkSelfPermission(Manifest.permission.CALL_PHONE)
                    == PackageManager.PERMISSION_DENIED
                    || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_DENIED
                    || checkSelfPermission(Manifest.permission.INTERNET)
                    == PackageManager.PERMISSION_DENIED
                    || checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_DENIED
                    || checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                    == PackageManager.PERMISSION_DENIED
                    || checkSelfPermission(Manifest.permission.ACCESS_NETWORK_STATE)
                    == PackageManager.PERMISSION_DENIED
                    || checkSelfPermission(Manifest.permission.READ_CONTACTS)
                    == PackageManager.PERMISSION_DENIED) {

                Log.d(TAG, "permission denied to CAMERA - requesting it");
                String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE
                        , Manifest.permission.CAMERA
                        , Manifest.permission.SEND_SMS
                        , Manifest.permission.CALL_PHONE
                        , Manifest.permission.WRITE_EXTERNAL_STORAGE
                        , Manifest.permission.INTERNET
                        , Manifest.permission.ACCESS_FINE_LOCATION
                        , Manifest.permission.ACCESS_COARSE_LOCATION
                        , Manifest.permission.ACCESS_NETWORK_STATE
                        , Manifest.permission.READ_CONTACTS};

                requestPermissions(permissions, PERMISSION_REQUEST_CODE);
            }
        }}
}
