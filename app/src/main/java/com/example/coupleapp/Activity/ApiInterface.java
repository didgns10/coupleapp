package com.example.coupleapp.Activity;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiInterface {

/*    @FormUrlEncoded
    @POST("uploads.php")
    Call<ImageClass> uploadImage(@Field("title") String title, @Field("image") String image);*/

    @FormUrlEncoded
    @POST("profile.php")
    Call<ProfileClass> uploadImage1(@Field("email") String email,
                                    @Field("name") String name,
                                    @Field("birthday") String birthday,
                                    @Field("sex") String sex,
                                    @Field("imgurl") String imgurl,
                                    @Field("date") String date
    );

}
