package com.svalero.enajenarte.presenter;

import com.svalero.enajenarte.contract.EventListContract;
import com.svalero.enajenarte.domain.Event;
import com.svalero.enajenarte.model.EventListModel;

import java.util.List;


public class EventListPresenter implements EventListContract.Presenter,
        EventListContract.Model.OnLoadListener {

    private final EventListContract.Model model;
    private final EventListContract.View view;

    public EventListPresenter(EventListContract.View view) {
        this.model = new EventListModel();
        this.view = view;
    }

    @Override
    public void loadEvents(String title, String location, Boolean isPublic) {
        model.loadEvents(title, location, isPublic, this);
    }

    @Override
    public void onLoadSuccess(List<Event> events) {
        view.showEvents(events);
        view.showMessage("Eventos cargados: " + events.size());
    }

    @Override
    public void onLoadError(String message) {
        view.showError(message);
    }

    @Override
    public void deleteEvent(long id) {
        model.deleteEvent(id, new EventListContract.Model.OnDeleteListener() {

            @Override
            public void onDeleteSuccess() {
                view.showMessage("Evento eliminado");
                loadEvents(null, null, null);
            }

            @Override
            public void onDeleteError(String message) {
                view.showError(message);
            }
        });
    }
}
