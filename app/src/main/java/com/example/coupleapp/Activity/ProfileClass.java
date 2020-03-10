package com.example.coupleapp.Activity;

import com.google.gson.annotations.SerializedName;

public class ProfileClass {

    @SerializedName("email")
    private String email;

    @SerializedName("name")
    private String name;

    @SerializedName("birthday")
    private String birthday;

    @SerializedName("sex")
    private String sex;

    @SerializedName("imgurl")
    private String imgurl;

    @SerializedName("date")
    private String date;

    @SerializedName("response")
    private String Response;

    public String getResponse() {
        return Response;
    }

}
