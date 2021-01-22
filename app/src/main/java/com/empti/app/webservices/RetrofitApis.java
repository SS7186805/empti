package com.empti.app.webservices;

import com.empti.app.activity.MapModel;
import com.empti.app.model.BatchesModel;
import com.empti.app.model.LeaderboardModel;
import com.empti.app.model.NewBadgeModel;

import java.util.HashMap;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

//Apis Used interface
public interface RetrofitApis {
    @Headers("Content-Type: application/json")
    @POST("api/login")
    Call<ResponseBody> login(@Body RequestBody responseBody);

    //    @Headers("Content-Type: application/json")
    @Multipart
    @POST("api/register")
    Call<ResponseBody> signup(@PartMap HashMap<String, RequestBody> responseBody, @Part MultipartBody.Part part);

    @Headers("Content-Type: application/json")
    @POST("api/register")
    Call<ResponseBody> signup(@Body RequestBody responseBody);

    @Headers("Content-Type: application/json")
    @POST("api/user/authentication")
    Call<ResponseBody> checkEmail(@Body RequestBody responseBody);

    @Headers("Content-Type: application/json")
    @POST("api/user/checksocialaccount")
    Call<ResponseBody> checkSocial(@Body RequestBody responseBody);

    @FormUrlEncoded
    @POST("api/user/forgot_password")
    Call<ResponseBody> recoverPassword(@FieldMap Map<String, String> param);

    @FormUrlEncoded
    @POST("api/user/reset_password")
    Call<ResponseBody> resetPassword(@FieldMap Map<String, String> param);

    @Headers("Content-Type: application/json")
    @POST("api/user/nearby_outlet")
    Call<MapModel> data(@Header("Authorization") String auth, @Body RequestBody responseBody);

    //  @Headers("Content-Type: application/json")
    @Multipart
    @POST("api/user/editprofile")
    Call<ResponseBody> editProfile(@Header("Authorization") String auth, @PartMap HashMap<String, RequestBody> responseBody, @Part MultipartBody.Part part);

    @Headers("Content-Type: application/json")
    @POST("api/user/editprofile")
    Call<ResponseBody> editProfile(@Header("Authorization") String auth, @Body RequestBody responseBody);

    @Headers("Content-Type: application/json")
    @POST("api/user/outletDetail")
    Call<ResponseBody> outletdata(@Header("Authorization") String auth, @Body RequestBody responseBody);

    @Headers("Content-Type: application/json")
    @POST("api/user/deleteuser")
    Call<ResponseBody> deleteAccount(@Header("Authorization") String auth);

    @Headers("Content-Type: application/json")
    @GET("api/user/myoutletDetail")
    Call<ResponseBody> outletInfo(@Header("Authorization") String auth);

    @Headers("Content-Type: application/json")
    @GET("api/containers")
    Call<ResponseBody> outletContainerInfo(@Header("Authorization") String auth);

    @Headers("Content-Type: application/json")
    @POST("api/outletcontainer/add")
    Call<ResponseBody> addContainers(@Header("Authorization") String auth, @Body RequestBody responseBody);

    @Headers("Content-Type: application/json")
    @POST("api/outletcontainer/filtered_container")
    Call<ResponseBody> filterContainers(@Header("Authorization") String auth, @Body RequestBody responseBody);

    @Headers("Content-Type: application/json")
    @DELETE()
    Call<ResponseBody> deleteContainers(@Header("Authorization") String auth, @Url String responseBody);

    @Headers("Content-Type: application/json")
    @GET("api/faq")
    Call<ResponseBody> getFAQ(@Header("Authorization") String auth);

    @Headers("Content-Type: application/json")
    @PUT("api/outletcontainer/edit")
    Call<ResponseBody> editContainers(@Header("Authorization") String auth, @Body RequestBody responseBody);

    @Headers("Content-Type: application/json")
    @POST("api/user/change_password")
    Call<ResponseBody> changePassword(@Header("Authorization") String auth, @Body RequestBody responseBody);

    @Headers("Content-Type: application/json")
    @POST("api/user/all_users_containers")
    Call<ResponseBody> myContainerData(@Header("Authorization") String auth, @Body RequestBody responseBody);

    @Headers("Content-Type: application/json")
    @POST("api/outletcontainer/detail")
    Call<ResponseBody> containerDetails(@Header("Authorization") String auth, @Body RequestBody responseBody);

    @Headers("Content-Type: application/json")
    @POST("api/outletcontainer/hire_return1")
    Call<ResponseBody> hireReturnApi(@Header("Authorization") String auth, @Body RequestBody responseBody);

    @Headers("Content-Type: application/json")
    @POST("api/outletcontainer/hire_return1")
    Call<NewBadgeModel> hireReturnApiModel(@Header("Authorization") String auth, @Body RequestBody responseBody);

    @Headers("Content-Type: application/json")
    @PUT("api/outletcontainer/change_container_ready_to_use")
    Call<ResponseBody> changeStatusContainer(@Header("Authorization") String auth, @Body RequestBody responseBody);

    @Headers("Content-Type: application/json")
    @GET("api/user/renew_token")
    Call<ResponseBody> getTokenRefersh(@Header("Authorization") String auth);

    @Headers("Content-Type: application/json")
    @POST("api/outletcontainer/remind_notification")
    Call<ResponseBody> remindNotifications(@Header("Authorization") String auth, @Body RequestBody responseBody);

    @Headers("Content-Type: application/json")
    @POST("api/contactus")
    Call<ResponseBody> contactUs(@Header("Authorization") String auth, @Body RequestBody responseBody);

    @Headers("Content-Type: application/json")
    @PUT("api/user/notification_token")
    Call<ResponseBody> updateToken(@Header("Authorization") String auth, @Body RequestBody responseBody);

    @Headers("Content-Type: application/json")
    @GET("api/batches")
    Call<BatchesModel> getBatches(@Header("Authorization") String auth, @Query("user_id") String userId);

    @Headers("Content-Type: application/json")
    @GET("api/batches")
    Call<BatchesModel> getOtherBatchUser(@Header("Authorization") String auth, @Header("user_id") String user);

    @Headers("Content-Type: application/json")
    @GET("api/batches/leaderboard")
    Call<LeaderboardModel> getLeadersList(@Header("Authorization") String auth, @Query("page") String pageNo);

    @Headers("Content-Type: application/json")
    @POST("api/payment")
    Call<ResponseBody> paymentApi(@Header("Authorization") String auth, @Body RequestBody responseBody);


}
