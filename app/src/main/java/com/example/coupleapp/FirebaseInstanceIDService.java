package com.example.coupleapp;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class FirebaseInstanceIDService {

    //@Override
    public void onTokenRefresh() {

        String token = FirebaseInstanceId.getInstance().getToken();
        
       // registerToken(token);

        Log.e("토큰",token);

    }

    private void registerToken(String token) {

        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("Token",token)
                .build();

        Request request = new Request.Builder().url("http://13.125.232.78/fcm.php").build();

        try {
            client.newCall(request).execute();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
