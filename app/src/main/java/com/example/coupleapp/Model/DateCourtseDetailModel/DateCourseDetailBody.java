package com.example.coupleapp.Model.DateCourtseDetailModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DateCourseDetailBody {

    @SerializedName("items")
    @Expose
    private DateCourseDetailItems items;
    @SerializedName("numOfRows")
    @Expose
    private Long numOfRows;
    @SerializedName("pageNo")
    @Expose
    private Long pageNo;
    @SerializedName("totalCount")
    @Expose
    private Long totalCount;

    public DateCourseDetailItems getItems() {
        return items;
    }

    public void setItems(DateCourseDetailItems items) {
        this.items = items;
    }

    public Long getNumOfRows() {
        return numOfRows;
    }

    public void setNumOfRows(Long numOfRows) {
        this.numOfRows = numOfRows;
    }

    public Long getPageNo() {
        return pageNo;
    }

    public void setPageNo(Long pageNo) {
        this.pageNo = pageNo;
    }

    public Long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }

}