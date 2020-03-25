package com.example.coupleapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.coupleapp.R;

public class ChatImagedatail1Activity extends AppCompatActivity {

    private ImageView imgv_detail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_imagedatail);

        imgv_detail=findViewById(R.id.imgv_detail);

        String img = getIntent().getStringExtra("image");


        Glide.with(this).load(img)
                /*.diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)*/.into(imgv_detail);
    }
}
