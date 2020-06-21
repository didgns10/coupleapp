package com.example.coupleapp.Model.DateDetailModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DetailItem {

    @SerializedName("addr1")
    @Expose
    private String addr1;
    @SerializedName("addr2")
    @Expose
    private String addr2;
    @SerializedName("booktour")
    @Expose
    private Long booktour;
    @SerializedName("contentid")
    @Expose
    private Long contentid;
    @SerializedName("contenttypeid")
    @Expose
    private Long contenttypeid;
    @SerializedName("createdtime")
    @Expose
    private Long createdtime;
    @SerializedName("firstimage")
    @Expose
    private String firstimage;
    @SerializedName("firstimage2")
    @Expose
    private String firstimage2;
    @SerializedName("homepage")
    @Expose
    private String homepage;
    @SerializedName("modifiedtime")
    @Expose
    private Long modifiedtime;
    @SerializedName("overview")
    @Expose
    private String overview;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("zipcode")
    @Expose
    private String zipcode;

    public String getAddr1() {
        return addr1;
    }

    public void setAddr1(String addr1) {
        this.addr1 = addr1;
    }

    public String getAddr2() {
        return addr2;
    }

    public void setAddr2(String addr2) {
        this.addr2 = addr2;
    }

    public Long getBooktour() {
        return booktour;
    }

    public void setBooktour(Long booktour) {
        this.booktour = booktour;
    }

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

    public Long getCreatedtime() {
        return createdtime;
    }

    public void setCreatedtime(Long createdtime) {
        this.createdtime = createdtime;
    }

    public String getFirstimage() {
        return firstimage;
    }

    public void setFirstimage(String firstimage) {
        this.firstimage = firstimage;
    }

    public String getFirstimage2() {
        return firstimage2;
    }

    public void setFirstimage2(String firstimage2) {
        this.firstimage2 = firstimage2;
    }

    public String getHomepage() {
        return homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    public Long getModifiedtime() {
        return modifiedtime;
    }

    public void setModifiedtime(Long modifiedtime) {
        this.modifiedtime = modifiedtime;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

}

