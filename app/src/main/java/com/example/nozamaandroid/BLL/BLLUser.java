package com.example.nozamaandroid.BLL;

import android.widget.ImageView;

import com.example.nozamaandroid.DAL.DALUser;
import com.example.nozamaandroid.Models.Users;

public class BLLUser {
    DALUser dalUser = new DALUser();

    public Users getUserInfo(String userID) {
        return dalUser.getUserFromDatabase(userID);
    }
    public void setUserImage(String userID, ImageView imageView){
       dalUser.setUserImage(userID, imageView);
    }
    public void createUser(String email, String password){
        dalUser.createUser(email, password);
    }
}
