package com.example.nozamaandroid.DAL;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.example.nozamaandroid.Models.MetaData;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class DALUser {

    Users user;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
    public static String TAG = "DALUser";
    String userToSaveId;
    Map<String, Object> userMap;
    Map<String, Object> fileMap;
    Boolean[] success = new Boolean[2];
    FirebaseFirestore db;


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
    }

    public void setUserImage(String userID, final OnResponse response) {
        Log.d(TAG, "current userID: " + userID);
        mStorageRef.child("user-images/" + userID).
                getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {

                // Use the bytes to display the image
                Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                response.onResponseReceived(bm);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                response.onResponseReceived(null);
                Log.d(TAG, "Error with getting the current user image: " + exception);
            }
        });
    }

    /*public Boolean createUser(final Activity userCreation, final String email, final String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(userCreation, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser fbUser = mAuth.getCurrentUser();
                            userToSaveId = fbUser.getUid();
                            Log.d(TAG, "What is the D: " + userToSaveId);
                            success[0] = true;
                            // uploadToStorage(email, password, username, phonenumber, address);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(userCreation.getBaseContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            success[0] = false;
                        }
                    }

                });
        return success[0];
    }*/

    public void uploadToFireStore(final Users user, final OnResponse response) {
        try {
            final Users users = new Users();
            userMap = new HashMap<>();
            // FireStoreDatabase initialize
            userMap.put("Email", user.getEmail());
            userMap.put("Password", user.getPassword());
            userMap.put("Username", user.getUserName());
            userMap.put("Address", user.getAddress());
            userMap.put("Phonenumber", user.getPhoneNumber());
            userMap.put("PictureId", user.getImgId());

            // Add a new document with a generated ID
            db = FirebaseFirestore.getInstance();
            db.collection("users")
                    .document(user.getUserId())
                    .set(userMap)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "DocumentSnapshot added with ID: " + user.getUserId());

                            getMetaData(user, response);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            response.onResponseReceived(e);
                        }
                    });

        } catch (Error e) {
            response.onResponseReceived(e);
            Log.d(TAG, "Exception: " + e);
        }
    }

    public void uploadMetaDataToDataBase(final Users user,MetaData metaData, final OnResponse response) {
        fileMap = new HashMap<>();
        fileMap.put("lastModified", metaData.getMetaUplTime());
        fileMap.put("name", metaData.getMetaName());
        fileMap.put("size", metaData.getMetaSize());
        fileMap.put("type", metaData.getMetaType());

        db.collection("files")
                .add(fileMap)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                      response.onResponseReceived(user);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        response.onResponseReceived("uploadMetaDataToDataBase" + e);
                        Log.d(TAG, "Something went wrong with uploading metaData: " + e);
                    }
                });
    }


    public void upLoadImage(final Users user, byte[] image, final OnResponse response) {

        StorageReference riversRef = mStorageRef.child("user-images/" + user.getUserId());
            riversRef.putBytes(image)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        Task<Uri> downloadUrl = mStorageRef.getDownloadUrl();
                        Log.d(TAG, "What is downloadURL: " + downloadUrl + " and name: " + mStorageRef.getName());
                        user.setImgId(user.getUserId());
                       uploadToFireStore(user,response);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        response.onResponseReceived(exception);
                        Log.d(TAG, "Failed to upload an image to storage: " + exception);
                    }
                });
    }

    public void createUser(final Users user, final byte[] currentImage, final OnResponse response) {
        mAuth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser fbUser = mAuth.getCurrentUser();
                            user.setUserId( fbUser.getUid());
                            upLoadImage(user,currentImage,response);
                            // uploadToStorage(email, password, username, phonenumber, address);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            response.onResponseReceived("tast failed");
                        }
                    }

                });
    }
    private void getMetaData(final Users user, final OnResponse response) {
        // Get reference to the file
        StorageReference forestRef = mStorageRef.child("user-images/" + user.getImgId());
        forestRef.getMetadata().addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
            @Override
            public void onSuccess(StorageMetadata storageMetadata) {
                Log.d(TAG, "What is the metaData: " + storageMetadata.getContentType());
                Log.d(TAG, "What is the name: " + storageMetadata.getName());
                Log.d(TAG, "What is the size: " + storageMetadata.getSizeBytes());
                Log.d(TAG, "What is the update Time in millis: " + storageMetadata.getUpdatedTimeMillis());
                MetaData metaData =  new MetaData();
                metaData.setMetaName( storageMetadata.getName());
                metaData.setMetaType(storageMetadata.getContentType());
                metaData.setMetaSize(storageMetadata.getSizeBytes() + "");
                metaData.setMetaUplTime(storageMetadata.getUpdatedTimeMillis() + "");
                uploadMetaDataToDataBase(user,metaData,response);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                response.onResponseReceived("Getmeta Data" + exception);
                // Uh-oh, an error occurred!
            }
        });
    }
}
