package com.svalero.enajenarte.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.view.Menu;
import android.view.MenuItem;

import com.svalero.enajenarte.R;
import com.svalero.enajenarte.contract.WorkshopDetailContract;
import com.svalero.enajenarte.db.DatabaseUtil;
import com.svalero.enajenarte.db.entity.EventEntity;
import com.svalero.enajenarte.db.entity.WorkshopEntity;
import com.svalero.enajenarte.domain.Workshop;
import com.svalero.enajenarte.presenter.WorkshopDetailPresenter;
import com.svalero.enajenarte.view.PreferencesActivity;

public class WorkshopDetailActivity extends AppCompatActivity implements WorkshopDetailContract.View {

    // Borrado delegado desde WorkshopListActivity
    private static final String EXTRA_DELETE_WORKSHOP_ID = "delete_workshop_id";

    private WorkshopDetailPresenter presenter;
    private long workshopId;
    private TextView nameTextView;
    private TextView descriptionTextView;
    private TextView startDateTextView;
    private TextView durationTextView;
    private TextView priceTextView;
    private TextView isOnlineTextView;
    private ImageView imageWorkshopDetail;
    private TextView speakerIdTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workshop_detail);

        presenter = new WorkshopDetailPresenter(this);


        nameTextView = findViewById(R.id.text_workshop_name);
        descriptionTextView = findViewById(R.id.text_workshop_description);
        startDateTextView = findViewById(R.id.text_workshop_start_date);
        durationTextView = findViewById(R.id.text_workshop_duration);
        priceTextView = findViewById(R.id.text_workshop_price);
        isOnlineTextView = findViewById(R.id.text_workshop_is_online);
        imageWorkshopDetail = findViewById(R.id.image_workshop_detail);
        speakerIdTextView = findViewById(R.id.text_workshop_speaker_id);

        workshopId = getIntent().getLongExtra("workshop_id", -1);

        presenter = new WorkshopDetailPresenter(this);

        if (nameTextView == null) {
            Toast.makeText(this, "Layout/ids incorrectos", Toast.LENGTH_SHORT).show();
            return;
        }

        if (workshopId == -1) {
            Toast.makeText(this, "ID del taller no recibido", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        setTitle("Detalle taller");

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        presenter.loadWorkshop(workshopId);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (workshopId != -1) {
            presenter.loadWorkshop(workshopId);
        }
    }

    // Se abre la pantalla de edición
    private void openEdit() {
        Intent intent = new Intent(this, WorkshopEditActivity.class);
        intent.putExtra("workshop_id", workshopId);
        startActivity(intent);
    }

    // Confirmando la eliminación para evitar borrados accidentales
    private void confirmDelete() {
        new AlertDialog.Builder(this)
                .setTitle("Eliminar taller")
                .setMessage("¿Seguro que quieres eliminar este taller?")
                .setPositiveButton("Eliminar", (dialog, which) -> redirectToListForDelete())
                .setNegativeButton("Cancelar", null)
                .show();
    }

    // Si se pulsa Delete, se vuelve al listado y se borra el taller.
    private void redirectToListForDelete() {

        Intent intent = new Intent(this, WorkshopListActivity.class);
        intent.putExtra(EXTRA_DELETE_WORKSHOP_ID, workshopId);

        // Reutiliza la instancia del listado si ya está en la pila
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        startActivity(intent);
        finish();
    }

    // ACTIONBAR

    // Carga el menú del detalle del workshop.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_workshop_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
            return true;
        } else if (id == R.id.action_edit_workshop) {
            openEdit();
            return true;
        } else if (id == R.id.action_delete_workshop) {
            confirmDelete();
            return true;
        } else if (id == R.id.action_settings) {
            Intent intent = new Intent(this, PreferencesActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // CONTRACT VIEW
    @Override
    public void showWorkshop(Workshop workshop) {
        Toast.makeText(this, workshop.getName(), Toast.LENGTH_SHORT).show();
        nameTextView.setText(workshop.getName());
        descriptionTextView.setText(workshop.getDescription());
        startDateTextView.setText(workshop.getStartDate() != null ? workshop.getStartDate().toString() : "");
        durationTextView.setText(workshop.getDurationMinutes() + " minutos");
        priceTextView.setText("Precio: " + (workshop.getPrice() + " €"));
        isOnlineTextView.setText("Es online: " +(workshop.isOnline() ? "Sí" : "No"));
        speakerIdTextView.setText(String.valueOf(workshop.getSpeakerId()));
        showImageIfExists();
    }

    private void showImageIfExists() {
        // Busca la Uri guardada en Room para este taller
        WorkshopEntity workshopEntity = DatabaseUtil.getDb(this).workshopDao().findById(workshopId);

        if (workshopEntity != null && workshopEntity.getImageUri() != null && !workshopEntity.getImageUri().isEmpty()) {
            imageWorkshopDetail.setImageURI(Uri.parse(workshopEntity.getImageUri()));
            imageWorkshopDetail.setVisibility(View.VISIBLE);

        } else {
            // Sin placeholder
            imageWorkshopDetail.setVisibility(View.GONE);

        }
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}