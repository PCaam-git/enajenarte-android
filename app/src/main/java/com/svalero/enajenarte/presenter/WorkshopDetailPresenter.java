package com.svalero.enajenarte.presenter;

import com.svalero.enajenarte.contract.WorkshopDetailContract;
import com.svalero.enajenarte.domain.Workshop;
import com.svalero.enajenarte.model.WorkshopDetailModel;

public class WorkshopDetailPresenter implements WorkshopDetailContract.Presenter, WorkshopDetailContract.Model.OnLoadListener {

    private final WorkshopDetailContract.Model model;
    private final WorkshopDetailContract.View view;

    public WorkshopDetailPresenter(WorkshopDetailContract.View view) {
        this.model = new WorkshopDetailModel();
        this.view = view;
    }

    @Override
    public void loadWorkshop(long id) {
        model.loadWorkshop(id, this);
    }

    @Override
    public void onLoadSuccess(Workshop workshop) {
        view.showWorkshop(workshop);
    }
    @Override
    public void onLoadError(String message) {
        view.showError(message);
    }
}
