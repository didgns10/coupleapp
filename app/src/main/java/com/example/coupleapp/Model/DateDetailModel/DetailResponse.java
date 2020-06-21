package com.example.coupleapp.Model.DateDetailModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DetailResponse {

    @SerializedName("header")
    @Expose
    private DetailHeader header;
    @SerializedName("body")
    @Expose
    private DetailBody body;

    public DetailHeader getHeader() {
        return header;
    }

    public void setHeader(DetailHeader header) {
        this.header = header;
    }

    public DetailBody getBody() {
        return body;
    }

    public void setBody(DetailBody body) {
        this.body = body;
    }

}