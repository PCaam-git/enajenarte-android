package com.svalero.enajenarte.model;

import com.svalero.enajenarte.api.WorkshopApi;
import com.svalero.enajenarte.api.WorkshopApiInterface;
import com.svalero.enajenarte.contract.WorkshopDetailContract;
import com.svalero.enajenarte.domain.Workshop;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WorkshopDetailModel implements WorkshopDetailContract.Model {

    @Override
    public void loadWorkshop(long id, OnLoadListener listener) {
        WorkshopApiInterface api = WorkshopApi.buildInstance();

        api.getWorkshop(id).enqueue(new Callback<Workshop>() {
            @Override
            public void onResponse(Call<Workshop> call, Response<Workshop> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listener.onLoadSuccess(response.body());
                } else {
                    listener.onLoadError("Error HTTP: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Workshop> call, Throwable throwable) {
                listener.onLoadError(throwable.getMessage());

            }
        });
    }
}
