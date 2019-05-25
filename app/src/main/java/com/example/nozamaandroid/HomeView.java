package com.example.nozamaandroid;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.ObservableList;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
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
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nozamaandroid.Adaptor.AdaptorProduct;
import com.example.nozamaandroid.BLL.BLLProducts;
import com.example.nozamaandroid.BLL.BLLUser;
import com.example.nozamaandroid.Models.CartModel;
import com.example.nozamaandroid.Models.Products;
import com.example.nozamaandroid.Models.Users;
import com.example.nozamaandroid.Shared.CameraModel;
import com.example.nozamaandroid.Shared.OnResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class HomeView extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    static CartModel cartModel = CartModel.getInstance();
    Products products = new Products();
    AdaptorProduct adapterProduct;
    private static final int PERMISSION_REQUEST_CODE = 1;
    Intent intent;
    /*----------------BLL----------------*/
    BLLUser bllUser = new BLLUser();
    BLLProducts bllProducts = new BLLProducts();
    /*----------------Arrays----------------*/
    ArrayList<Products> productsArrayList;
    ArrayList<Products> filteredSearchBoxArrayList = new ArrayList<>();
    ArrayList<Products> filteredListForUsing;
    /*----------------View items----------------*/
    ActionBarDrawerToggle toggle;
    EditText etSearchBar;
    TextView tvCartCount, tvUsername;
    ImageView ivHeaderUsername;
    NavigationView navigationView;
    MenuItem menuItemLogin;
    MenuItem menuItemAccount;
    NavigationView mNavigationView;
    DrawerLayout drawer;
    Toolbar toolbar;
    ImageButton imageButton;
    GridView gridViewProduct;
    FloatingActionButton addButton;
    /*----------------Firebase----------------*/
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();
    /*----------------Strings----------------*/
    String TAG = "HomeView";
    String Keyword;
    String productKey = "productKey";
    String[] options = new String[]{"Show detail", "Add to cart"};
    String[] contactOptions = new String[]{"Make a call", "Send an Email"};
    private String serviceEmail = "service@gmail.com";
    private String servicePhone = "42746155";
    CameraModel cameraModel = CameraModel.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_view2);
        askPremision();
        Log.d(TAG, "View has been set. Lets setup the items");
        setupItems();
        setupSideNavBar();
        getProductsFromDatabase();

        //Set the adapter to the main list view
        adapterProduct = new AdaptorProduct(HomeView.this, productsArrayList);
        gridViewProduct.setAdapter(adapterProduct);
        etSearchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //Nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                Keyword = s.toString().toLowerCase();
                filteredSearchBoxArrayList.clear();
                if (!filteredListForUsing.isEmpty()) {
                    for (Products product : filteredListForUsing) {
                        if (product.getProdName().toLowerCase().contains(Keyword))
                            filteredSearchBoxArrayList.add(product);
                        adapterProduct = new AdaptorProduct(HomeView.this, filteredSearchBoxArrayList);
                        gridViewProduct.setAdapter(adapterProduct);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                //Nothing
            }
        });
        clickOnList();

        cartModel.cartList.addOnListChangedCallback(new ObservableList.OnListChangedCallback<ObservableList<Products>>() {
            @Override
            public void onChanged(ObservableList<Products> sender) { //Nothing
            }

            @Override
            public void onItemRangeChanged(ObservableList<Products> sender, int positionStart, int itemCount) { //Nothing

            }

            @Override
            public void onItemRangeInserted(ObservableList<Products> sender, int positionStart, int itemCount) {
                tvCartCount.setVisibility(View.VISIBLE);
                tvCartCount.setText(cartModel.getProductInCart().size() + "");
            }

            @Override
            public void onItemRangeMoved(ObservableList<Products> sender, int fromPosition, int toPosition, int itemCount) { //Nothing

            }

            @Override
            public void onItemRangeRemoved(ObservableList<Products> sender, int positionStart, int itemCount) {
                if (cartModel.getProductInCart().size() == 0)
                    tvCartCount.setText(cartModel.getProductInCart().size() + "");
                else
                    tvCartCount.setVisibility(View.INVISIBLE);
            }
        });

    }


    public void openAddProductView(View view) {
        if (currentUser != null) {
            intent = new Intent(this, AddProduct.class);
            startActivity(intent);
        } else
            Toast.makeText(this, "Please, log in first to access this functionality.", Toast.LENGTH_LONG).show();
    }

    public void loginView() {
        intent = new Intent(HomeView.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void openCreateUser() {
        cameraModel.changePreImage(null);
        intent = new Intent(this, UserCreation.class);
        startActivity(intent);
        finish();
    }

    private void clickOnList() {
        Log.d(TAG, "clickOnList: ");
        gridViewProduct.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(HomeView.this);
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    /*
                     * Give the user an option to either choose add to the cart
                     * or open the products detail.
                     * */
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (options[which].equals(options[0])) {
                            try {
                                // Here we want to initiate the product class so we can pass in data between views, it also gets the
                                // It also gets the position in the listview by using onitemclick adapter view click listener, which has a built in position
                                // which we can find.  I have made several logs and some toasts to help me to see if I would get the correct values.

                                Toast.makeText(HomeView.this, "Product: " + gridViewProduct.getItemAtPosition(position), Toast.LENGTH_SHORT).show();

                                intent = new Intent(HomeView.this, ProductDetails.class);
                                intent.putExtra(productKey, productsArrayList.get(position));
                                startActivity(intent);
                                Log.d(TAG, "Opening detail activity");
                            } catch (Exception e) {
                                Log.d(TAG, "Opening Product Details error" + e);
                            }
                        }
                        if (options[which].equals(options[1])) {
                            products = productsArrayList.get(position);
                            Log.d(TAG, "onClick: " + products.getProdName());
                            cartModel.checkIfProductId(products, HomeView.this);

                        }
                    }
                });
                builder.show();

            }


        });
    }

    public void loginView() {
        intent = new Intent(HomeView.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void setupSideNavBar() {
        Log.d(TAG, "Setting up the side navigation bar");
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Open cart view
                intent = new Intent(HomeView.this, CartView.class);
                startActivity(intent);
                Toast.makeText(HomeView.this, "Opening cart view", Toast.LENGTH_SHORT);
            }
        });
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

    }

    private ArrayList<Products> getProductsFromDatabase() {
        productsArrayList = bllProducts.readProductsFromDatabase();
        return filteredListForUsing = productsArrayList;
    }

    private void setupItems() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        imageButton = findViewById(R.id.imageButton);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        tvCartCount = findViewById(R.id.countCartSize);
        etSearchBar = findViewById(R.id.searchBox);
        tvUsername = findViewById(R.id.currentUserName);
        addButton = findViewById(R.id.addProductButton);
        gridViewProduct = findViewById(R.id.gridview_product);
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        ivHeaderUsername = (ImageView) mNavigationView.getHeaderView(0).findViewById(R.id.userHomeImageView);
        tvUsername = (TextView) mNavigationView.getHeaderView(0).findViewById(R.id.currentUserName);
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

        switch (id) {
            case R.id.nav_home:
                adapterProduct = new AdaptorProduct(HomeView.this, productsArrayList);
                gridViewProduct.setAdapter(adapterProduct);
                break;
            case R.id.nav_books:
                filteredListForUsing = new ArrayList<>();
                for (Products products : productsArrayList) {
                    if (products.getCategory().equals("Books"))
                        filteredListForUsing.add(products);
                }
                adapterProduct = new AdaptorProduct(HomeView.this, filteredListForUsing);
                gridViewProduct.setAdapter(adapterProduct);
                break;
            case R.id.nav_clothes:
                filteredListForUsing = new ArrayList<>();
                for (Products products : productsArrayList) {
                    if (products.getCategory().equals("Clothes"))
                        filteredListForUsing.add(products);
                }
                adapterProduct = new AdaptorProduct(HomeView.this, filteredListForUsing);
                gridViewProduct.setAdapter(adapterProduct);
                break;
            case R.id.nav_decoration:
                filteredListForUsing = new ArrayList<>();
                for (Products products : productsArrayList) {
                    if (products.getCategory().equals("Decoration"))
                        filteredListForUsing.add(products);
                }
                adapterProduct = new AdaptorProduct(HomeView.this, filteredListForUsing);
                gridViewProduct.setAdapter(adapterProduct);
                break;
            case R.id.nav_electrical_appliance:
                filteredListForUsing = new ArrayList<>();
                for (Products products : productsArrayList) {
                    if (products.getCategory().equals("Electrical Appliance"))
                        filteredListForUsing.add(products);
                }
                adapterProduct = new AdaptorProduct(HomeView.this, filteredListForUsing);
                gridViewProduct.setAdapter(adapterProduct);
                break;
            case R.id.nav_account:
                if (currentUser == null) {
                    openCreateUser();
                } else {
                    openAccountDetails();
                }
                break;
            case R.id.nav_log_out:
                if (currentUser == null) {
                    loginView();
                } else {
                    currentUser = null;
                    FirebaseAuth.getInstance().signOut();
                    getMenuItem();
                    menuItemLogin.setTitle("Login");
                    menuItemAccount.setTitle("Create an account");
                    Toast.makeText(this, "You have logged out, thank you and please come again. :-)", Toast.LENGTH_LONG).show();
                    ivHeaderUsername.setImageResource(R.drawable.bag_icon);
                    tvUsername.setText(R.string.guest);

                }

                break;
            case R.id.nav_contact:
                final AlertDialog.Builder contactBuilder = new AlertDialog.Builder(HomeView.this);
                contactBuilder.setItems(contactOptions, new DialogInterface.OnClickListener() {
                    /*
                     * Give the user an option to either choose call the services
                     * or send an email.
                     * */
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (contactOptions[which].equals(contactOptions[0])) {
                            //Call
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {

                                if (checkSelfPermission(android.Manifest.permission.CALL_PHONE)
                                        == PackageManager.PERMISSION_DENIED) {

                                    Log.d(TAG, "permission denied to CALL - requesting it");
                                    String[] permissions = {android.Manifest.permission.CALL_PHONE};

                                    requestPermissions(permissions, PERMISSION_REQUEST_CODE);
                                    return;

                                } else {
                                    Log.d(TAG, "permission to CALL granted!");
                                }
                            }
                            makeCall();
                        }
                        if (contactOptions[which].equals(contactOptions[1])) {
                            sendEmail();
                        }
                    }
                });
                contactBuilder.show();
                break;
        }

        drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void makeCall() {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel: " + servicePhone));
        Log.d(TAG, "Calling permission: " + intent);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        startActivity(intent);
    }

    private void sendEmail() {
        Log.d(TAG, "Email: " + serviceEmail);
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("plain/text");
        String[] receivers = {serviceEmail};
        emailIntent.putExtra(Intent.EXTRA_EMAIL, receivers);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "");
        startActivity(emailIntent);
    }

    private void openAccountDetails() {
        intent = new Intent(this, UserAccountDetails.class);
        startActivity(intent);
        finish();
    }

    private void getMenuItem() {
        menuItemLogin = navigationView.getMenu().findItem(R.id.nav_log_out);
        menuItemAccount = navigationView.getMenu().findItem(R.id.nav_account);

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        Log.d(TAG, "onStart: " + menuItemLogin);
        getMenuItem();
        if (currentUser == null) {
            Log.d(TAG, "No one is logged in");
            Toast.makeText(this, "You are currently not logged in.", Toast.LENGTH_SHORT).show();
            menuItemLogin.setTitle("Login");
            menuItemAccount.setTitle("Create an account");
        } else {
            Log.d(TAG, "Getting user information");
            String currentUserId = mAuth.getCurrentUser().getUid();
            NavigationView mNavigationView = (NavigationView) findViewById(R.id.nav_view);


            //tvUsername.setText(bllUser.getUserInfo(currentUserId).getUserName());
            bllUser.getImageById(currentUserId, new OnResponse() {
                @Override
                public void onResponseReceived(Object response) {
                    if (response != null) {
                        ivHeaderUsername.setImageBitmap((Bitmap) response);
                    } else {
                        ivHeaderUsername.setImageResource(R.drawable.cake);
                    }
                }
            });
            bllUser.getUserById(currentUserId, new OnResponse() {
                @Override
                public void onResponseReceived(Object response) {
                    if (response != null) {
                        Users user = (Users) response;
                        tvUsername.setText(user.getUserName());
                    }
                }
            });

            menuItemLogin.setTitle("Logout");
            menuItemAccount.setTitle("Account details");
        }
    }

    private void askPremision() {
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
        }
    }


}
