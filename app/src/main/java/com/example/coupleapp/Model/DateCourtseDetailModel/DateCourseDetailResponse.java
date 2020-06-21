package com.example.coupleapp.Model.DateCourtseDetailModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DateCourseDetailResponse {

    @SerializedName("header")
    @Expose
    private DateCourseDetailHeader header;
    @SerializedName("body")
    @Expose
    private DateCourseDetailBody body;

    public DateCourseDetailHeader getHeader() {
        return header;
    }

    public void setHeader(DateCourseDetailHeader header) {
        this.header = header;
    }

    public DateCourseDetailBody getBody() {
        return body;
    }

    public void setBody(DateCourseDetailBody body) {
        this.body = body;
    }

}