package com.example.nozamaandroid.BLL;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.nozamaandroid.DAL.DALUser;
import com.example.nozamaandroid.Models.Users;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class BLLUser {
    DALUser dalUser = new DALUser();
    String TAG = "BLLUser";
    String filePath;
    Boolean[] success = new Boolean[1];


    public Users getUserInfo(Query docRef) {
        return dalUser.getUserFromDatabase(docRef);
    }

    public void setUserImage(String userID, ImageView imageView) {
        dalUser.setUserImage(userID, imageView);
    }

    public Boolean createUser(Activity userCreation, String email, String password) {
        return dalUser.createUser(userCreation, email, password);
    }

    public Boolean uploadToFirestore(String email, String password, String username, String address,
                                     String phonenumber, String pictureId) {
        return dalUser.uploadToFireStore(email, password, username, address, phonenumber, pictureId);
    }

    public Boolean uploadToStorage(Bitmap currentImage, String userId, final StorageReference mStorageRef) {
        Log.d(TAG, "Starting uploadPictureToFB");
        Log.d(TAG, "What is the current userId: " + userId);
        StorageReference riversRef = mStorageRef.child("user-images/" + userId);
        Bitmap bitmap = currentImage;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        Log.d(TAG, "upload to storage byte: " + data.toString());
        riversRef.putBytes(data)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        Task<Uri> downloadUrl = mStorageRef.getDownloadUrl();
                        Log.d(TAG, "What is downloadURL: " + downloadUrl + " and name: " + mStorageRef.getName());
                        success[0] = true;
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        Log.d(TAG, "Failed to upload an image to storage: " + exception);
                        success[0] = false;
                    }
                });
        return success[0];
    }

    public String getFilePath(String _filePath, ImageView pictureView) {
        try {
            if (_filePath != null) {
                Log.d(TAG, "is this here?");
                Bitmap bit = BitmapFactory.decodeFile(_filePath);
                Log.d(TAG, "is bit null: " + bit);
                if (bit != null) {
                    pictureView.setImageBitmap(bit);
                    Log.d(TAG, "image is this now: " + _filePath);
                    filePath = _filePath;
                }
            }
        } catch (Exception ex) {
            Log.d(TAG, "locateItems: images " + ex);
        }
        Log.d(TAG, "let's see what is image: " + _filePath);
        return filePath;
    }

    public void uploadMetaDataToDatabase(String metaUplTime, String metaName, String metaSize, String metaType) {
        dalUser.uploadMetaDataToDataBase(metaUplTime, metaName, metaSize, metaType);
    }

}
