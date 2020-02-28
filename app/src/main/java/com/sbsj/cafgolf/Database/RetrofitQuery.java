package com.sbsj.cafgolf.Database;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface RetrofitQuery {

//    @FormUrlEncoded
//    @POST("get_memberinfo.php")
//    Call<String> get_memberinfo(@Field("directory") String directory, @Field("sns_id") String sns_id, @Field("fb_token") String fb_token); // directory, sns_id

    @FormUrlEncoded
    @POST("get_memberinfo_id.php")
    Call<MemberInfoField> get_memberinfo_id(@Field("id") int id); // directory, sns_id

    @FormUrlEncoded
    @POST("get_memberinfo_sns.php")
    Call<MemberInfoField> get_memberinfo_sns(@Field("directory") String directory, @Field("sns_id") String sns_id); // directory, sns_id

    @FormUrlEncoded
    @POST("insert_memberinfo.php")
    Call<String> insert_memberinfo(@Field("directory") String directory, @Field("sns_id") String sns_id, @Field("fb_token") String fb_token);

    @FormUrlEncoded
    @POST("update_memberinfo_name.php")
    Call<String> update_memberinfo_name(@Field("id") int id, @Field("name") String name); // id, name

    @FormUrlEncoded
    @POST("update_memberinfo_lessoncount.php")
    Call<String> update_memberinfo_lessoncount(@Field("id") int id, @Field("lesson_count") int lesson_count); // id, lesson_count

    @FormUrlEncoded
    @POST("update_memberinfo_token.php")
    Call<String> update_memberinfo_token(@Field("id") int id, @Field("fb_token") String fb_token); // id, fb_token

    @GET("get_bookinginfo_you.php")
    Call<BookingInfoList> get_bookinginfo_you(@Query("clickdate") String clickdate);

    @GET("get_bookinginfo_my.php")
    Call<BookingInfoList> get_bookinginfo_my(@Query("member_id") int member_id);

    @FormUrlEncoded
    @POST("insert_bookinginfo.php")
    Call<String> insert_bookinginfo(@Field("member_id") int member_id, @Field("book_date") String book_date);

    @GET("get_lessontime.php")
    Call<LessonTimeList> get_lessontime();

    @POST("get_masterinfo.php")
    Call<MasterInfoList> get_masterinfo();
}
