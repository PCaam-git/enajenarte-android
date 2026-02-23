package com.svalero.enajenarte.model;

import android.content.Context;

import com.svalero.enajenarte.api.WorkshopApi;
import com.svalero.enajenarte.api.WorkshopApiInterface;
import com.svalero.enajenarte.contract.WorkshopListContract;
import com.svalero.enajenarte.db.DatabaseUtil;
import com.svalero.enajenarte.db.entity.WorkshopEntity;
import com.svalero.enajenarte.domain.Workshop;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WorkshopListModel implements WorkshopListContract.Model {

    private Context context;

    public WorkshopListModel(Context context) {
        this.context = context;
    }

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
                        workshops = new ArrayList<>();
                    }

                    // NUEVO: guardar en Room
                    saveWorkshopsToDatabase(workshops);

                    listener.onLoadSuccess(workshops);

                } else {
                    listener.onLoadError("Error HTTP: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Workshop>> call, Throwable throwable) {

                // NUEVO: intentar leer de Room
                List<Workshop> localWorkshops = getWorkshopsFromDatabase();

                if (!localWorkshops.isEmpty()) {
                    listener.onLoadSuccess(localWorkshops);
                } else {
                    // Se mantiene tu mensaje original
                    listener.onLoadError(throwable.getMessage());
                }
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
                    deleteWorkshopFromDatabase(id);
                    listener.onDeleteSuccess();
                } else {

                    if (response.code() == 500) {
                        listener.onDeleteError("No se puede eliminar el taller porque tiene registros asociados");
                    } else {
                        listener.onDeleteError("Error HTTP: " + response.code());
                    }
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable throwable) {
                listener.onDeleteError(throwable.getMessage());
            }
        });
    }

    // Métodos ROOM

    private void saveWorkshopsToDatabase(List<Workshop> workshops) {

        List<WorkshopEntity> entities = new ArrayList<>();

        for (Workshop workshop : workshops) {
            WorkshopEntity entity = new WorkshopEntity(
                    workshop.getId(),
                    workshop.getName(),
                    workshop.getDescription(),
                    workshop.getStartDate().toString(),
                    workshop.getDurationMinutes(),
                    workshop.getPrice(),
                    workshop.getMaxCapacity(),
                    workshop.isOnline(),
                    workshop.getSpeakerId()
            );
            entities.add(entity);
        }

        DatabaseUtil.getDb(context).workshopDao().deleteAll();
        DatabaseUtil.getDb(context).workshopDao().insertAll(entities);
    }

    private List<Workshop> getWorkshopsFromDatabase() {

        List<WorkshopEntity> entities =
                DatabaseUtil.getDb(context).workshopDao().findAll();

        List<Workshop> workshops = new ArrayList<>();

        for (WorkshopEntity entity : entities) {
            Workshop workshop = new Workshop(
                    entity.getId(),
                    entity.getName(),
                    entity.getDescription(),
                    LocalDate.parse(entity.getStartDate()),
                    entity.getDurationMinutes(),
                    entity.getPrice(),
                    entity.getMaxCapacity(),
                    entity.isOnline(),
                    entity.getSpeakerId()
            );
            workshops.add(workshop);
        }

        return workshops;
    }

    private void deleteWorkshopFromDatabase(long id) {

        List<WorkshopEntity> entities =
                DatabaseUtil.getDb(context).workshopDao().findAll();

        for (WorkshopEntity entity : entities) {
            if (entity.getId() == id) {
                DatabaseUtil.getDb(context).workshopDao().delete(entity);
                break;
            }
        }
    }
}
