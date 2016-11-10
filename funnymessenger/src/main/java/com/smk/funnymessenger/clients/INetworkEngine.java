package com.smk.funnymessenger.clients;

import com.smk.funnymessenger.models.User;

import java.util.List;

import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;

public interface INetworkEngine {

    @FormUrlEncoded
    @POST("/api/user")
    void postUser(
            @Field("name") String name,
            @Field("email") String email,
            @Field("phone") String phone,
            @Field("image") String image,
            @Field("device_id") String device_id,
            @Field("client_id") Integer client_id,
            Callback<User> callback);

    @GET("/api/user")
    void getUsers(@Query("client_id") Integer client_id, Callback<List<User>> callback);

    @GET("/api/user/{id}")
    void getUser(@Path("id") Integer id,@Query("client_id") Integer client_id, Callback<User> callback);

}
