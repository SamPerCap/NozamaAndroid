package com.example.nozamaandroid.DAL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ImageView;

import com.example.nozamaandroid.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class DALProductImage {
    private  String dataBaseUrlFiles = "files";
    private StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();

    public void setImageviewById(String pictureId, final ImageView imageView)  {

        mStorageRef.child("product-pictures/"+ pictureId).
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
                Log.d("trythis", "onFailure: ");
                imageView.setImageResource(R.drawable.cake);
            }
        });
    }
}
