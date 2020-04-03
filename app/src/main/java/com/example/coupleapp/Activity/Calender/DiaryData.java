package com.example.coupleapp.Activity.Calender;

public class DiaryData {

    private String title,idx,date,name,time,content;

    public DiaryData(String title, String idx, String date, String name, String time, String content) {
        this.title = title;
        this.idx = idx;
        this.date = date;
        this.name = name;
        this.time = time;
        this.content = content;
    }

    public DiaryData() {
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
