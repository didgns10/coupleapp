package com.example.coupleapp.Model;

public class StoryVIewData {

    private String img_idx;
    private String title;
    private String img_day;
    private String img;

    public StoryVIewData(String img_idx, String title, String img_day, String img) {
        this.img_idx = img_idx;
        this.title = title;
        this.img_day = img_day;
        this.img = img;
    }

    public StoryVIewData() {
    }

    public String getImg_idx() {
        return img_idx;
    }

    public void setImg_idx(String img_idx) {
        this.img_idx = img_idx;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImg_day() {
        return img_day;
    }

    public void setImg_day(String img_day) {
        this.img_day = img_day;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
