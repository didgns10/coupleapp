package com.example.coupleapp.Activity.Calender;

public class DiaryCommentData {
    private String name,idx,diary_idx,date,comment;

    //댓글 삭제시 디테일로 넘어가기위해서 인테트를 줘야하는데 그러기위ㅎ새ㅓ 디테일의 값들이 필요하다
    private String diary_title,diary_date,diary_content,diary_name,diary_focus,diary_time;

    public String getDiary_title() {
        return diary_title;
    }

    public void setDiary_title(String diary_title) {
        this.diary_title = diary_title;
    }

    public String getDiary_date() {
        return diary_date;
    }

    public void setDiary_date(String diary_date) {
        this.diary_date = diary_date;
    }

    public String getDiary_content() {
        return diary_content;
    }

    public void setDiary_content(String diary_content) {
        this.diary_content = diary_content;
    }

    public String getDiary_name() {
        return diary_name;
    }

    public void setDiary_name(String diary_name) {
        this.diary_name = diary_name;
    }

    public String getDiary_focus() {
        return diary_focus;
    }

    public void setDiary_focus(String diary_focus) {
        this.diary_focus = diary_focus;
    }

    public String getDiary_time() {
        return diary_time;
    }

    public void setDiary_time(String diary_time) {
        this.diary_time = diary_time;
    }

    public DiaryCommentData(String name, String idx, String diary_idx, String date, String comment) {
        this.name = name;
        this.idx = idx;
        this.diary_idx = diary_idx;
        this.date = date;
        this.comment = comment;
    }

    public DiaryCommentData() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdx() {
        return idx;
    }

    public void setIdx(String idx) {
        this.idx = idx;
    }

    public String getDiary_idx() {
        return diary_idx;
    }

    public void setDiary_idx(String diary_idx) {
        this.diary_idx = diary_idx;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
