package com.example.coupleapp.Model.DateImgModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DateImage {

    @SerializedName("response")
    @Expose
    private DateImgResponse response;

    public DateImgResponse getResponse() {
        return response;
    }

    public void setResponse(DateImgResponse response) {
        this.response = response;
    }

}