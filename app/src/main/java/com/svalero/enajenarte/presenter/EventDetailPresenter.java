package com.svalero.enajenarte.presenter;

import com.svalero.enajenarte.contract.EventDetailContract;
import com.svalero.enajenarte.domain.Event;
import com.svalero.enajenarte.model.EventDetailModel;

public class EventDetailPresenter implements EventDetailContract.Presenter, EventDetailContract.Model.OnLoadListener {

    private final EventDetailContract.Model model;
    private final EventDetailContract.View view;

    public EventDetailPresenter(EventDetailContract.View view) {
        this.model = new EventDetailModel();
        this.view = view;
    }

    @Override
    public void loadEvent(long id) {
        model.loadEvent(id, this);
    }

    @Override
    public void onLoadSuccess(Event event) {
        view.showEvent(event);
    }

    @Override
    public void onLoadError(String message) {
        view.showError(message);
    }
}
