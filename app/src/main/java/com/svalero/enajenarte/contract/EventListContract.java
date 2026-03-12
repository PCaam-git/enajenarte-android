package com.svalero.enajenarte.contract;

import com.svalero.enajenarte.domain.Event;
import java.util.List;

public interface EventListContract {

    interface Model {
        interface OnLoadListener {
            void onLoadSuccess(List<Event> events);
            void onLoadError(String message);
        }

        interface OnDeleteListener {
            void onDeleteSuccess();

            void onDeleteError(String message);
        }

        void loadEvents(String title, String location, Boolean isPublic, OnLoadListener listener);
        void deleteEvent(long id, OnDeleteListener listener);
    }

    interface View {
        void showEvents(List<Event> events);
        void showMessage(String message);
        void showError(String message);
    }

    interface Presenter {
        void loadEvents(String title, String location, Boolean isPublic);
        void deleteEvent(long id);
    }
}
