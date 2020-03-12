package com.example.coupleapp.Activity;

import com.google.gson.annotations.SerializedName;

public class ImageClass {

    @SerializedName("couple_idx")
    private String couple_idx;

    @SerializedName("title")
    private String Title;

    @SerializedName("image")
    private String Image;

    @SerializedName("thumb")
    private String thumb;

    @SerializedName("day")
    private String day;

    @SerializedName("album")
    private String album;

    @SerializedName("response")
    private String Response;

    public String getResponse() {
        return Response;
    }
}
