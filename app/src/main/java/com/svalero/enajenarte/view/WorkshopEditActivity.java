package com.svalero.enajenarte.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.svalero.enajenarte.R;
import com.svalero.enajenarte.api.WorkshopApi;
import com.svalero.enajenarte.api.WorkshopApiInterface;
import com.svalero.enajenarte.contract.WorkshopEditContract;
import com.svalero.enajenarte.db.DatabaseUtil;
import com.svalero.enajenarte.db.entity.WorkshopEntity;
import com.svalero.enajenarte.domain.Workshop;
import com.svalero.enajenarte.domain.request.WorkshopRequest;
import com.svalero.enajenarte.presenter.WorkshopEditPresenter;
import com.svalero.enajenarte.util.DateUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WorkshopEditActivity extends AppCompatActivity implements WorkshopEditContract.View {

    private WorkshopEditPresenter presenter;

    private EditText editName;
    private EditText editDescription;
    private EditText editStartDate;
    private EditText editDuration;
    private EditText editPrice;
    private EditText editMaxCapacity;
    private Switch switchOnline;
    private String imageUri;
    private ImageView imagePreview;
    private EditText editSpeakerId;
    private Button buttonSave;

    private long workshopId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workshop_edit);
        imagePreview = findViewById(R.id.image_workshop_preview);

        presenter = new WorkshopEditPresenter(this);

        // Views
        editName = findViewById(R.id.edit_workshop_name);
        editDescription = findViewById(R.id.edit_workshop_description);
        editStartDate = findViewById(R.id.edit_workshop_start_date);
        editStartDate.setHint("dd/MM/yyyy");
        editDuration = findViewById(R.id.edit_workshop_duration);
        editPrice = findViewById(R.id.edit_workshop_price);
        editMaxCapacity = findViewById(R.id.edit_workshop_max_capacity);
        switchOnline = findViewById(R.id.switch_workshop_online);
        editSpeakerId = findViewById(R.id.edit_workshop_speaker_id);
        buttonSave = findViewById(R.id.button_save_workshop);

        // Get id
        workshopId = getIntent().getLongExtra("workshop_id", -1);

        boolean isEditMode = workshopId != -1;

        if (isEditMode) {
            loadWorkshop(workshopId);
            setTitle("Editar taller");
        } else {
            setTitle("Crear taller");
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
    private void loadWorkshop(long id) {
        if (id == -1) return; // modo crear, no tiene que precargar

        WorkshopApiInterface apiInterface = WorkshopApi.buildInstance();
        apiInterface.getWorkshop(id).enqueue(new Callback<Workshop>() {
            @Override
            public void onResponse(Call<Workshop> call, Response<Workshop> response) {
                if (response.isSuccessful() && response.body() != null) {
                    fillForm(response.body());
                } else {
                    Toast.makeText(WorkshopEditActivity.this, "Error HTTP: " + response.code(), Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<Workshop> call, Throwable throwable) {
                Toast.makeText(WorkshopEditActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                if (workshopId != -1) // solo cierra en el modo editar
                finish();
            }
        });
    }

    private void fillForm(Workshop workshop) {
        editName.setText(workshop.getName());
        editDescription.setText(workshop.getDescription());


        if (workshop.getStartDate() != null) {
            editStartDate.setText(DateUtil.formatDate(workshop.getStartDate()));
        }

        editDuration.setText(String.valueOf(workshop.getDurationMinutes()));
        editPrice.setText(String.valueOf(workshop.getPrice()));
        switchOnline.setChecked(workshop.isOnline());
        editSpeakerId.setText(String.valueOf(workshop.getSpeakerId()));
    }

    public void selectImage (View view) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        galleryLauncher.launch(intent);
    }

    private void trySave() {
        String name = editName.getText().toString().trim();
        String description = editDescription.getText().toString().trim();
        String startDate = editStartDate.getText().toString().trim();
        String durationStr = editDuration.getText().toString().trim();
        String priceStr = editPrice.getText().toString().trim();
        String maxCapacityStr = editMaxCapacity.getText().toString().trim();
        String speakerIdStr = editSpeakerId.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(startDate)
                || TextUtils.isEmpty(durationStr) || TextUtils.isEmpty(priceStr)
                || TextUtils.isEmpty(maxCapacityStr) ||TextUtils.isEmpty(speakerIdStr)) {
            showError("Rellena los campos obligatorios (nombre, fecha, duración, precio, capacidad máxima y speakerId)");
            return;
        }

        int duration;
        float price;
        int maxCapacity;
        long speakerId;

        try {
            duration = Integer.parseInt(durationStr);
            price = Float.parseFloat(priceStr);
            maxCapacity = Integer.parseInt(maxCapacityStr);
            speakerId = Long.parseLong(speakerIdStr);
        } catch (NumberFormatException e) {
            showError("Formato numérico incorrecto (duración, precio, capacidad máxima o speakerId)");
            return;
        }

        if (duration < 1) {
            showError("Duración mínima: 1");
            return;
        }

        if (maxCapacity < 1) {
            showError("Capacidad mínima: 1");
            return;
        }

        if (speakerId < 1) {
            showError("SpeakerId inválido");
            return;
        }

        startDate = DateUtil.userToIsoDate(startDate);

        if (startDate == null) {
            showError("Formato de fecha inválido. Use dd/MM/yyyy");
            return;
        }

        WorkshopRequest request = WorkshopRequest.builder()
                .name(name)
                .description(description)
                .startDate(startDate)
                .durationMinutes(duration)
                .price(price)
                .maxCapacity(maxCapacity)         //para evitar 400. no está en outdto pero sí en indto
                .isOnline(switchOnline.isChecked())
                .speakerId(speakerId)
                .build();

        new AlertDialog.Builder(this)
                .setTitle("Confirmar")
                .setMessage(workshopId != -1 ?
                        "¿Actualizar este taller?" :
                        "¿Crear este taller?")
                .setPositiveButton("Aceptar", (dialog, which) -> {

                    if (workshopId != -1) {
                        presenter.updateWorkshop(workshopId, request);
                    } else {
                        presenter.createWorkshop(request);
                    }

                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    @Override
    public void closeAfterUpdate(Workshop workshop) {

        // Se guarda en Room la Uri asociada a este taller.
        saveImageUriInRoom(workshop);

        setResult(RESULT_OK);
        finish();
    }

    private void saveImageUriInRoom(Workshop workshop) {
        // Si no se ha seleccionado imagen al editar, no sobrescribimos. Esto permite mantener una imagen antigua al editar
        if (imageUri == null || imageUri.isEmpty()) {
            return;
        }

        // Creamos - actualizamos el registro local de Room con la imageUri
        WorkshopEntity workshopEntity = new WorkshopEntity();

        workshopEntity.setId(workshop.getId());
        workshopEntity.setName(workshop.getName());
        workshopEntity.setDescription(workshop.getDescription());
        workshopEntity.setStartDate(String.valueOf(workshop.getStartDate()));
        workshopEntity.setDurationMinutes(workshop.getDurationMinutes());
        workshopEntity.setPrice(workshop.getPrice());
        workshopEntity.setMaxCapacity(workshop.getMaxCapacity());
        workshopEntity.setOnline(workshop.isOnline());
        workshopEntity.setSpeakerId(workshop.getSpeakerId());

        workshopEntity.setImageUri(imageUri);

        DatabaseUtil.getDb(this).workshopDao().insert(workshopEntity);
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
