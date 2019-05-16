package com.example.nozamaandroid.DAL;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.nozamaandroid.Models.Users;
import com.example.nozamaandroid.R;
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
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

public class DALUser {

    Users user;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
    public static String TAG = "DALUser";
    String userToSave;
    FirebaseUser fbUser = mAuth.getCurrentUser();
    Map<String, Object> userMap;
    Map<String, Object> fileMap;

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

    public Boolean[] createUser(final Activity userCreation, final String email, final String password) {
        final Boolean[] successful = new Boolean[1];
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(userCreation, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            userToSave = fbUser.getUid();
                            Log.d(TAG, "What is the D: " + userToSave);
                            // uploadToStorage(email, password, username, phonenumber, address);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(userCreation.getBaseContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        successful[0] = task.isSuccessful();

                        // ...
                    }

                });
        return successful;
    }

    public Boolean[] uploadToFireStore(final String email, final String password, final String username, final String phonenumber,
                                       final String address, final String pictureId) {
        final Boolean[] successUploadToFirestore = new Boolean[1];

        try {
            final Users users = new Users();
            userMap = new HashMap<>();
            // FireStoreDatabase initialize
            Log.d(TAG, "What is the id: " + userToSave);
            userMap.put("Email", email);
            userMap.put("Password", password);
            userMap.put("Username", username);
            userMap.put("Address", address);
            userMap.put("Phonenumber", phonenumber);
            userMap.put("PictureId", pictureId);

            // Add a new document with a generated ID
            db = FirebaseFirestore.getInstance();
            db.collection("users")
                    .document(userToSave)
                    .set(userMap)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "DocumentSnapshot added with ID: " + userToSave);
                            users.setUserName(email);
                            users.setPassword(password);
                            users.setUserName(username);
                            users.setAddress(address);
                            users.setPhoneNumber(phonenumber);
                            users.setImgId(userToSave);

                            Log.d(TAG, "What is username: " + users.getUserName().toString());
                            Log.d(TAG, "What is password: " + users.getPassword().toString());
                            Log.d(TAG, "What is the id: " + userToSave);
                            successUploadToFirestore[0] = true;
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "Error adding document", e);
                            successUploadToFirestore[0] = false;
                        }
                    });

            Log.d(TAG, "What is get text: " + email);
        } catch (Error e) {
            Log.d(TAG, "Exception: " + e);
        }
        StorageReference spaceRef = mStorageRef.child("user-images/" + userToSave);
        Log.d(TAG, "What is ID: " + spaceRef);
        return successUploadToFirestore;
    }

    public void uploadMetaDataToDataBase(String metaUplTime, String metaName, String metaSize, String metaType) {
        fileMap = new HashMap<>();
        fileMap.put("lastModified", metaUplTime);
        fileMap.put("name", metaName);
        fileMap.put("size", metaSize);
        fileMap.put("type", metaType);

        db.collection("files")
                .add(fileMap)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "Uploading to firestore was successful!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Something went wrong with uploading metaData: " + e);
                    }
                });
    }
}
