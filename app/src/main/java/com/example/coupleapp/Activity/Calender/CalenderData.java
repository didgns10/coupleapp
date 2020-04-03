package com.example.coupleapp.Activity.Calender;

public class CalenderData {
    private String title,idx,date;

    public CalenderData(String title, String idx, String date) {
        this.title = title;
        this.idx = idx;
        this.date = date;
    }

    public CalenderData() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIdx() {
        return idx;
    }

    public void setIdx(String idx) {
        this.idx = idx;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
