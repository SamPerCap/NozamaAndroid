package com.example.nozamaandroid;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import com.example.nozamaandroid.Models.Products;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class ProductDetails extends AppCompatActivity
{

    TextView productName, productDetail;
    String TAG = "Product Details class";
    String prodKey = "nameKey";
    String prodKey2 = "detailKey";
    String idKey = "idKey";
    String document;
    String prodIdData;
    RatingBar prodRating;
    Button saveRatingBtn;

    // We need to create an instance of the product class so we can use
    // the getters where we saved the prodNameData from mainactivity
    Products p = new Products();

    Map<String, Object> ratingMap = new HashMap<>();
    Map<String, Object> prodIdMap = new HashMap<>();



    @Override
    protected void onCreate(Bundle saveInstance)
    {
        super.onCreate(saveInstance);
        setContentView(R.layout.product_details);
        
        // save the id of the buttons to the button class variable so we can use that later and interact with it
        setupItems();


        // Here we grab the intent from main activity which saved it to the model BE product entity, so we should
        // be able to display it on the text field

        String prodNameData = getIntent().getExtras().getString(prodKey,p.getProdName());
        String prodDetailData = getIntent().getExtras().getString(prodKey2, p.getProdDetails());
        prodIdData = getIntent().getExtras().getString(idKey, p.getProdId());
        

        // Logs help to debug and check if we get the prodNameData we want and if it is null or if it has some value
        Log.i(TAG, "getProduct from Products returns: " + p.getProdName() + " " + prodNameData + " " + prodIdData);

        // Since we used the intent to grab the information passed from main activity class, we can now use the string
        // that we saved the information to and set the text to that value
        productName.setText(prodNameData);
        productDetail.setText(prodDetailData);
        Log.i(TAG, "Rating value: " + prodRating.getRating());
    }

    private void setupItems() {
        productName = findViewById(R.id.productName);
        productDetail = findViewById(R.id.productDetail);
        prodRating = findViewById(R.id.ratingBar);
        saveRatingBtn = findViewById(R.id.btnSave);
        prodRating.setNumStars(5);
    }

    public void saveRatingButton(View view)
    {
        // We need first to make sure we get the correct rating value, then we want to save that to the firestore
        Log.i(TAG, "Rating value: " + prodRating.getRating());
        Toast.makeText(this, "Rating Value is: " + prodRating.getRating() + " and rating " + prodIdData, Toast.LENGTH_SHORT).show();
        document = p.getProdId();
        // create the variable db so we can use that to save data to firestore
        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        //ratingMap.put("Product rating", Arrays.asList(prodRating.getRating()));

        DocumentReference washingtonRef = db.collection("products").document(prodIdData);

        // Atomically add a new region to the "regions" array field.
        washingtonRef.update("Product rating", FieldValue.arrayUnion(prodRating.getRating()));

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
}
