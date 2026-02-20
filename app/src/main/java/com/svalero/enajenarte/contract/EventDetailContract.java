package com.svalero.enajenarte.contract;

import com.svalero.enajenarte.domain.Event;

public interface EventDetailContract {

    interface Model {
        interface OnLoadListener {
            void onLoadSuccess(Event event);
            void onLoadError(String message);
        }

        void loadEvent(long id, OnLoadListener listener);
    }

    interface View {
        void showEvent(Event event);
        void showMessage(String message);
        void showError(String message);
    }

    interface Presenter {
        void loadEvent(long id);
    }
}
