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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class DALUser {

    Users user;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
    public static String TAG = "DALUser";


    public Users getUserFromDatabase(Query docRef) {
        docRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                user = new Users();

                                String getFireStoreAddress = document.getString("Address");
                                String getFireStoreEmail = document.getString("Email");
                                String getFireStorePhonenumber = document.getString("Phonenumber");
                               // String getFireStorePictureId = document.getString("PictureId");
                                String getFireStoreUsername = document.getString("Username");

                                //user.setImgId(getFireStorePictureId);
                                user.setAddress(getFireStoreAddress);
                                user.setPhoneNumber(getFireStorePhonenumber);
                                user.setEmail(getFireStoreEmail);
                                user.setUserName(getFireStoreUsername);
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
        return user;

        /*Log.d(TAG,"Getting user from Database");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                Log.d(TAG, task.getResult().toString());
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        user = new Users();
                        String getFireStoreAddress = document.getString("Username");
                        String getFireStoreEmail = document.getString("Email");
                        String getFireStorePhonenumber = document.getString("Phonenumber");
                        String getFireStorePictureId = document.getString("PictureId");
                        String getFireStoreUsername = document.getString("Username");

                        user.setAddress(getFireStoreAddress);
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
        return user;*/
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
