package com.svalero.enajenarte.model;

import com.svalero.enajenarte.api.WorkshopApi;
import com.svalero.enajenarte.api.WorkshopApiInterface;
import com.svalero.enajenarte.contract.WorkshopEditContract;
import com.svalero.enajenarte.domain.Workshop;
import com.svalero.enajenarte.domain.request.WorkshopRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WorkshopEditModel implements WorkshopEditContract.Model {

    @Override
    public void updateWorkshop(long id, WorkshopRequest workshopRequest, OnUpdateListener listener) {

        // El Model se encarga de acceder a la API (fuente de datos).
        // Aquí construimos la interfaz de Retrofit para ejecutar la llamada HTTP.
        WorkshopApiInterface WorkkshopApiInterface = WorkshopApi.buildInstance();

        // Llamada PUT /workshops/{id}.
        // Delegamos en Retrofit y usamos enqueue (asíncrono) para no bloquear la UI.
        WorkkshopApiInterface.updateWorkshop(id, workshopRequest).enqueue(new Callback<Workshop>() {
            @Override
            public void onResponse(Call<Workshop> call, Response<Workshop> response) {

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
            public void onFailure(Call<Workshop> call, Throwable throwable) {
                // Error de red / excepción: notificamos el mensaje al Presenter.
                listener.onUpdateError(throwable.getMessage());
            }
        });
    }

    @Override
    public void createWorkshop(WorkshopRequest workshopRequest, OnUpdateListener listener) {

        // El Model se encarga de acceder a la API.
        WorkshopApiInterface apiInterface = WorkshopApi.buildInstance();

        // Llamada POST /workshops
        apiInterface.createWorkshop(workshopRequest).enqueue(new Callback<Workshop>() {
            @Override
            public void onResponse(Call<Workshop> call, Response<Workshop> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Si la respuesta es correcta y viene un body, notificamos éxito al Presenter.
                    listener.onUpdateSuccess(response.body());
                } else {
                    // Si no es successful, notificamos el error simple con el código HTTP.
                    listener.onUpdateError("Error HTTP: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Workshop> call, Throwable throwable) {
                // Error de red / excepción.
                listener.onUpdateError(throwable.getMessage());
            }
        });
    }
}
