package com.example.coupleapp.Activity;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiInterface {

    @FormUrlEncoded
    @POST("mainimg.php")
    Call<MainImgClass> uploadImage(@Field("email") String email, @Field("imgurl") String imgurl,@Field("idx") String idx);

    @FormUrlEncoded
    @POST("profile.php")
    Call<ProfileClass> uploadImage1(@Field("email") String email,
                                    @Field("name") String name,
                                    @Field("birthday") String birthday,
                                    @Field("sex") String sex,
                                    @Field("imgurl") String imgurl,
                                    @Field("date") String date
    );

    @FormUrlEncoded
    @POST("image_upload.php")
    Call<ImageClass> uploadImage(@Field("couple_idx") String couple_idx,
                                    @Field("title") String title,
                                    @Field("image") String image,
                                    @Field("thumb") String thumb,
                                    @Field("day") String day,
                                    @Field("album") String album
    );
    @FormUrlEncoded
    @POST("photo_upload.php")
    Call<PhotoClass> uploadImage2(@Field("couple_idx") String couple_idx,
                                 @Field("image") String image,
                                 @Field("album") String album
    );
    @FormUrlEncoded
    @POST("message_upload.php")
    Call<MessageClass> uploadMessage(@Field("couple_idx") String couple_idx,
                                  @Field("image") String image,
                                  @Field("name") String name,
                                  @Field("message") String message,
                                  @Field("datem") String datem,
                                  @Field("email") String email
    );

}
