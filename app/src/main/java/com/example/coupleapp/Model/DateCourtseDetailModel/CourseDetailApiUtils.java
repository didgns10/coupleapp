package com.example.coupleapp.Model.DateCourtseDetailModel;


import com.example.coupleapp.Model.DateTotalModel.RetrofitClient;

public class CourseDetailApiUtils {

    public static final String BASE_URL = "http://api.visitkorea.or.kr/openapi/service/rest/KorService/";

    public static DateCourseDetailInterface getSOService() {
        return RetrofitClient.getClient(BASE_URL).create(DateCourseDetailInterface.class);
    }
}