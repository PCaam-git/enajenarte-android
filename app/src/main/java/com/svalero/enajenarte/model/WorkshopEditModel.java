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

        WorkshopApiInterface apiInterface = WorkshopApi.buildInstance();

        apiInterface.updateWorkshop(id, workshopRequest).enqueue(new Callback<Workshop>() {
            @Override
            public void onResponse(Call<Workshop> call, Response<Workshop> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listener.onUpdateSuccess(response.body());
                    return;
                }

                String errorText = "Error HTTP: " + response.code();
                try {
                    if (response.errorBody() != null) {
                        errorText += " - " + response.errorBody().string();
                    }
                } catch (Exception ignored) {}

                listener.onUpdateError(errorText);
            }

            @Override
            public void onFailure(Call<Workshop> call, Throwable throwable) {
                listener.onUpdateError(throwable.getMessage());
            }
        });
    }
}
