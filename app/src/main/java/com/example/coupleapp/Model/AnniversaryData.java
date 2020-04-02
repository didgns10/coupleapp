package com.example.coupleapp.Model;

public class AnniversaryData implements Comparable<AnniversaryData>{
    private String dday,anniversary,date,idx,today;

    public AnniversaryData(String dday, String anniversary, String date, String idx, String today) {
        this.dday = dday;
        this.anniversary = anniversary;
        this.date = date;
        this.idx = idx;
        this.today = today;
    }

    public AnniversaryData() {
    }

    public String getIdx() {
        return idx;
    }

    public String getToday() {
        return today;
    }

    public void setToday(String today) {
        this.today = today;
    }

    public void setIdx(String idx) {
        this.idx = idx;
    }

    public String getDday() {
        return dday;
    }

    public void setDday(String dday) {
        this.dday = dday;
    }

    public String getAnniversary() {
        return anniversary;
    }

    public void setAnniversary(String anniversary) {
        this.anniversary = anniversary;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public int compareTo(AnniversaryData o) {
        return this.date.compareTo(o.date);
    }
}
