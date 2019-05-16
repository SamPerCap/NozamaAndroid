package com.example.nozamaandroid.Shared;

import android.graphics.Bitmap;

import org.json.JSONObject;

public interface ImageResponse<T> {
    void onResponseReceived(T response);
}