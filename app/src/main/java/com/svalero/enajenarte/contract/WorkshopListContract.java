package com.svalero.enajenarte.contract;

import com.svalero.enajenarte.domain.Workshop;

import java.util.List;
public interface WorkshopListContract {

    interface Model {
        interface OnLoadListener {
            void onLoadSuccess(List<Workshop> workshops);

            void onLoadError(String message);
        }

        interface OnDeleteListener {
            void onDeleteSuccess();
            void onDeleteError(String message);
        }

        void loadWorkshops(String name, String isOnline, String speakerId, OnLoadListener listener);

        void deleteWorkshop(long id, OnDeleteListener listener);
    }

    interface View {
        void showWorkshops(List<Workshop> workshops);
        void showMessage(String message);
        void showError(String message);
    }

    interface Presenter {
        void loadWorkshops(String name, String isOnline, String speakerId);
        void deleteWorkshop(long id);
    }
}
