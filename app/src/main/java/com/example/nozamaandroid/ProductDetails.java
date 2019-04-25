package com.example.nozamaandroid;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.example.nozamaandroid.Models.Products;

public class ProductDetails extends AppCompatActivity
{
    TextView productName, productDetail;
    String TAG = "Product Details class";
    String prodKey = "nameKey";
    String prodKey2 = "detailKey";

    @Override
    protected void onCreate(Bundle saveInstance)
    {
        super.onCreate(saveInstance);
        setContentView(R.layout.product_details);

        productName = findViewById(R.id.productName);
        productDetail = findViewById(R.id.productDetail);
        Products p = new Products();

        Intent intent = getIntent();
        String data = getIntent().getExtras().getString(prodKey,p.getProdName());
        String data2 = getIntent().getExtras().getString(prodKey2, p.getProdDetails());

        Log.i(TAG, "getProduct from Products returns: " + p.getProdName() + " " + data);

        productName.setText(data);
        productDetail.setText(data2);

    }
}
