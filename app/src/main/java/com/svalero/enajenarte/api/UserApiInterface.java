package com.svalero.enajenarte.api;

import com.svalero.enajenarte.domain.User;
import com.svalero.enajenarte.domain.request.UserRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UserApiInterface {

    @GET("users")
    Call<List<User>> getUsers(
            @Query("username") String username,
            @Query("email") String email,
            @Query("active") String active
    );

    @POST("users")
    Call<User> createUser(@Body UserRequest userRequest);

    @GET("users/{id}")
    Call<User> getUser(@Path("id") long id);

    @PUT("users/{id}")
    Call<User> updateUser(@Path("id") long id, @Body UserRequest userRequest);

    @DELETE("users/{id}")
    Call<Void> deleteUser(@Path("id") long id);
}
