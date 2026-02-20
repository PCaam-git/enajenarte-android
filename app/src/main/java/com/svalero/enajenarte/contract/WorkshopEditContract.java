package com.svalero.enajenarte.contract;

import com.svalero.enajenarte.domain.Workshop;
import com.svalero.enajenarte.domain.request.WorkshopRequest;

public interface WorkshopEditContract {
    public interface Model {
        interface OnUpdateListener {
            void onUpdateSuccess(Workshop workshop);
            void onUpdateError(String message);
        }
        void updateWorkshop(long id, WorkshopRequest workshopRequest, OnUpdateListener listener);

        void createWorkshop(WorkshopRequest workshopRequest, OnUpdateListener listener);
    }
    public interface View {
        void showMessage(String message);
        void showError(String message);
        void closeAfterUpdate();
}
    public interface Presenter {
        void updateWorkshop(long id, WorkshopRequest workshopRequest);
        void createWorkshop(WorkshopRequest workshopRequest);
    }
}
