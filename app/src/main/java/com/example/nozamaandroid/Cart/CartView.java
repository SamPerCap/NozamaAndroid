package com.example.nozamaandroid.Cart;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.nozamaandroid.Adaptor.AdaptorProduct;
import com.example.nozamaandroid.HomeView;
import com.example.nozamaandroid.Models.CartModel;
import com.example.nozamaandroid.R;

import java.util.List;

public class CartView extends AppCompatActivity {
 CartModel cartModel ;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.cart_view);
        cartModel = CartModel.getInstance();
        ListView listView = (ListView) findViewById(R.id.listOrderProduct);
       // AdaptorProduct adapter = new AdaptorProduct(CartView.this,
        AdaptorProduct adapterProduct = new AdaptorProduct(this, cartModel.getProductInCart() );
        listView.setAdapter(adapterProduct);
    }
}
