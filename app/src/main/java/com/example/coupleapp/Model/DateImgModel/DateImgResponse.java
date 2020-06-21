package com.example.coupleapp.Model.DateImgModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DateImgResponse {

    @SerializedName("header")
    @Expose
    private DateImgHeader header;
    @SerializedName("body")
    @Expose
    private DateImgBody body;

    public DateImgHeader getHeader() {
        return header;
    }

    public void setHeader(DateImgHeader header) {
        this.header = header;
    }

    public DateImgBody getBody() {
        return body;
    }

    public void setBody(DateImgBody body) {
        this.body = body;
    }

}
