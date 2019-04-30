package com.example.nozamaandroid.Adaptor;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.nozamaandroid.HomeView;
import com.example.nozamaandroid.Models.CartModel;
import com.example.nozamaandroid.Models.Products;
import com.example.nozamaandroid.R;

import java.util.ArrayList;

public class AdaptorProduct extends ArrayAdapter<Products> {
    CartModel cartModel ;
    private final Activity context;
    private String TAG = HomeView.TAG;
    ArrayList<Products> _arrayData;
    public AdaptorProduct(Activity context, ArrayList<Products> arrayData) {


        super(context, R.layout.product_list,arrayData );
        cartModel = CartModel.getInstance();
        Log.d(TAG, "Product adaptor ");
        this.context = context;
        this._arrayData = arrayData;

    }

    public View getView(final int position, View view, final ViewGroup parent)
    {
        Log.d(TAG, "Getting the view from Product Adapter ");
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.product_list, null, true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.productNameList);
        txtTitle.setText(_arrayData.get(position).getProdName());


        return rowView;

    }
}
