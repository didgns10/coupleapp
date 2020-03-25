package com.example.coupleapp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.coupleapp.R;

public class ChatImagedatailActivity extends AppCompatActivity {

    private ImageView imgv_detail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_imagedatail);

        imgv_detail=findViewById(R.id.imgv_detail);

        Intent intent = getIntent();
        byte[] arr = getIntent().getByteArrayExtra("bitmap");
        Bitmap bitmap = BitmapFactory.decodeByteArray(arr, 0, arr.length);
        imgv_detail.setImageBitmap(bitmap);

    }
}
