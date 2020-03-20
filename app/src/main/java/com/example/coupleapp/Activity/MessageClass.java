package com.example.coupleapp.Activity;

import com.google.gson.annotations.SerializedName;

public class MessageClass {

    @SerializedName("couple_idx")
    private String couple_idx;

    @SerializedName("image")
    private String Image;

    @SerializedName("name")
    private String name;

    @SerializedName("email")
    private String email;

    @SerializedName("message")
    private String message;

    @SerializedName("datem")
    private String datem;


    @SerializedName("response")
    private String Response;

    public String getResponse() {
        return Response;
    }

}
