package com.svalero.enajenarte.api;

import com.svalero.enajenarte.domain.Registration;
import com.svalero.enajenarte.domain.request.RegistrationRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RegistrationApiInterface {

    @GET("registrations")
    Call<List<Registration>> getRegistrations(
            @Query("userId") String userId,
            @Query("workshopId") String workshopId,
            @Query("isPaid") String isPaid
    );

    @POST("registrations")
    Call<Registration> createRegistration(@Body RegistrationRequest registrationRequest);

    @GET("registrations/{id}")
    Call<Registration> getRegistration(@Path("id") long id);

    @PUT("registrations/{id}")
    Call<Registration> updateRegistration(@Path("id") long id, @Body RegistrationRequest registrationRequest);

    @DELETE("registrations/{id}")
    Call<Void> deleteRegistration(@Path("id") long id);
}
