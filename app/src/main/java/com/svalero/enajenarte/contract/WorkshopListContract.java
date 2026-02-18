package com.svalero.enajenarte.contract;

import com.svalero.enajenarte.domain.Workshop;

import java.util.List;
public interface WorkshopListContract {

    interface Model {
        interface OnLoadListener {
            void onLoadSuccess(List<Workshop> workshops);
            void onLoadError(String message);
        }

        void loadWorkshops(String name, String isOnline, String speakerId, OnLoadListener listener);
    }

    interface View {
        void showWorkshops(List<Workshop> workshops);
        void showMessage(String message);
        void showError(String message);
    }

    interface Presenter {
        void loadWorkshops(String name, String isOnline, String speakerId);
    }
}
