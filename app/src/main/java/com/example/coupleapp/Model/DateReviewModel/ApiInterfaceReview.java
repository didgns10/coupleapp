package com.example.coupleapp.Model.DateReviewModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiInterfaceReview {

    @FormUrlEncoded
    @POST("review_add.php")
    Call<ReviewClass> uploadReview(@Field("name") String name,
                                   @Field("profile") String profile,
                                   @Field("date") String date,
                                   @Field("rate") String rate,
                                   @Field("review") String review,
                                   @Field("review_img") String review_img,
                                   @Field("contentid") String contentid,
                                   @Field("contenttypeid") String contenttypeid
    );
    @FormUrlEncoded
    @POST("review_update.php")
    Call<ReviewClass> updateReview(@Field("name") String name,
                                   @Field("profile") String profile,
                                   @Field("date") String date,
                                   @Field("rate") String rate,
                                   @Field("review") String review,
                                   @Field("review_img") String review_img,
                                   @Field("contentid") String contentid,
                                   @Field("contenttypeid") String contenttypeid,
                                   @Field("review_idx") String review_idx

    );

}
