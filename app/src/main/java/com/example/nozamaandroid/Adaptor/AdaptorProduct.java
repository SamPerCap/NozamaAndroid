package com.example.nozamaandroid.Adaptor;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nozamaandroid.BLL.BLLProductImage;
import com.example.nozamaandroid.HomeView;
import com.example.nozamaandroid.Models.CartModel;
import com.example.nozamaandroid.Models.Products;
import com.example.nozamaandroid.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class AdaptorProduct extends ArrayAdapter<Products> {
    CartModel cartModel ;
    private final Activity context;
    private String TAG = HomeView.TAG;
    ArrayList<Products> _arrayData;
    BLLProductImage bllProductImage = new BLLProductImage();

    StorageReference mStorageRef ;
    public AdaptorProduct(Activity context, ArrayList<Products> arrayData) {


        super(context, R.layout.product_list,arrayData );
        cartModel = CartModel.getInstance();
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
        final ImageView imageView =  rowView.findViewById(R.id.productImage);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.productNameList);
        txtTitle.setText(_arrayData.get(position).getProdName());

        Log.d(TAG, "getView: "+_arrayData.get(position).getPictureId());
            mStorageRef.child("product-pictures/"+ _arrayData.get(position).getPictureId()).
                    getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    // Use the bytes to display the image
                    Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                    imageView.setImageBitmap(bm);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                    imageView.setImageResource(R.drawable.cake);
                }
            });



        return rowView;

    }
}
