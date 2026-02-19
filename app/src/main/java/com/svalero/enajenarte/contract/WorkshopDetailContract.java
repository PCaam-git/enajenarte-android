package com.svalero.enajenarte.contract;

import com.svalero.enajenarte.domain.Workshop;

public interface WorkshopDetailContract {

    interface Model {
        interface OnLoadListener {
            void onLoadSuccess(Workshop workshop);
            void onLoadError(String message);
        }
        void loadWorkshop(long id, OnLoadListener listener);
    }

    interface View {
        void showWorkshop(Workshop workshop);
        void showMessage(String message);
        void showError(String message);
    }

    interface Presenter {
        void loadWorkshop(long id);
    }
}
