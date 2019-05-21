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
import com.example.nozamaandroid.BLL.BLLUser;
import com.example.nozamaandroid.Models.CartModel;
import com.example.nozamaandroid.Models.Order;
import com.example.nozamaandroid.Models.Products;
import com.example.nozamaandroid.Models.Users;
import com.example.nozamaandroid.Shared.IMyCallBack;
import com.example.nozamaandroid.Shared.OnResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class CartView extends AppCompatActivity {
    CartModel cartModel;
    Button buy;
    BLLOrder bllOrder = new BLLOrder();
    TextView productAmount;
    ListView listView;
    AdapterCart adapterCart;
    ArrayList<String> productsId;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();
    BLLUser bllUser = new BLLUser();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart_view);
        setupViewItems();
        cartModel = CartModel.getInstance();
        adapterCart = new AdapterCart(this, cartModel.getProductInCart());
        adapterCart.setNotifyOnChange(true);
        listView.setAdapter(adapterCart);
    }

    private void setupViewItems() {
        buy = findViewById(R.id.buyNow);
        productAmount = findViewById(R.id.productAmount);
        listView = findViewById(R.id.listOrderProduct);
        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Buy();
            }
        });
    }

    private void Buy() {
        if (currentUser == null) {
            Toast.makeText(this, "You need to be logged in to be able to buy.", Toast.LENGTH_LONG).show();
        } else {
            final Order order = new Order();
            order.setIdUser(currentUser.getUid());
            productsId = new ArrayList<>();
            for (Products product : cartModel.getProductInCart()) {
                productsId.add(product.getProdId());
            }
            order.setProductsId(productsId);
            order.setStatusOfDelivery(false);
            bllUser.getUserById(currentUser.getUid(), new OnResponse() {
                @Override
                public void onResponseReceived(Object response) {
                    Users user = (Users) response;
                    order.setUserAddress(user.getAddress());
                    bllOrder.addOrder(order);
                    cartModel.clearList(adapterCart);
                    finish();
                }
            });
            Toast.makeText(this, "Your order has been saved correctly.", Toast.LENGTH_LONG).show();
        }
    }
}
