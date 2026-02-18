package com.svalero.enajenarte.model;

import com.svalero.enajenarte.api.WorkshopApi;
import com.svalero.enajenarte.api.WorkshopApiInterface;
import com.svalero.enajenarte.contract.WorkshopListContract;
import com.svalero.enajenarte.domain.Workshop;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WorkshopListModel implements WorkshopListContract.Model {

    @Override
    public void loadWorkshops(String name, String isOnline, String speakerId, OnLoadListener listener) {

        // 1) Obtener API
        WorkshopApiInterface api = WorkshopApi.buildInstance();

        // 2) Crear llamada
        Call<List<Workshop>> call = api.getWorkshops(name, isOnline, speakerId);

        // 3) Ejecutar llamada asíncrona
        call.enqueue(new Callback<List<Workshop>>() {

            @Override
            public void onResponse(Call<List<Workshop>> call, Response<List<Workshop>> response) {

                if (response.isSuccessful()) {

                    List<Workshop> workshops = response.body();

                    // Spring devuelve 204 → body null
                    if (workshops == null) {
                        listener.onLoadSuccess(java.util.Collections.emptyList());
                        return;
                    }

                    listener.onLoadSuccess(workshops);

                } else {
                    listener.onLoadError("Error HTTP: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Workshop>> call, Throwable t) {
                listener.onLoadError(t.getMessage());
            }
        });
    }
}
