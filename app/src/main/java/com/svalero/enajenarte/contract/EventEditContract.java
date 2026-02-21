package com.svalero.enajenarte.contract;

import com.svalero.enajenarte.domain.Event;
import com.svalero.enajenarte.domain.request.EventRequest;
public interface EventEditContract {

    public interface Model {

        interface OnUpdateListener {
            void onUpdateSuccess(Event event);
            void onUpdateError(String message);
        }

        void updateEvent(long id, EventRequest eventRequest, OnUpdateListener listener);
        void createEvent(EventRequest eventRequest, OnUpdateListener listener);
    }

    public interface View {
        void showMessage(String message);
        void showError(String message);
        void closeAfterUpdate();
    }

    public interface Presenter {
        void updateEvent(long id, EventRequest eventRequest);
        void createEvent(EventRequest eventRequest);
    }
}
