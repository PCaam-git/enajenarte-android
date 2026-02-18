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

        WorkshopApiInterface api = WorkshopApi.buildInstance();
        Call<List<Workshop>> call = api.getWorkshops(name, isOnline, speakerId);

        call.enqueue(new Callback<List<Workshop>>() {
            @Override
            public void onResponse(Call<List<Workshop>> call, Response<List<Workshop>> response) {

                if (response.isSuccessful()) {
                    List<Workshop> workshops = response.body();

                    // Si backend devuelve 204 -> body null
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
            public void onFailure(Call<List<Workshop>> call, Throwable throwable) {
                listener.onLoadError(throwable.getMessage());
            }
        });
    }

    @Override
    public void deleteWorkshop(long id, OnDeleteListener listener) {

        WorkshopApiInterface api = WorkshopApi.buildInstance();

        api.deleteWorkshop(id).enqueue(new Callback<Void>() {
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
