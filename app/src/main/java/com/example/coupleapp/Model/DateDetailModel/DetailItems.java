package com.example.coupleapp.Model.DateDetailModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DetailItems {

    @SerializedName("item")
    @Expose
    private DetailItem item;

    public DetailItem getItem() {
        return item;
    }

    public void setItem(DetailItem item) {
        this.item = item;
    }

}