package com.example.nozamaandroid.Models;

import java.io.Serializable;

public class BEProducts implements Serializable
{
    //public long m_id;
    private String p_name;
    private String p_detail;
    private double p_price;
    private double uploadTime;
    private double p_location_x;
    private double p_location_y;
    private int p_img;

    public BEProducts(String p_name, String p_detail, double p_price, double uploadTime,
                      int p_img) {
        //this.m_id = m_id;
        this.p_name = p_name;
        this.p_detail = p_detail;
        this.p_price = p_price;
        this.uploadTime = uploadTime;
        this.p_img = p_img;
    }

    public BEProducts(String p_name, String p_detail, double p_price, double uploadTime, double p_location_x,
                    double p_location_y, int m_img) {

        this.p_name = p_name;
        this.p_detail = p_detail;
        this.p_price = p_price;
        this.uploadTime = uploadTime;
        this.p_location_x = p_location_x;
        this.p_location_y = p_location_y;
        this.p_img = p_img;
    }

    /*public long getM_id() {
        return m_id;
    }

    public void setM_id(long p_id) {
        this.m_id = p_id;
    }*/

    public String getP_name() {
        return p_name;
    }

    public void setP_name(String m_name) {
        this.p_name = p_name;
    }

    public double getP_price() {
        return p_price;
    }

    public void setP_price(double p_price) {
        this.p_price = p_price;
    }

    public double getP_uploadTime() {
        return uploadTime;
    }

    public void setP_uploadTime(double uploadTime) {
        this.uploadTime = uploadTime;
    }

    public double getM_location_x() {
        return p_location_x;
    }
    public double getM_location_y(){
        return p_location_y;
    }

    public int getM_img() {
        return p_img;
    }
    public void setM_img(int p_img) {
        this.p_img = p_img;
    }

    public void setP_location(double p_location_x, double p_location_y) {
        this.p_location_x = p_location_x;
        this.p_location_y = p_location_y;
    }



    @Override
    public String toString() {
        return "BEProduct{" +
                "p_name='" + p_name + '\'' +
                ", p_price='" + p_price + '\'' +
                ", p_detail='" + p_detail + '\'' +
                ", p_locationV1=" + p_location_x + '\'' +
                ", p_img=" + p_img +
                '}';
    }
}
