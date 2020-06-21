package com.example.coupleapp.Model.DateImgModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DateImgItem {

    @SerializedName("contentid")
    @Expose
    private Long contentid;
    @SerializedName("originimgurl")
    @Expose
    private String originimgurl;
    @SerializedName("serialnum")
    @Expose
    private String serialnum;
    @SerializedName("smallimageurl")
    @Expose
    private String smallimageurl;

    public Long getContentid() {
        return contentid;
    }

    public void setContentid(Long contentid) {
        this.contentid = contentid;
    }

    public String getOriginimgurl() {
        return originimgurl;
    }

    public void setOriginimgurl(String originimgurl) {
        this.originimgurl = originimgurl;
    }

    public String getSerialnum() {
        return serialnum;
    }

    public void setSerialnum(String serialnum) {
        this.serialnum = serialnum;
    }

    public String getSmallimageurl() {
        return smallimageurl;
    }

    public void setSmallimageurl(String smallimageurl) {
        this.smallimageurl = smallimageurl;
    }

}
