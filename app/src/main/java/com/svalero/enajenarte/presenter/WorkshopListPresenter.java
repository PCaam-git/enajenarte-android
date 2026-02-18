package com.svalero.enajenarte.presenter;

import com.svalero.enajenarte.contract.WorkshopListContract;
import com.svalero.enajenarte.domain.Workshop;
import com.svalero.enajenarte.model.WorkshopListModel;

import java.util.List;

public class WorkshopListPresenter implements WorkshopListContract.Presenter,
        WorkshopListContract.Model.OnLoadListener {
    private WorkshopListContract.Model model;
    private WorkshopListContract.View view;

    public WorkshopListPresenter(WorkshopListContract.View view) {
        this.model = new WorkshopListModel();
        this.view = view;
    }

    @Override
    public void loadWorkshops(String name, String isOnline, String speakerId) {
        model.loadWorkshops(name, isOnline, speakerId, this);
    }

    @Override
    public void onLoadSuccess(List<Workshop> workshops) {
        view.showWorkshops(workshops);
        view.showMessage("Talleres cargados: " + workshops.size());
    }

    @Override
    public void onLoadError(String message) {
        view.showError(message);
    }
    @Override
    public void deleteWorkshop(long id) {
        model.deleteWorkshop(id, new WorkshopListContract.Model.OnDeleteListener() {
            @Override
            public void onDeleteSuccess() {
                view.showMessage("Taller eliminado");
                loadWorkshops(null, null, null);
            }
            @Override
            public void onDeleteError(String message) {
                view.showError(message);
            }
        });
    }
}
