package com.example.nozamaandroid;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nozamaandroid.Models.CartModel;
import com.example.nozamaandroid.Models.Products;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;
public class ProductDetails extends AppCompatActivity
{
    TextView productName, productDetail;
    String TAG = "Product Details class";
    String prodKey = "productKey";
    String prodNameData;
    Products products = new Products();
    FirebaseFirestore db;
    DocumentReference docRef;
    Products currentProduct;
    String prodDetailData;
    String document;
    String prodIdData;
    RatingBar prodRating;
    Button saveRatingBtn;
    Bundle bundle;
    CartModel cartModel = HomeView.cartModel;

    // We need to create an instance of the product class so we can use
    // the getters where we saved the prodNameData from mainactivity


    Map<String, Object> ratingMap = new HashMap<>();
    Map<String, Object> prodIdMap = new HashMap<>();


    @Override
    protected void onCreate(Bundle saveInstance) {
        super.onCreate(saveInstance);
        setContentView(R.layout.product_details);
        db = FirebaseFirestore.getInstance();

        // save the id of the buttons to the button class variable so we can use that later and interact with it
        setupItems();


        // Here we grab the intent from main activity which saved it to the model BE product entity, so we should
        // be able to display it on the text field
        /*
        prodNameData = getIntent().getExtras().getString(prodKey, p.getProdName());
        prodDetailData = getIntent().getExtras().getString(prodKey2, p.getProdDetails());
        prodIdData = getIntent().getExtras().getString(idKey, p.getProdId());*/
        currentProduct = (Products) getIntent().getExtras().get(prodKey);
        // Logs help to debug and check if we get the prodNameData we want and if it is null or if it has some value
        Log.d(TAG, "getProduct from Products returns: " + products.getProdName() + " " + prodNameData + " " + prodIdData);

        // Since we used the intent to grab the information passed from main activity class, we can now use the string
        // that we saved the information to and set the text to that value
        productName.setText(currentProduct.getProdName());
        productDetail.setText(currentProduct.getProdDetails());
        Log.d(TAG, "Rating value: " + prodRating.getRating());
        Log.i(TAG, "Rating value: " + prodRating.getRating());
    }

    private void setupItems() {
        productName = findViewById(R.id.tvProductName);
        productDetail = findViewById(R.id.tvProductDetail);
        prodRating = findViewById(R.id.ratingBar);
        saveRatingBtn = findViewById(R.id.btnSaveRating);
        prodRating.setNumStars(5);
    }

    private void getProduct()
    {
        db = FirebaseFirestore.getInstance();
        Log.d(TAG,"user id; " + prodIdData);
        DocumentReference docRef = db.collection("products").document(prodIdData);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        // This way we can get the fields from firestore and save them to a string.
                        String getFireStoreFieldUserName = document.getString("name");
                        String getFireStoreFieldAddress = document.getString("pictureId");

                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        Log.d(TAG, "Get Username for Sam " + getFireStoreFieldUserName + " pictureID " + getFireStoreFieldAddress);
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    public void saveRatingButton(View view)
    {
        // We need first to make sure we get the correct rating value, then we want to save that to the firestore
        Log.i(TAG, "Rating value: " + prodRating.getRating());
        Toast.makeText(this, "Rating Value is: " + prodRating.getRating() + " and rating " + prodIdData, Toast.LENGTH_SHORT).show();
        document = products.getProdId();
        // create the variable db so we can use that to save data to firestore


        //ratingMap.put("Product rating", Arrays.asList(prodRating.getRating()));

        //docRef = db.collection("products").document(prodIdData);

        // Atomically add a new region to the "regions" array field.
        docRef.update("Product rating", FieldValue.arrayUnion(prodRating.getRating()));

        // We want to add data to the document called ratings, on success we could do an if statement and test the
        // method, or just put out in log that it is complete.  We can do some things if we want ones it is successful or
        // just like in this case, leave it empty.
        /*db.collection("products")
                .add(ratingMap)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {

                    }
                });*/
       /* Log.d(TAG, "What is the id " + document);
        // Let's get the rating id so we are able to put the data in the correct document,
        // this is how we link the rating with the products
        db.collection("products")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult())
                            {
                                prodIdData = document.getId();
                            }
                            //db.collection("products").document(document).set(document, SetOptions.merge());
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });*/
    }

    public void addProductToCart(View view) {
        if (cartModel.getProductInCart().contains(currentProduct)) {

            Toast.makeText(this, "The product is already on the cart", Toast.LENGTH_SHORT).show();
        } else {
            cartModel.addProductToCart(currentProduct);
            Toast.makeText(this, "The product has been added to the cart.", Toast.LENGTH_SHORT).show();
        }
    }
}
