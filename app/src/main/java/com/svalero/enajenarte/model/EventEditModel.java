package com.svalero.enajenarte.model;

import com.svalero.enajenarte.api.EventApi;
import com.svalero.enajenarte.api.EventApiInterface;
import com.svalero.enajenarte.contract.EventEditContract;
import com.svalero.enajenarte.domain.Event;
import com.svalero.enajenarte.domain.Workshop;
import com.svalero.enajenarte.domain.request.EventRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class EventEditModel implements EventEditContract.Model {

    @Override
    public void updateEvent(long id, EventRequest eventRequest, OnUpdateListener listener) {

        // El Model se encarga de acceder a la API (fuente de datos).
        // Aquí construimos la interfaz de Retrofit para ejecutar la llamada HTTP.
        EventApiInterface apiInterface = EventApi.buildInstance();

        // Llamada PUT /events/{id}.
        // Delegamos en Retrofit y usamos enqueue (asíncrono) para no bloquear la UI.
        apiInterface.updateEvent(id, eventRequest).enqueue(new Callback<Event>() {
            @Override
            public void onResponse(Call<Event> call, Response<Event> response) {

                // Si la respuesta es correcta y viene un body, notificamos éxito al Presenter.
                if (response.isSuccessful() && response.body() != null) {
                    listener.onUpdateSuccess(response.body());
                    return;
                }

                // Si no es successful, construimos un mensaje básico.
                String errorText = "Error HTTP: " + response.code();
                try {
                    if (response.errorBody() != null) {
                        errorText += " - " + response.errorBody().string();
                    }
                } catch (Exception ignored) {}

                // Notificamos el error al Presenter.
                listener.onUpdateError(errorText);
            }

            @Override
            public void onFailure(Call<Event> call, Throwable throwable) {
                // Error de red / excepción: notificamos el mensaje al Presenter.
                listener.onUpdateError(throwable.getMessage());
            }
        });
    }

    @Override
    public void createEvent(EventRequest eventRequest, OnUpdateListener listener) {

        // El Model se encarga de acceder a la API.
        EventApiInterface apiInterface = EventApi.buildInstance();

        // Llamada POST /events.
        apiInterface.createEvent(eventRequest).enqueue(new Callback<Event>() {
            @Override
            public void onResponse(Call<Event> call, Response<Event> response) {

                // Si la respuesta es correcta y viene un body, notificamos éxito al Presenter.
                if (response.isSuccessful() && response.body() != null) {
                    listener.onUpdateSuccess(response.body());
                } else {
                    // Si no es successful, notificamos el error simple con el código HTTP.
                    listener.onUpdateError("Error HTTP: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Event> call, Throwable throwable) {
                // Error de red / excepción.
                listener.onUpdateError(throwable.getMessage());
            }
        });
    }
}


