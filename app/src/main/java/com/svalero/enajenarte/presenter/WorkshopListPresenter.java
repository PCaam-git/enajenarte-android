package com.svalero.enajenarte.presenter;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

import com.svalero.enajenarte.contract.WorkshopListContract;
import com.svalero.enajenarte.domain.Workshop;
import com.svalero.enajenarte.model.WorkshopListModel;
import com.svalero.enajenarte.util.DateUtil;

import java.util.ArrayList;
import java.util.List;

public class WorkshopListPresenter implements WorkshopListContract.Presenter,
        WorkshopListContract.Model.OnLoadListener {
    private WorkshopListContract.Model model;
    private WorkshopListContract.View view;

    public WorkshopListPresenter(WorkshopListContract.View view) {
        this.model = new WorkshopListModel((Context) view);
        this.view = view;
    }

    @Override
    public void loadWorkshops(String name, String isOnline, String speakerId) {
        model.loadWorkshops(name, isOnline, speakerId, this);
    }

    @Override
    public void onLoadSuccess(List<Workshop> workshops) {

        SharedPreferences preferences =
                PreferenceManager.getDefaultSharedPreferences((Context) view);

        boolean onlyFuture = preferences.getBoolean("only_future", false);

        List<Workshop> filteredWorkshops = workshops;

        if (onlyFuture) {
            filteredWorkshops = new ArrayList<>();
            for (Workshop workshop : workshops) {
                if (DateUtil.isFuture(workshop.getStartDate())) {
                    filteredWorkshops.add(workshop);
                }
            }
        }
        view.showWorkshops(filteredWorkshops);
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
