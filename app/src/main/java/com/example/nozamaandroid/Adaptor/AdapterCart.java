package com.example.nozamaandroid.Adaptor;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.example.nozamaandroid.HomeView;
import com.example.nozamaandroid.Models.CartModel;
import com.example.nozamaandroid.Models.Products;
import com.example.nozamaandroid.R;

import java.util.ArrayList;

public class AdapterCart  extends ArrayAdapter<Products> {
    CartModel cartModel ;
    private final Activity context;
    private String TAG = HomeView.TAG;
    ArrayList<Products> _arrayData;
    public AdapterCart(Activity context, ArrayList<Products> arrayData) {


        super(context, R.layout.product_list,arrayData );
        cartModel = CartModel.getInstance();
        Log.d(TAG, "Product adaptor ");
        this.context = context;
        this._arrayData = arrayData;

    }

    public View getView(final int position, final View view, final ViewGroup parent)
    {

        Log.d(TAG, "Getting the view from Product Adapter ");
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.cart_list, null, true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.productNameList);
        txtTitle.setText(_arrayData.get(position).getProdName());

       /* TextView price = (TextView) rowView.findViewById(R.id.productPrice);
        price.setText(_arrayData.get(position).get());*/

        EditText amount = (EditText) rowView.findViewById(R.id.amount);

        amount.setText(_arrayData.get(position).getAmount()+"");
        TextView lessthan = (TextView) rowView.findViewById(R.id.lessAmount);
        lessthan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeNumber(position,-1);
            }
        });
        TextView increaseAmount = (TextView) rowView.findViewById(R.id.increaseAmount);
        increaseAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeNumber(position,1);
            }
        });
        final TextView removeItem = (TextView) rowView.findViewById(R.id.removeItem);
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
        notifyDataSetInvalidated();

    }

    private void changeNumber(int postion, int i) {
        if(cartModel.getProductInCart().get(postion).getAmount()+i == 0)
        {

        }
        else
        {
            cartModel.changeAmount(postion,i);
            notifyDataSetInvalidated();
        }

    }


}
