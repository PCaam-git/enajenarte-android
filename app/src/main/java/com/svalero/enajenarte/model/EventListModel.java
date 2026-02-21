package com.svalero.enajenarte.model;

import com.svalero.enajenarte.api.EventApi;
import com.svalero.enajenarte.api.EventApiInterface;
import com.svalero.enajenarte.contract.EventListContract;
import com.svalero.enajenarte.domain.Event;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventListModel implements EventListContract.Model {

    @Override
    public void loadEvents(String title, String location, Boolean isPublic, OnLoadListener listener) {

        EventApiInterface api = EventApi.buildInstance();
        Call<List<Event>> call = api.getEvents(title, location, isPublic);

        call.enqueue(new Callback<List<Event>>() {
            @Override
            public void onResponse(Call<List<Event>> call, Response<List<Event>> response) {

                if (response.isSuccessful()) {
                    List<Event> events = response.body();

                    // si backend devuelve 204, body null
                    if (events == null) {
                        listener.onLoadSuccess(java.util.Collections.emptyList());
                        return;
                    }
                    listener.onLoadSuccess(events);
                    } else {
                    listener.onLoadError("Error HTTP: " + response.code());

                }
            }

            @Override
            public void onFailure(Call<List<Event>> call, Throwable throwable) {
                listener.onLoadError(throwable.getMessage());

            }
        });
    }

    @Override
    public void deleteEvent(long id, OnDeleteListener listener) {

        EventApiInterface eventApiInterface = EventApi.buildInstance();

        eventApiInterface.deleteEvent(id).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    listener.onDeleteSuccess();
                } else {
                    listener.onDeleteError("Error HTTP: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable throwable) {
                listener.onDeleteError(throwable.getMessage());
            }
        });
    }
}
