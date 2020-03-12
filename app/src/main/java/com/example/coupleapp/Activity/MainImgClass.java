package com.example.coupleapp.Activity;

import com.google.gson.annotations.SerializedName;

public class MainImgClass {

    @SerializedName("email")
    private String email;

    @SerializedName("imgurl")
    private String imgurl;

    @SerializedName("idx")
    private String idx;

    @SerializedName("response")
    private String Response;

    public String getResponse() {
        return Response;
    }
}
