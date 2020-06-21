package com.example.coupleapp.Model.DateTotalModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface DateInterface {

    @GET("locationBasedList?serviceKey=7uXEosyKta2x4pTO3XdRh6WaKccTJg8QsHjXPEl6Jc7iVUSGwydtmr%2FRypr76U8gzM8mEOzdBdna5dvHVr%2Fx9Q%3D%3D&numOfRows=20&pageNo=1&MobileOS=AND&MobileApp=AppTest&radius=5000&listYN=Y&_type=json")
    Call<Date1> getAnswers(@Query("contentTypeId") int contenttypdid, @Query("mapX") double lon, @Query("mapY") double lat, @Query("arrange") String arrange);
}
