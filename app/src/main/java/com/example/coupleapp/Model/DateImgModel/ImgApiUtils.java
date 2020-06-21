package com.example.coupleapp.Model.DateImgModel;


import com.example.coupleapp.Model.DateTotalModel.RetrofitClient;

public class ImgApiUtils {

    public static final String BASE_URL = "http://api.visitkorea.or.kr/openapi/service/rest/KorService/";

    public static DateImgInterface getSOService() {
        return RetrofitClient.getClient(BASE_URL).create(DateImgInterface.class);
    }
}