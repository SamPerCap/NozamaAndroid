package com.example.nozamaandroid.BLL;

import android.graphics.Bitmap;
import android.media.Image;
import android.widget.ImageView;

import com.example.nozamaandroid.DAL.DALUser;
import com.example.nozamaandroid.UserCreation;
import com.example.nozamaandroid.Models.Users;

public class BLLUser {
    DALUser dalUser = new DALUser();

    public Users getUserInfo(String userID) {
        return dalUser.getUserFromDatabase(userID);
    }
    public Bitmap getUserImage(String userID){
        return dalUser.getUserImageFromStorage(userID);
    }
    public void createUser(String email, String password){
        dalUser.createUser(email, password);
    }
}
