package com.example.coupleapp.Model.DateDetailModel;


import com.example.coupleapp.Model.DateTotalModel.RetrofitClient;

public class DetailApiUtils {

    public static final String BASE_URL = "http://api.visitkorea.or.kr/openapi/service/rest/KorService/";

    public static DateDetailInterface getSOService() {
        return RetrofitClient.getClient(BASE_URL).create(DateDetailInterface.class);
    }
}