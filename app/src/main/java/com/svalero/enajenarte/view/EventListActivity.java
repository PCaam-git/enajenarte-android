package com.svalero.enajenarte.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.svalero.enajenarte.R;
import com.svalero.enajenarte.adapter.EventAdapter;
import com.svalero.enajenarte.contract.EventListContract;
import com.svalero.enajenarte.domain.Event;
import com.svalero.enajenarte.presenter.EventListPresenter;

import java.util.ArrayList;
import java.util.List;

public class EventListActivity extends AppCompatActivity implements EventListContract.View {

    private EventAdapter eventAdapter;
    private List<Event> eventList;
    private EventListPresenter presenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);

        presenter = new EventListPresenter(this);
        eventList = new ArrayList<>();

        RecyclerView recyclerView = findViewById(R.id.event_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        eventAdapter = new EventAdapter(this, eventList, event -> {
            Intent intent = new Intent(this, EventDetailActivity.class);
            intent.putExtra("event_id", event.getId());
            startActivity(intent);
        });
        recyclerView.setAdapter(eventAdapter);

        setTitle("Eventos");
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.loadEvents(null, null, null); // sin filtros
    }

    @Override
    public void showEvents(List<Event> events) {
        eventList.clear();
        eventList.addAll(events);
        eventAdapter.notifyDataSetChanged();
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showError(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

    }
}