package com.example.nozamaandroid.Models;

public class Users
{
    private String userId;
    private String userName;
    private String password;

    public Users()
    {

    }

    public Users( String userId, String userName, String password )
    {
        this.userId = userId;
        this.userName = userName;
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserName()
    {
        return userName;
    }

    public String getPassword()
    {
        return password;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }
}
