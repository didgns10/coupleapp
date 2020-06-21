package com.example.coupleapp.Model.DateCourtseDetailModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DateCourseDetailItem {

    @SerializedName("contentid")
    @Expose
    private Long contentid;
    @SerializedName("contenttypeid")
    @Expose
    private Long contenttypeid;
    @SerializedName("subcontentid")
    @Expose
    private Long subcontentid;
    @SerializedName("subdetailalt")
    @Expose
    private String subdetailalt;
    @SerializedName("subdetailimg")
    @Expose
    private String subdetailimg;
    @SerializedName("subdetailoverview")
    @Expose
    private String subdetailoverview;
    @SerializedName("subname")
    @Expose
    private String subname;
    @SerializedName("subnum")
    @Expose
    private Long subnum;

    public Long getContentid() {
        return contentid;
    }

    public void setContentid(Long contentid) {
        this.contentid = contentid;
    }

    public Long getContenttypeid() {
        return contenttypeid;
    }

    public void setContenttypeid(Long contenttypeid) {
        this.contenttypeid = contenttypeid;
    }

    public Long getSubcontentid() {
        return subcontentid;
    }

    public void setSubcontentid(Long subcontentid) {
        this.subcontentid = subcontentid;
    }

    public String getSubdetailalt() {
        return subdetailalt;
    }

    public void setSubdetailalt(String subdetailalt) {
        this.subdetailalt = subdetailalt;
    }

    public String getSubdetailimg() {
        return subdetailimg;
    }

    public void setSubdetailimg(String subdetailimg) {
        this.subdetailimg = subdetailimg;
    }

    public String getSubdetailoverview() {
        return subdetailoverview;
    }

    public void setSubdetailoverview(String subdetailoverview) {
        this.subdetailoverview = subdetailoverview;
    }

    public String getSubname() {
        return subname;
    }

    public void setSubname(String subname) {
        this.subname = subname;
    }

    public Long getSubnum() {
        return subnum;
    }

    public void setSubnum(Long subnum) {
        this.subnum = subnum;
    }

}
