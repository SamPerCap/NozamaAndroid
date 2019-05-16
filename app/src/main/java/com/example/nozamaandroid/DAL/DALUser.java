package com.example.nozamaandroid.DAL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ImageView;

import com.example.nozamaandroid.Models.Users;
import com.example.nozamaandroid.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class DALUser {

    Users user;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
    DocumentReference docRef;
    public static String TAG = "DALUser";


    public Users getUserFromDatabase(String userID) {
        Log.d(TAG,"Getting user from Database");
        docRef = db.collection("users").document(userID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        user = new Users();
                        String getFireStoreAddres = document.getString("Username");
                        String getFireStoreEmail = document.getString("Email");
                        String getFireStorePhonenumber = document.getString("Phonenumber");
                        String getFireStorePictureId = document.getString("PictureId");
                        String getFireStoreUsername = document.getString("Username");

                        user.setAddress(getFireStoreAddres);
                        user.setEmail(getFireStoreEmail);
                        user.setPhoneNumber(getFireStorePhonenumber);
                        user.setImgId(getFireStorePictureId);
                        user.setUserName(getFireStoreUsername);

                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
        return user;
    }

    public void setUserImage(String userID, final ImageView imageView) {
        Log.d(TAG, "current userID: " + userID);
        mStorageRef.child("user-images/" + userID).
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
                imageView.setImageResource(R.drawable.bag_icon);
                Log.d(TAG, "Error with getting the current user image: " + exception);
            }
        });
    }

    public void createUser(String email, String password) {
    }
}
