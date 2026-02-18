package com.svalero.enajenarte.api;

import com.svalero.enajenarte.domain.Workshop;
import com.svalero.enajenarte.domain.request.WorkshopRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface WorkshopApiInterface {

    @GET("workshops")
    Call<List<Workshop>> getWorkshops(
            @Query("name") String name,
            @Query("isOnline") String isOnline,
            @Query("speakerId") String speakerId
    );

    @POST("workshops")
    Call<Workshop> createWorkshop(@Body WorkshopRequest workshopRequest);

    @GET("workshops/{id}")
    Call<Workshop> getWorkshop(@Path("id") long id);

    @PUT("workshops/{id}")
    Call<Workshop> updateWorkshop(@Path("id") long id, @Body WorkshopRequest workshopRequest);

    @DELETE("workshops/{id}")
    Call<Void> deleteWorkshop(@Path("id") long id);
}
