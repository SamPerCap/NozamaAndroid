package com.example.nozamaandroid.Adaptor;

import android.app.Activity;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.nozamaandroid.Models.CartModel;
import com.example.nozamaandroid.Models.Products;
import com.example.nozamaandroid.R;

import java.util.ArrayList;

public class AdapterCart extends ArrayAdapter<Products> {
    static CartModel cartModel = CartModel.getInstance();
    private final Activity context;
    private String TAG = "AdapterCart";
    private ArrayList<Products> arrayCartList;
    private TextView amount, txtTitle, removeItem;
    private Button increaseAmount, decreaseAmount;
    int one = 1;

    public AdapterCart(Activity context, ArrayList<Products> arrayData) {
        super(context, R.layout.product_list, arrayData);
        Log.d(TAG, "Product adaptor");
        this.context = context;
        this.arrayCartList = arrayData;

    }

    public View getView(final int position, final View view, final ViewGroup parent) {

        Log.d(TAG, "Getting the view from Product Adapter ");
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.cart_list, null, true);

        txtTitle = rowView.findViewById(R.id.productNameList);
        amount = rowView.findViewById(R.id.productAmount);
        decreaseAmount = rowView.findViewById(R.id.lessAmount);
        increaseAmount = rowView.findViewById(R.id.increaseAmount);
        removeItem = rowView.findViewById(R.id.removeItem);

        txtTitle.setText(arrayCartList.get(position).getProdName());
        amount.setText(arrayCartList.get(position).getAmount() + "");

        decreaseAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (arrayCartList.get(position).getAmount() != 1)
                    changeNumber(false, arrayCartList.get(position));
                amount.setText(arrayCartList.get(position).getAmount() + "");
            }
        });
        increaseAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeNumber(true, arrayCartList.get(position));
                amount.setText(arrayCartList.get(position).getAmount() + "");
            }
        });
        removeItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteItem(position);
            }
        });
        return rowView;

    }

    private void deleteItem(int postion) {
        cartModel.removeItem(postion);
        this.remove(arrayCartList.get(postion));
        notifyDataSetChanged();
    }

    private void changeNumber(boolean increase, Products product) {
        int productAmount = product.getAmount();
        if (increase) {
            product.setAmount(productAmount + one);
        } else {
            product.setAmount(productAmount - one);
        }
    }
}
