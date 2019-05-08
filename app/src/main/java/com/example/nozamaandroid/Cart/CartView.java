package com.example.nozamaandroid.Cart;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.nozamaandroid.Adaptor.AdapterCart;
import com.example.nozamaandroid.Adaptor.AdaptorProduct;
import com.example.nozamaandroid.BLL.BLLOrder;
import com.example.nozamaandroid.DALProducts.AddProduct;
import com.example.nozamaandroid.HomeView;
import com.example.nozamaandroid.Models.CartModel;
import com.example.nozamaandroid.Models.Order;
import com.example.nozamaandroid.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class CartView extends AppCompatActivity {
 CartModel cartModel ;
 Button buy;
    FirebaseAuth mAuth;
 BLLOrder bllOrder = new BLLOrder();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.cart_view);
        cartModel = CartModel.getInstance();
        ListView listView = (ListView) findViewById(R.id.listOrderProduct);
       // AdaptorProduct adapter = new AdaptorProduct(CartView.this,
        AdapterCart adapterProduct = new AdapterCart(this, cartModel.getProductInCart() );
        listView.setAdapter(adapterProduct);
        setupViewItems();
    }

    private void setupViewItems() {
        buy = findViewById(R.id.buyNow);
        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Buy();
            }
        });
    }

    private void Buy() {
        Order order = new Order();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        order.setIdUser(currentUser.getUid());
        order.setProducts(cartModel.getProductInCart());
        bllOrder.addOrder(order);
        cartModel.clearList();
        Intent intent = new Intent(this, HomeView.class);
        startActivity(intent);
        finish();

    }
}
