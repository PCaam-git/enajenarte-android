package com.svalero.enajenarte.model;

import android.content.Context;

import com.svalero.enajenarte.api.EventApi;
import com.svalero.enajenarte.api.EventApiInterface;
import com.svalero.enajenarte.contract.EventListContract;
import com.svalero.enajenarte.db.DatabaseUtil;
import com.svalero.enajenarte.db.entity.EventEntity;
import com.svalero.enajenarte.domain.Event;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventListModel implements EventListContract.Model {

    private Context context;

    public EventListModel(Context context) {
        this.context = context;
    }


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
                        events = new ArrayList<>();
                    }

                    // Guardar en Room
                    saveEventsToDatabase(events);

                    listener.onLoadSuccess(events);

                    } else {
                    listener.onLoadError("Error HTTP: " + response.code());

                }
            }

            @Override
            public void onFailure(Call<List<Event>> call, Throwable throwable) {

                // Intentar Room
                List<Event> localEvents = getEventsFromDatabase();

                if(!localEvents.isEmpty()) {
                    listener.onLoadSuccess(localEvents);
                } else {
                    listener.onLoadError(throwable.getMessage());
                }
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

                    // Eliminar también de Room
                    deleteEventFromDatabase(id);

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

    // Métodos Room

    private void saveEventsToDatabase(List<Event> events) {

        List<EventEntity> entities = new ArrayList<>();

        for (Event event : events) {
            EventEntity entity = new EventEntity(
                    event.getId(),
                    event.getTitle(),
                    event.getLocation(),
                    event.getEventDate(),
                    event.getEntryFee(),
                    event.isPublic(),
                    event.getSpeakerId()
            );
            entities.add(entity);
        }

        DatabaseUtil.getDb(context).eventDao().deleteAll();
        DatabaseUtil.getDb(context).eventDao().insertAll(entities);
    }

    private List<Event> getEventsFromDatabase() {

        List<EventEntity> entities = DatabaseUtil.getDb(context).eventDao().findAll();

        List<Event> events = new ArrayList<>();

        for (EventEntity entity : entities) {
            Event event = new Event(
                    entity.getId(),
                    entity.getTitle(),
                    entity.getLocation(),
                    entity.getEventDate(),
                    entity.getEntryFee(),
                    entity.isPublic(),
                    entity.getSpeakerId()
            );
            events.add(event);
        }

        return events;
    }

    private void deleteEventFromDatabase(long id) {

        List<EventEntity> entities = DatabaseUtil.getDb(context).eventDao().findAll();

        for (EventEntity entity : entities) {
            if (entity.getId() == id) {
                DatabaseUtil.getDb(context).eventDao().delete(entity);
                break;
            }
        }
    }
}
