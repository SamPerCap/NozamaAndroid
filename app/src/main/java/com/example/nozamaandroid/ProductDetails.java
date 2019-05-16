package com.example.nozamaandroid;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nozamaandroid.Shared.ImageResponse;
import com.example.nozamaandroid.BLL.BLLProducts;
import com.example.nozamaandroid.Models.CartModel;
import com.example.nozamaandroid.Models.Products;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProductDetails extends AppCompatActivity {
    TextView productName, productDetail, productPrice;
    String TAG = "Product Details class";
    String prodKey = "productKey";
    Products products;
    FirebaseFirestore db;
    DocumentReference docRef;
    Products currentProduct;
    String document;
    String prodIdData;
    ImageView productImage;
    RatingBar prodRating;
    BLLProducts bllProducts = new BLLProducts();
    Button saveRatingBtn;
    static CartModel cartModel = CartModel.getInstance();
    // We need to create an instance of the product class so we can use
    // the getters where we saved the prodNameData from mainactivity

    @Override
    protected void onCreate(Bundle saveInstance) {
        super.onCreate(saveInstance);
        setContentView(R.layout.product_details);

        db = FirebaseFirestore.getInstance();

        setupItems();

        currentProduct = (Products) getIntent().getExtras().get(prodKey);

        productName.setText(currentProduct.getProdName());
        productDetail.setText(currentProduct.getProdDetails());
//        productPrice.setText(currentProduct.getPrice());

        bllProducts.getImageById(currentProduct.getPictureId(), new ImageResponse() {
            @Override
            public void onResponseReceived(Object response) {
                if(response != null) {
                    productImage.setImageBitmap((Bitmap) response);
                }
                else{
                    productImage.setImageResource(R.drawable.cake);
                }
            }
        });


        Log.d(TAG, "Rating value: " + prodRating.getRating());
    }

    private void setupItems() {

        productName = findViewById(R.id.tvProductName);
        productDetail = findViewById(R.id.tvProductDetail);
        prodRating = findViewById(R.id.ratingBar);
        saveRatingBtn = findViewById(R.id.btnSaveRating);
        productImage = findViewById(R.id.productImage);
        productPrice = findViewById(R.id.tvProductPrice);
        prodRating.setNumStars(5);
    }

    public void saveRatingButton(View view) {
        // We need first to make sure we get the correct rating value, then we want to save that to the firestore
        Log.d(TAG, "Rating value: " + prodRating.getRating());
        Toast.makeText(this, "Rating Value is: " + prodRating.getRating() + " and rating " + prodIdData, Toast.LENGTH_SHORT).show();
        document = products.getProdId();
        // create the variable db so we can use that to save data to firestore

        // Atomically add a new region to the "regions" array field.
        docRef.update("Product rating", FieldValue.arrayUnion(prodRating.getRating()));

        // We want to add data to the document called ratings, on success we could do an if statement and test the
        // method, or just put out in log that it is complete.  We can do some things if we want ones it is successful or
        // just like in this case, leave it empty.
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
    cartModel.checkIfProductId(currentProduct,this);
    }
}
