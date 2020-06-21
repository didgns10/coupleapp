package com.example.coupleapp.Model.DateCourtseDetailModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DateCourseDetailItems {

    @SerializedName("item")
    @Expose
    private List<DateCourseDetailItem> item = null;

    public List<DateCourseDetailItem> getItem() {
        return item;
    }

    public void setItem(List<DateCourseDetailItem> item) {
        this.item = item;
    }

}