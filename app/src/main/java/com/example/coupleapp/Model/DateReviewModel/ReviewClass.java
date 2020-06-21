package com.example.coupleapp.Model.DateReviewModel;

import com.google.gson.annotations.SerializedName;

public class ReviewClass {

    @SerializedName("name")
    private String name;

    @SerializedName("profile")
    private String profile;

    @SerializedName("date")
    private String date;

    @SerializedName("rate")
    private String rate;

    @SerializedName("review")
    private String review;

    @SerializedName("review_img")
    private String review_img;

    @SerializedName("contentid")
    private String contentid;

    @SerializedName("contenttypeid")
    private String contenttypeid;

    @SerializedName("review_idx")
    private String review_idx;

    @SerializedName("response")
    private String Response;

    public String getResponse() {
        return Response;
    }
}
