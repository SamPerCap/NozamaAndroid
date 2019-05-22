package com.example.nozamaandroid.DAL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.nozamaandroid.Models.Users;
import com.example.nozamaandroid.Shared.OnResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class DALUser {

    private Users user;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
    public static String TAG = "DALUser";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DALImage dalImage = new DALImage();



    public void createUser(final Users user, final byte[] currentImage, final OnResponse response) {
        mAuth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser fbUser = mAuth.getCurrentUser();
                            user.setUserId(fbUser.getUid());
                            dalImage.uploadImage(user, currentImage, response, null);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            response.onResponseReceived("tast failed");
                        }
                    }

                });
    }

    public void getUserById(String uid, final OnResponse onResponse) {
        Log.d(TAG, "getUserById: " + uid);
        DocumentReference docRef = db.collection("users").document(uid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        user = new Users();

                        String getFireStoreAddress = document.getString("Address");
                        String getFireStoreEmail = document.getString("Email");
                        String getFireStorePhonenumber = document.getString("Phonenumber");
                        String getFireStorePictureId = document.getString("PictureId");
                        String getFireStoreUsername = document.getString("Username");

                        user.setImgId(getFireStorePictureId);
                        user.setAddress(getFireStoreAddress);
                        user.setPhoneNumber(getFireStorePhonenumber);
                        user.setEmail(getFireStoreEmail);
                        user.setUserName(getFireStoreUsername);
                        onResponse.onResponseReceived(user);
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                    } else {
                        onResponse.onResponseReceived(null);
                        Log.d(TAG, "No such document");
                    }
                } else {
                    onResponse.onResponseReceived(null);
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

    }

    public void updateUser(Users currentUser, String uid) {
        db = FirebaseFirestore.getInstance();
        db.collection("users").document(uid)
                .update(
                        "Username", currentUser.getUserName(),
                        "Address", currentUser.getAddress(),
                        "Phonenumber", currentUser.getPhoneNumber()
                );
    }

    public void removeAccount(String userId, final OnResponse response) {

        db = FirebaseFirestore.getInstance();
        db.collection("users").document(userId)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                        user.delete()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Log.d(TAG, "User account deleted.");
                                            response.onResponseReceived("done");
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                response.onResponseReceived(null);
                            }
                        });
                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        response.onResponseReceived(null);
                        Log.w(TAG, "Error deleting document", e);
                    }
                });

    }
}
