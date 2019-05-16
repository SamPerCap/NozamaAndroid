package com.example.nozamaandroid.BLL;

import android.widget.ImageView;

import com.example.nozamaandroid.DAL.DALUser;
import com.example.nozamaandroid.Models.Users;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Query;

public class BLLUser {
    DALUser dalUser = new DALUser();

    public Users getUserInfo(Query docRef) {
        return dalUser.getUserFromDatabase(docRef);
    }
    public void setUserImage(String userID, ImageView imageView){
       dalUser.setUserImage(userID, imageView);
    }
    public void createUser(String email, String password){
        dalUser.createUser(email, password);
    }
}
