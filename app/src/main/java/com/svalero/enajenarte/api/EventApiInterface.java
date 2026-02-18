package com.svalero.enajenarte.api;

import com.svalero.enajenarte.domain.Event;
import com.svalero.enajenarte.domain.request.EventRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface EventApiInterface {

    @GET("events")
    Call<List<Event>> getEvents(
            @Query("title") String title,
            @Query("location") String location,
            @Query("isPublic") String isPublic
    );

    @POST("events")
    Call<Event> createEvent(@Body EventRequest eventRequest);

    @GET("events/{id}")
    Call<Event> getEvent(@Path("id") long id);

    @PUT("events/{id}")
    Call<Event> updateEvent(@Path("id") long id, @Body EventRequest eventRequest);

    @DELETE("events/{id}")
    Call<Void> deleteEvent(@Path("id") long id);
}
