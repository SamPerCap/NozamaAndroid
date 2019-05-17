package com.example.nozamaandroid.Shared;

import android.graphics.Bitmap;

import org.json.JSONObject;

public interface OnResponse<T> {
    void onResponseReceived(T response);
}