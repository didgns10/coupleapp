package com.example.coupleapp.Model.DateImgModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DateImgItems {

    @SerializedName("item")
    @Expose
    private List<DateImgItem> item = null;

    public List<DateImgItem> getItem() {
        return item;
    }

    public void setItem(List<DateImgItem> item) {
        this.item = item;
    }

}
