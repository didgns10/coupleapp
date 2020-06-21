package com.example.coupleapp.Model.DateCourtseDetailModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface DateCourseDetailInterface {

    @GET("detailInfo?serviceKey=7uXEosyKta2x4pTO3XdRh6WaKccTJg8QsHjXPEl6Jc7iVUSGwydtmr%2FRypr76U8gzM8mEOzdBdna5dvHVr%2Fx9Q%3D%3D&numOfRows=10&pageNo=1&MobileOS=AND&MobileApp=coupleapp&contentTypeId=25&_type=json")
    Call<DateCourseDetail> getAnswers(@Query("contentId") String contentid);
}
