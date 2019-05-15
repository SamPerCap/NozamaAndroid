package com.example.nozamaandroid;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.ObservableList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nozamaandroid.Adaptor.AdaptorProduct;
import com.example.nozamaandroid.BLL.BLLProducts;
import com.example.nozamaandroid.DAL.DALProduct;
import com.example.nozamaandroid.DAL.DALUser;
import com.example.nozamaandroid.Models.CartModel;
import com.example.nozamaandroid.Models.Products;
import com.example.nozamaandroid.Models.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class HomeView extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    static CartModel cartModel = CartModel.getInstance();
    Products products = new Products();
    AdaptorProduct adapterProduct;
    private static final int PERMISSION_REQUEST_CODE = 1;
    Intent intent;
    ActionBarDrawerToggle toggle;
    DocumentReference docRef;
    BLLProducts bllProducts = new BLLProducts();
    /*----------------Arrays----------------*/
    ArrayList<Products> productsArrayList;
    ArrayList<Products> filteredArrayList = new ArrayList<>();
    /*----------------View items----------------*/
    EditText searchBar;
    ListView listView;
    TextView cartCount;
    NavigationView navigationView;
    MenuItem menuItemLogin;
    MenuItem menuItemAccount;
    DrawerLayout drawer;
    Toolbar toolbar;
    ImageButton imageButton;
    /*----------------Firebase----------------*/
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    StorageReference mStorageRef;
    /*----------------Strings----------------*/
    String TAG = "HomeView";
    String Keyword;
    String userKey = "userKey", passwordKey = "passwordKey", addressKey = "addressKey",
            productKey = "productKey", userImgId = "userImgId";
    String[] options = new String[]{"Show detail", "Add to cart"};
    String getUserImgId;

    GridView gridViewProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_view2);
        askPremision();
        Log.d(TAG, "View has been set. Lets setup the items");
        setupItems();
        setupSideNavBar();
        getProductsFromDatabase();
        gridViewProduct = findViewById(R.id.gridview_product);

        //Set the adapter to the main list view
        adapterProduct = new AdaptorProduct(HomeView.this, productsArrayList);
        gridViewProduct.setAdapter(adapterProduct);
        // listView.setAdapter(adapterProduct);
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //Nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                Keyword = s.toString().toLowerCase();
                filteredArrayList.clear();
                for (Products product : productsArrayList) {
                    if (product.getProdName().toLowerCase().contains(Keyword))
                        filteredArrayList.add(product);
                    adapterProduct = new AdaptorProduct(HomeView.this, filteredArrayList);
                    listView.setAdapter(adapterProduct);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                //Nothing
            }
        });
        getUser();
        clickOnList();

        //cartCount.setText(cartModel.getProductInCart().size() + "");
        cartModel.cartList.addOnListChangedCallback(new ObservableList.OnListChangedCallback<ObservableList<Products>>() {
            @Override
            public void onChanged(ObservableList<Products> sender) { //Nothing
            }

            @Override
            public void onItemRangeChanged(ObservableList<Products> sender, int positionStart, int itemCount) { //Nothing

            }

            @Override
            public void onItemRangeInserted(ObservableList<Products> sender, int positionStart, int itemCount) {
                cartCount.setVisibility(View.VISIBLE);
                cartCount.setText(cartModel.getProductInCart().size() + "");
            }

            @Override
            public void onItemRangeMoved(ObservableList<Products> sender, int fromPosition, int toPosition, int itemCount) { //Nothing

            }

            @Override
            public void onItemRangeRemoved(ObservableList<Products> sender, int positionStart, int itemCount) {
                if (cartModel.getProductInCart().size() == 0)
                    cartCount.setText(cartModel.getProductInCart().size() + "");
                else
                    cartCount.setVisibility(View.INVISIBLE);
            }
        });
    }

    public void openUserView(View view) {
        intent = new Intent(this, DALProduct.class);
        startActivity(intent);
    }

    public void openCreateUser() {
        intent = new Intent(this, DALUser.class);
        startActivity(intent);
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
                                /*Products currentProducts =
                                currentProducts.setProdId(productsArrayList.get(position).getProdId());
                                currentProducts.setProdDetails(productsArrayList.get(position).getProdDetails());
                                currentProducts.setProdName(productsArrayList.get(position).getProdName());
                                currentProducts.setPictureId(productsArrayList.get(position).getPictureId());*/
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
                            // cartModel.addProductToCart(products);

                        }
                    }
                });
                builder.show();

            }


        });
    }


    private void getUser() {
        Users user = new Users();

        try {
            String getUser = getIntent().getExtras().getString(userKey, user.getEmail());
            String getPassword = getIntent().getExtras().getString(passwordKey, user.getPassword());
            String getAddress = getIntent().getExtras().getString(addressKey, user.getAddress());
            getUserImgId = getIntent().getExtras().getString(userImgId, user.getImgId());
            Log.d(TAG, "getUser: " + getUser + " Password:" + getPassword + " address: " + getAddress);
            Toast.makeText(this, "You are logged in as: " + getUser, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.d(TAG, "Exception with getting user: " + e);
            Toast.makeText(this, "No one is currently logged in, please login", Toast.LENGTH_SHORT).show();
        }
    }

    public void loginView() {
        intent = new Intent(HomeView.this, LoginActivity.class);
        startActivity(intent);
    }

    public void setupSideNavBar() {
        Log.d(TAG, "Setting up the side navigation bar");
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Open cart view
                intent = new Intent(HomeView.this, CartView.class);
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

    private ArrayList<Products> getProductsFromDatabase() {
        return productsArrayList = bllProducts.readProductsFromDatabase();
    }

    private void setupItems() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        imageButton = findViewById(R.id.imageButton);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        //listView = findViewById(R.id.synchronizeProducts);
        cartCount = findViewById(R.id.countCartSize);
        searchBar = findViewById(R.id.searchBox);
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
            case R.id.nav_books:
                break;
            case R.id.nav_clothes:
                break;
            case R.id.nav_decoration:
                break;
            case R.id.nav_electrical_appliance:
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

                    TextView textView = findViewById(R.id.currentUserName);
                    textView.setText("Guest");
                }

                break;
        }

        drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void openAccountDetails() {
        intent = new Intent(this, UserAccountDetails.class);
        startActivity(intent);
    }

    private void getMenuItem() {
        menuItemLogin = navigationView.getMenu().findItem(R.id.nav_log_out);
        menuItemAccount = navigationView.getMenu().findItem(R.id.nav_account);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.

        getMenuItem();

        if (currentUser == null) {
            Log.d(TAG, "No one is logged in");
            Toast.makeText(this, "You are currently not logged in.", Toast.LENGTH_SHORT).show();
            menuItemLogin.setTitle("Login");
            menuItemAccount.setTitle("Create an account");
        } else {
            getUserFirestore();
            getUserImageFromStorage();
            Log.d(TAG, "Who is the current user: " + currentUser.getEmail());
            Toast.makeText(this, currentUser.getEmail() + " is currently logged in.", Toast.LENGTH_SHORT).show();
            menuItemLogin.setTitle("Logout");
            menuItemAccount.setTitle("Account details");
        }
    }

    private void getUserFirestore() {
        Log.d(TAG, "user id; " + currentUser.getUid());
        docRef = db.collection("users").document(currentUser.getUid());
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

    private void getUserImageFromStorage() {

        Log.d(TAG, "current userID: " + currentUser.getUid());
        mStorageRef = FirebaseStorage.getInstance().getReference();
        mStorageRef.child("user-images/" + currentUser.getUid()).
                getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                // Use the bytes to display the image
                Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                ImageView imgView = findViewById(R.id.userHomeImageView);
                imgView.setImageBitmap(bm);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                ImageView imgView = findViewById(R.id.userHomeImageView);
                imgView.setImageResource(R.drawable.cake);
                Log.d(TAG, "Error with getting the current user image: " + exception);
            }
        });
    }

}
