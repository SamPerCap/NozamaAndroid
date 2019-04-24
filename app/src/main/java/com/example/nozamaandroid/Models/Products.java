package com.example.nozamaandroid.Models;

public class Products
{
    private String prodId;
    private String prodName;
    private String prodDetails;

    public Products()
    {

    }

    public Products( String prodId, String prodName, String prodDetails )
    {
        this.prodId = prodId;
        this.prodName = prodName;
        this.prodDetails = prodDetails;
    }

    public String getProductId() {
        return prodId;
    }

    public String getProdName()
    {
        return prodName;
    }

    public String getProdDetails()
    {
        return prodDetails;
    }
}
