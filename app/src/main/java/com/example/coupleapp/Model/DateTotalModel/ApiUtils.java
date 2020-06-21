package com.example.coupleapp.Model.DateTotalModel;

public class ApiUtils {

    public static final String BASE_URL = "http://api.visitkorea.or.kr/openapi/service/rest/KorService/";

    public static DateInterface getSOService() {
        return RetrofitClient.getClient(BASE_URL).create(DateInterface.class);
    }
}