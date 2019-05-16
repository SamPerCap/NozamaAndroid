package com.example.nozamaandroid.Adaptor;

import android.app.Activity;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.nozamaandroid.BLL.BLLProducts;
import com.example.nozamaandroid.Models.Products;
import com.example.nozamaandroid.R;
import com.example.nozamaandroid.Shared.ImageResponse;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class AdaptorProduct extends ArrayAdapter<Products> {
    private final Activity context;
    private String TAG = "AdapterProduct";
    ArrayList<Products> _arrayData;
    BLLProducts bllProduct = new BLLProducts();

    StorageReference mStorageRef ;
    public AdaptorProduct(Activity context, ArrayList<Products> arrayData) {


        super(context, R.layout.product_list,arrayData );
        Log.d(TAG, "Product adaptor ");
        this.context = context;
        this._arrayData = arrayData;
        mStorageRef = FirebaseStorage.getInstance().getReference();

    }

    public View getView(final int position, View view, final ViewGroup parent)
    {

            Log.d(TAG, "Getting the view from Product Adapter ");
            LayoutInflater inflater = context.getLayoutInflater();
            View rowView = inflater.inflate(R.layout.product_list, null, true);
            final ImageView imageView = rowView.findViewById(R.id.productImage);
            TextView txtTitle = (TextView) rowView.findViewById(R.id.productNameList);
            txtTitle.setText(_arrayData.get(position).getProdName());
            TextView txtPrice = (TextView) rowView.findViewById(R.id.product_price_HomeView);
            txtPrice.setText(_arrayData.get(position).getProdDetails()+" DKK");
            Log.d(TAG, "getView: " + _arrayData.get(position).getPictureId());
            bllProduct.getImageById(_arrayData.get(position).getPictureId(), new ImageResponse() {
                @Override
                public void onResponseReceived(Bitmap response) {
                    if(response != null) {
                        imageView.setImageBitmap(response);
                    }
                    else{
                        imageView.setImageResource(R.drawable.cake);
                    }
                }
            });

                return rowView;

    }
}
