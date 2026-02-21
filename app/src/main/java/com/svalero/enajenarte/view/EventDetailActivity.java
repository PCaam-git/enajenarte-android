package com.svalero.enajenarte.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.svalero.enajenarte.R;
import com.svalero.enajenarte.contract.EventDetailContract;
import com.svalero.enajenarte.domain.Event;
import com.svalero.enajenarte.presenter.EventDetailPresenter;

import com.svalero.enajenarte.util.DateUtil;

public class EventDetailActivity extends AppCompatActivity implements EventDetailContract.View {

    private EventDetailPresenter presenter;
    private long eventId;
    private TextView textTitle;
    private TextView textDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        presenter = new EventDetailPresenter(this);

        textTitle = findViewById(R.id.text_event_title);
        textDate = findViewById(R.id.text_event_date);

        eventId = getIntent().getLongExtra("event_id", -1);
        if (eventId == -1) {
            Toast.makeText(this, "ID del evento no recibido", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        findViewById(R.id.button_edit_event).setOnClickListener(view -> {
            Intent intent = new Intent(this, EventEditActivity.class);
            intent.putExtra("event_id", eventId);
            startActivity(intent);
        });

        setTitle("Detalle evento");

        presenter.loadEvent(eventId);
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.loadEvent(eventId);
    }

    @Override
    public void showEvent(Event event) {
        textTitle.setText(event.getTitle());
        textDate.setText(DateUtil.formatDateTime(event.getEventDate()));
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