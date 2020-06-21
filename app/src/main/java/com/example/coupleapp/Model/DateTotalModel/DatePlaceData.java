package com.example.coupleapp.Model.DateTotalModel;

public class DatePlaceData {
    //api에서 받아온 데이터
    private String title,addr1,contentid,contenttypeid,firstimage,firstimage2,readcount,tel;

    public DatePlaceData(String title, String addr1, String contentid, String contenttypeid, String firstimage, String firstimage2, String readcount, String tel) {
        this.title = title;
        this.addr1 = addr1;
        this.contentid = contentid;
        this.contenttypeid = contenttypeid;
        this.firstimage = firstimage;
        this.firstimage2 = firstimage2;
        this.readcount = readcount;
        this.tel = tel;
    }

    public DatePlaceData() {

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddr1() {
        return addr1;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public void setAddr1(String addr1) {
        this.addr1 = addr1;
    }


    public String getContentid() {
        return contentid;
    }

    public void setContentid(String contentid) {
        this.contentid = contentid;
    }

    public String getContenttypeid() {
        return contenttypeid;
    }

    public void setContenttypeid(String contenttypeid) {
        this.contenttypeid = contenttypeid;
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

    public String getReadcount() {
        return readcount;
    }

    public void setReadcount(String readcount) {
        this.readcount = readcount;
    }
}
