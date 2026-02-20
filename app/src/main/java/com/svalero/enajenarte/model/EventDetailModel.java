package com.svalero.enajenarte.model;

import com.svalero.enajenarte.api.EventApi;
import com.svalero.enajenarte.api.EventApiInterface;
import com.svalero.enajenarte.contract.EventDetailContract;
import com.svalero.enajenarte.domain.Event;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class EventDetailModel implements EventDetailContract.Model {

    @Override
    public void loadEvent(long id, OnLoadListener listener)  {
        EventApiInterface api = EventApi.buildInstance();

        api.getEvent(id).enqueue(new Callback<Event>() {
        @Override
            public void onResponse(Call<Event> call, Response<Event> response) {
            if (response.isSuccessful() && response.body() != null) {
                listener.onLoadSuccess(response.body());
            } else {
                listener.onLoadError("Error HTTP: " + response.code());
            }
        }

        @Override
            public void onFailure(Call<Event> call, Throwable throwable) {
            listener.onLoadError(throwable.getMessage());
            }
        });
    }
}
