package com.example.coupleapp.Model;

import android.graphics.Bitmap;

public class SelectImageData {

    private Bitmap bitmap;

    public SelectImageData(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public SelectImageData() {
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
