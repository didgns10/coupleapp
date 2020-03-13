package com.example.coupleapp.Activity;

import com.google.gson.annotations.SerializedName;

public class PhotoClass {

    @SerializedName("couple_idx")
    private String couple_idx;

    @SerializedName("image")
    private String Image;

    @SerializedName("album")
    private String album;


    @SerializedName("response")
    private String Response;

    public String getResponse() {
        return Response;
    }
}
