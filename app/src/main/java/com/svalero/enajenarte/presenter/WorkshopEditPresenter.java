package com.svalero.enajenarte.presenter;

import com.svalero.enajenarte.contract.WorkshopEditContract;
import com.svalero.enajenarte.domain.Workshop;
import com.svalero.enajenarte.domain.request.WorkshopRequest;
import com.svalero.enajenarte.model.WorkshopEditModel;

public class WorkshopEditPresenter implements WorkshopEditContract.Presenter,
        WorkshopEditContract.Model.OnUpdateListener {

    private final WorkshopEditContract.Model model;
    private final WorkshopEditContract.View view;

    public WorkshopEditPresenter(WorkshopEditContract.View view) {
        this.model = new WorkshopEditModel();
        this.view = view;
    }

    @Override
    public void updateWorkshop(long id, WorkshopRequest workshopRequest) {
        model.updateWorkshop(id, workshopRequest, this);
    }

    @Override
    public void onUpdateSuccess(Workshop workshop) {
        view.showMessage("Taller actualizado");
        view.closeAfterUpdate();
    }

    @Override
    public void onUpdateError(String message) {
        view.showError(message);
    }
}
