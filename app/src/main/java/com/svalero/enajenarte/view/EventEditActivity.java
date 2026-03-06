package com.svalero.enajenarte.view;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;
import android.net.Uri;
import android.content.Intent;


import androidx.activity.result.contract.ActivityResultContracts;
import androidx.activity.result.ActivityResultLauncher;
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
import com.svalero.enajenarte.db.DatabaseUtil;
import com.svalero.enajenarte.db.entity.EventEntity;
import com.svalero.enajenarte.db.dao.EventDao;
import com.svalero.enajenarte.util.*;

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
    private String imageUri;
    private ImageView imagePreview;
    private Button buttonSave;

    private long eventId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_edit);
        imagePreview = findViewById(R.id.image_event_preview);

        presenter = new EventEditPresenter(this);

        // Views
        editTitle = findViewById(R.id.edit_event_title);
        editLocation = findViewById(R.id.edit_event_location);
        editEventDate = findViewById(R.id.edit_event_date);
        editEventDate.setHint("dd/MM/yyyy HH:mm");
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

    private final ActivityResultLauncher<Intent> galleryLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                            Uri uri = result.getData().getData();
                            if (uri != null) {

                                // Guardar permiso de lectura para poder usar esta Uri más adelante (Room + Detail)
                                final int takeFlags = Intent.FLAG_GRANT_READ_URI_PERMISSION;
                                getContentResolver().takePersistableUriPermission(uri, takeFlags);

                                imagePreview.setImageURI(uri);
                                imageUri = uri.toString();
                            }
                        }
                    }
            );

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
            editEventDate.setText(DateUtil.formatDateTime(event.getEventDate()));
        }

        editEntryFee.setText(String.valueOf(event.getEntryFee()));
        switchPublic.setChecked(event.isPublic());
        editSpeakerId.setText(String.valueOf(event.getSpeakerId()));


        // expectedAttendance NO está en Event (salida), solo en EventRequest (entrada).
        // No se precargan datos, el admin debe introducirlos manualmente
    }

    public void selectImage(View view) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        galleryLauncher.launch(intent);
    }


    private void trySave() {
        String title = editTitle.getText().toString().trim();
        String location = editLocation.getText().toString().trim();
        String eventDate = editEventDate.getText().toString().trim();
        String expectedAttendanceStr = editExpectedAttendance.getText().toString().trim();
        String entryFeeStr = editEntryFee.getText().toString().trim();
        String speakerIdStr = editSpeakerId.getText().toString().trim();

        // El usuario introduce la fecha en formato dd/MM/yyyy HH:mm.
        // Antes de enviar a la API se convierte a formato ISO con DateUtil.userToIsoDateTime().
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

        eventDate = DateUtil.userToIsoDateTime(eventDate);

        if (eventDate == null) {
            showError("Formato de fecha y hora inválido. Use dd/MM/yyyy HH:mm");
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
    public void closeAfterUpdate(Event event) {

        // Se guarda en Room la Uri asociada a este evento.
        // La API no guarda imágenes, el almacenamiento es solo local.
        saveImageUriInRoom(event);

        setResult(RESULT_OK);
        finish();
    }

    private void saveImageUriInRoom(Event event) {

        // Si no se ha seleccionado imagen al editar, no sobrescribimos. Esto permite mantener una imagen antigua al editar
        if (imageUri == null || imageUri.isEmpty()) {
            return;
        }

        // Creamos/actualizamos el registro local de Room con la imageUri.
        EventEntity eventEntity = new EventEntity();

        eventEntity.setId(event.getId());
        eventEntity.setTitle(event.getTitle());
        eventEntity.setLocation(event.getLocation());
        eventEntity.setEventDate(event.getEventDate());
        eventEntity.setEntryFee(event.getEntryFee());
        eventEntity.setPublic(event.isPublic());
        eventEntity.setSpeakerId(event.getSpeakerId());

        eventEntity.setImageUri(imageUri);

        DatabaseUtil.getDb(this).eventDao().insert(eventEntity);
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