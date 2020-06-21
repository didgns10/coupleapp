package com.example.coupleapp.Model.DateDetailModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface DateDetailInterface {

    @GET("detailCommon?serviceKey=7uXEosyKta2x4pTO3XdRh6WaKccTJg8QsHjXPEl6Jc7iVUSGwydtmr%2FRypr76U8gzM8mEOzdBdna5dvHVr%2Fx9Q%3D%3D&numOfRows=1&pageNo=1&MobileOS=AND&MobileApp=couple&defaultYN=Y&firstImageYN=Y&addrinfoYN=Y&overviewYN=Y&_type=json")
    Call<DateDetail> getAnswers(@Query("contentId") String contentid);
}
