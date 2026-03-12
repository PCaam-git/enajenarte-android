package com.svalero.enajenarte.api;

import com.svalero.enajenarte.domain.Speaker;
import com.svalero.enajenarte.domain.request.SpeakerRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface SpeakerApiInterface {

    @GET("speakers")
    Call<List<Speaker>> getSpeakers(
            @Query("speciality") String speciality,
            @Query("available") String available,
            @Query("yearsExperience") String yearsExperience
    );

    @POST("speakers")
    Call<Speaker> createSpeaker(@Body SpeakerRequest speakerRequest);

    @GET("speakers/{id}")
    Call<Speaker> getSpeaker(@Path("id") long id);

    @PUT("speakers/{id}")
    Call<Speaker> updateSpeaker(@Path("id") long id, @Body SpeakerRequest speakerRequest);

    @DELETE("speakers/{id}")
    Call<Void> deleteSpeaker(@Path("id") long id);
}
