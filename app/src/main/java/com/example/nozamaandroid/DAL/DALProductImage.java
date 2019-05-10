package com.example.nozamaandroid.DAL;

import android.support.annotation.NonNull;

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

    public boolean getImageById(String pictureId)  {

return true;
    }
}
