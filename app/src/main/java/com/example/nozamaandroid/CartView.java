package com.example.nozamaandroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.nozamaandroid.Adaptor.AdapterCart;
import com.example.nozamaandroid.BLL.BLLOrder;
import com.example.nozamaandroid.Models.CartModel;
import com.example.nozamaandroid.Models.Order;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class CartView extends AppCompatActivity {
    CartModel cartModel;
    Button buy;
    FirebaseAuth mAuth;
    BLLOrder bllOrder = new BLLOrder();
    TextView productAmount;
    AdapterCart adapterCart;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.cart_view);
        cartModel = CartModel.getInstance();
        ListView listView = findViewById(R.id.listOrderProduct);
        adapterCart = new AdapterCart(this, cartModel.getProductInCart());
        adapterCart.setNotifyOnChange(true);
        listView.setAdapter(adapterCart);
        setupViewItems();
    }

    private void setupViewItems() {
        buy = findViewById(R.id.buyNow);
        productAmount = findViewById(R.id.productAmount);
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
