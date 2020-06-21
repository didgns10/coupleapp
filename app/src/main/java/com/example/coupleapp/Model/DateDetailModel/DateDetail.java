package com.example.coupleapp.Model.DateDetailModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DateDetail {

    @SerializedName("response")
    @Expose
    private DetailResponse response;

    public DetailResponse getResponse() {
        return response;
    }

    public void setResponse(DetailResponse response) {
        this.response = response;
    }

}