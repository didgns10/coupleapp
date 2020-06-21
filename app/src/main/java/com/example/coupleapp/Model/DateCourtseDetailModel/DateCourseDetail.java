package com.example.coupleapp.Model.DateCourtseDetailModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DateCourseDetail {

    @SerializedName("response")
    @Expose
    private DateCourseDetailResponse response;

    public DateCourseDetailResponse getResponse() {
        return response;
    }

    public void setResponse(DateCourseDetailResponse response) {
        this.response = response;
    }

}
