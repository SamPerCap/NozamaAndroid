package com.example.nozamaandroid.Models;

import java.util.ArrayList;

public class Users {
    private String userId;
    private String email;
    private String password;
    private String userName;
    private String address;
    private String phoneNumber;
    private String imgId;

public Users() {}

    public String getImgId() {
        return imgId;
    }

    public void setImgId(String imgId) {
        this.imgId = imgId;
    }

    public Users(String userId, String email, String password, String userName, String address, String phoneNumber, String imgId) {
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.userName = userName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.imgId = imgId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
