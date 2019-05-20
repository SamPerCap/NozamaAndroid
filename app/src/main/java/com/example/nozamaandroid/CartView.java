package com.example.nozamaandroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nozamaandroid.Adaptor.AdapterCart;
import com.example.nozamaandroid.BLL.BLLOrder;
import com.example.nozamaandroid.Models.CartModel;
import com.example.nozamaandroid.Models.Order;
import com.example.nozamaandroid.Models.Products;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class CartView extends AppCompatActivity {
    CartModel cartModel;
    Button buy;
    FirebaseAuth mAuth;
    BLLOrder bllOrder = new BLLOrder();
    TextView productAmount;
    AdapterCart adapterCart;
    ArrayList<String> productsId;


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
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "You need to be logged in to be able to buy.", Toast.LENGTH_LONG).show();
        } else {
            Order order = new Order();
            order.setIdUser(currentUser.getUid());
            productsId = new ArrayList<>();
            for (Products product : cartModel.getProductInCart()) {
                productsId.add(product.getProdId());
            }
            order.setProductsId(productsId);
            order.setStatusOfDelivery(false);
            bllOrder.addOrder(order);
            Toast.makeText(this, "Your order has been saved correctly.", Toast.LENGTH_LONG).show();
            cartModel.clearList();
            finish();
        }
    }
}
