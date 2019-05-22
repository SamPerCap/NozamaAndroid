package com.example.nozamaandroid.DAL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.nozamaandroid.Models.MetaData;
import com.example.nozamaandroid.Models.Products;
import com.example.nozamaandroid.Models.Users;
import com.example.nozamaandroid.Shared.OnResponse;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class DALImage {
    private StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
    String TAG = "DALImage";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    StorageReference riversRef;


    public void uploadImage(final Users users, byte[] image, final OnResponse response, final Products products) {
        if (users != null)
            riversRef = mStorageRef.child("user-images/" + users.getUserId());
        else
            riversRef = mStorageRef.child("product-pictures/" + products.getProdId());
        riversRef.putBytes(image)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        Task<Uri> downloadUrl = mStorageRef.getDownloadUrl();
                        Log.d(TAG, "What is downloadURL: " + downloadUrl + " and name: " + mStorageRef.getName());
                        if (users != null) {
                            users.setImgId(users.getUserId());
                            uploadToFireStore(null, users, response);
                        } else {
                            products.setPictureId(products.getProdId());
                            uploadToFireStore(products, null, response);
                        }
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

    private void uploadToFireStore(final Products product, final Users user, final OnResponse response) {
        if (user != null)
            logicForUser(user, response);
        else
            logicForProduct(product, response);

    }

    private void logicForProduct(final Products products, final OnResponse onResponse) {
        try {
            Map<String, Object> productMap = new HashMap<>();
            // FireStoreDatabase initialize
            productMap.put("pictureId", products.getPictureId());
            productMap.put("name", products.getProdName());
            productMap.put("price", products.getPrice());
            productMap.put("details", products.getProdDetails());
            productMap.put("category", products.getCategory());
            // Add a new document with a generated ID
            db = FirebaseFirestore.getInstance();
            db.collection("products")
                    .document(products.getProdId())
                    .set(productMap)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "DocumentSnapshot added with ID: " + products.getProdId());
                            getMetaData(null, onResponse, products);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            onResponse.onResponseReceived(e);
                        }
                    });

        } catch (Error e) {
            onResponse.onResponseReceived(e);
            Log.d(TAG, "Exception: " + e);
        }
    }

    private void logicForUser(final Users user, final OnResponse onResponse) {
        try {
            Map<String, Object> userMap = new HashMap<>();
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

                            getMetaData(user, onResponse, null);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            onResponse.onResponseReceived(e);
                        }
                    });

        } catch (Error e) {
            onResponse.onResponseReceived(e);
            Log.d(TAG, "Exception: " + e);
        }
    }

    private void getMetaData(final Users user, final OnResponse response, final Products products) {
        // Get reference to the file
        riversRef.getMetadata().addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
            @Override
            public void onSuccess(StorageMetadata storageMetadata) {
                Log.d(TAG, "What is the metaData: " + storageMetadata.getContentType());
                Log.d(TAG, "What is the name: " + storageMetadata.getName());
                Log.d(TAG, "What is the size: " + storageMetadata.getSizeBytes());
                Log.d(TAG, "What is the update Time in millis: " + storageMetadata.getUpdatedTimeMillis());
                MetaData metaData = new MetaData();
                metaData.setMetaName(storageMetadata.getName());
                metaData.setMetaType(storageMetadata.getContentType());
                metaData.setMetaSize(storageMetadata.getSizeBytes() + "");
                metaData.setMetaUplTime(storageMetadata.getUpdatedTimeMillis() + "");
                if (user != null)
                    uploadMetaDataToDataBase(null, user, metaData, response);
                else
                    uploadMetaDataToDataBase(products, null, metaData, response);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                response.onResponseReceived("Getmeta Data" + exception);
                // Uh-oh, an error occurred!
            }
        });
    }

    public void uploadMetaDataToDataBase(final Products products, final Users user, MetaData metaData, final OnResponse response) {
        Map<String, Object> fileMap;

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
                        if (user != null)
                            response.onResponseReceived(user);
                        else
                            response.onResponseReceived(products.getPictureId());
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

    public void setImageviewById(String pictureId, final OnResponse response) {
        if (pictureId != null) {
            mStorageRef.child("product-pictures/" + pictureId).
                    getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    // Use the bytes to display the image
                    Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    response.onResponseReceived(bm);
                    //imageView.setImageBitmap(bm);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                    response.onResponseReceived(null);
                }
            });
        } else {
            response.onResponseReceived(null);
        }
    }

    public void setUserImage(String userID, final OnResponse response) {
        Log.d(TAG, "IMAGE current userID: " + userID);
        mStorageRef.child("user-images/" + userID).
                getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {

                // Use the bytes to display the image
                Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                Log.d(TAG, "onSuccess: image return");
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
}
