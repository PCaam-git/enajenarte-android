package com.svalero.enajenarte.presenter;

import com.svalero.enajenarte.contract.EventEditContract;
import com.svalero.enajenarte.domain.Event;
import com.svalero.enajenarte.domain.request.EventRequest;
import com.svalero.enajenarte.model.EventEditModel;

public class EventEditPresenter implements EventEditContract.Presenter,
        EventEditContract.Model.OnUpdateListener {

    private final EventEditContract.Model model;
    private final EventEditContract.View view;
    private String successMessage;

    public EventEditPresenter(EventEditContract.View view) {
        this.model = new EventEditModel();
        this.view = view;
    }

    @Override
    public void updateEvent(long id, EventRequest eventRequest) {
        successMessage = "Evento actualizado";
        model.updateEvent(id, eventRequest, this);
    }

    @Override
    public void createEvent(EventRequest eventRequest) {
        successMessage = "Evento creado";
        model.createEvent(eventRequest, this);
    }

    @Override
    public void onUpdateSuccess(Event event) {
        view.showMessage(successMessage);
        view.closeAfterUpdate(event);
    }

    @Override
    public void onUpdateError(String message) {
        view.showError(message);
    }
}
