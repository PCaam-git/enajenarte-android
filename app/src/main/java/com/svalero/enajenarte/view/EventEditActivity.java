package com.svalero.enajenarte.view;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.svalero.enajenarte.R;
import com.svalero.enajenarte.api.EventApi;
import com.svalero.enajenarte.api.EventApiInterface;
import com.svalero.enajenarte.contract.EventEditContract;
import com.svalero.enajenarte.domain.Event;
import com.svalero.enajenarte.domain.request.EventRequest;
import com.svalero.enajenarte.presenter.EventEditPresenter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventEditActivity extends AppCompatActivity implements EventEditContract.View {

    private EventEditPresenter presenter;

    private EditText editTitle;
    private EditText editLocation;
    private EditText editEventDate;
    private EditText editExpectedAttendance;
    private Switch switchPublic;
    private EditText editEntryFee;
    private EditText editSpeakerId;
    private Button buttonSave;

    private long eventId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_event_edit);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        presenter = new EventEditPresenter(this);

        // Views
        editTitle = findViewById(R.id.edit_event_title);
        editLocation = findViewById(R.id.edit_event_location);
        editEventDate = findViewById(R.id.edit_event_date);
        editExpectedAttendance = findViewById(R.id.edit_event_expected_attendance);
        switchPublic = findViewById(R.id.switch_event_public);
        editEntryFee = findViewById(R.id.edit_event_entry_fee);
        editSpeakerId = findViewById(R.id.edit_event_speaker_id);
        buttonSave = findViewById(R.id.button_save_event);

        // Get id
        eventId = getIntent().getLongExtra("event_id", -1);

        boolean isEditMode = eventId != -1;

        if (isEditMode) {
            loadEvent(eventId);
            setTitle("Editar evento");
        } else {
            setTitle("Crear evento");
        }

        // Guardar
        buttonSave.setOnClickListener(v -> trySave());
    }

    private void loadEvent(long id) {
        if (id == -1) return; // modo crear, no tiene que precargar

        EventApiInterface apiInterface = EventApi.buildInstance();
        apiInterface.getEvent(id).enqueue(new Callback<Event>() {
            @Override
            public void onResponse(Call<Event> call, Response<Event> response) {
                if (response.isSuccessful() && response.body() != null) {
                    fillForm(response.body());
                } else {
                    Toast.makeText(EventEditActivity.this, "Error HTTP: " + response.code(), Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<Event> call, Throwable throwable) {
                Toast.makeText(EventEditActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                if (eventId != -1) // solo cierra en el modo editar
                    finish();
            }
        });
    }

    private void fillForm(Event event) {
        editTitle.setText(event.getTitle());
        editLocation.setText(event.getLocation());

        if (event.getEventDate() != null) {
            editEventDate.setText(event.getEventDate());
        }

        editEntryFee.setText(String.valueOf(event.getEntryFee()));
        switchPublic.setChecked(event.isPublic());
        editSpeakerId.setText(String.valueOf(event.getSpeakerId()));

        // IMPORTANTE:
        // expectedAttendance NO está en Event (salida), solo en EventRequest (entrada).
        // No se precargan datos, el admin deberá introducirlos manualmente
    }

    private void trySave() {
        String title = editTitle.getText().toString().trim();
        String location = editLocation.getText().toString().trim();
        String eventDate = editEventDate.getText().toString().trim();
        String expectedAttendanceStr = editExpectedAttendance.getText().toString().trim();
        String entryFeeStr = editEntryFee.getText().toString().trim();
        String speakerIdStr = editSpeakerId.getText().toString().trim();

        // EventDate debe ir en formato ISO: yyyy-MM-dd'T'HH:mm:ss (ej: 2026-02-21T10:30:00)
        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(location) || TextUtils.isEmpty(eventDate)
                || TextUtils.isEmpty(expectedAttendanceStr) || TextUtils.isEmpty(entryFeeStr)
                || TextUtils.isEmpty(speakerIdStr)) {
            showError("Rellena los campos obligatorios (título, ubicación, fecha y hora, asistencia esperada, entrada y speakerId)");
            return;
        }

        int expectedAttendance;
        float entryFee;
        long speakerId;

        try {
            expectedAttendance = Integer.parseInt(expectedAttendanceStr);
            entryFee = Float.parseFloat(entryFeeStr);
            speakerId = Long.parseLong(speakerIdStr);
        } catch (NumberFormatException e) {
            showError("Formato incorrecto (asistencia esperada, entrada o speakerId)");
            return;
        }

        if (expectedAttendance < 0) {
            showError("Asistencia esperada inválida");
            return;
        }

        if (entryFee < 0) {
            showError("Precio entrada inválido");
            return;
        }

        if (speakerId < 1) {
            showError("SpeakerId inválido");
            return;
        }

        EventRequest request = EventRequest.builder()
                .title(title)
                .location(location)
                .eventDate(eventDate)
                .expectedAttendance(expectedAttendance) // está en InDto, pero NO en OutDto
                .isPublic(switchPublic.isChecked())
                .entryFee(entryFee)
                .speakerId(speakerId)
                .build();

        new AlertDialog.Builder(this)
                .setTitle("Confirmar")
                .setMessage(eventId != -1 ?
                        "¿Actualizar este evento?" :
                        "¿Crear este evento?")
                .setPositiveButton("Aceptar", (dialog, which) -> {

                    if (eventId != -1) {
                        presenter.updateEvent(eventId, request);
                    } else {
                        presenter.createEvent(request);
                    }

                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showError(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void closeAfterUpdate() {
        setResult(RESULT_OK);
        finish();
    }
}