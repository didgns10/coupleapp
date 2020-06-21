package com.example.coupleapp.Activity.DateCourse;

public class CourseRecommendData {

    private String img,title,contentid;

    public CourseRecommendData() {
    }

    public CourseRecommendData(String img, String title, String contentid) {
        this.img = img;
        this.title = title;
        this.contentid = contentid;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContentid() {
        return contentid;
    }

    public void setContentid(String contentid) {
        this.contentid = contentid;
    }
}
