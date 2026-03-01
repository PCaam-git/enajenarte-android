package com.svalero.enajenarte.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.view.Menu;
import android.view.MenuItem;

import com.svalero.enajenarte.R;
import com.svalero.enajenarte.contract.WorkshopDetailContract;
import com.svalero.enajenarte.domain.Workshop;
import com.svalero.enajenarte.presenter.WorkshopDetailPresenter;

public class WorkshopDetailActivity extends AppCompatActivity implements WorkshopDetailContract.View {
    private static final String EXTRA_DELETE_WORKSHOP_ID = "delete_workshop_id";

    private WorkshopDetailPresenter presenter;
    private long workshopId;
    private TextView nameTextView;
    private TextView descriptionTextView;
    private TextView startDateTextView;
    private TextView durationTextView;
    private TextView priceTextView;
    private TextView isOnlineTextView;
    private TextView speakerIdTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workshop_detail);

        nameTextView = findViewById(R.id.text_workshop_name);
        descriptionTextView = findViewById(R.id.text_workshop_description);
        startDateTextView = findViewById(R.id.text_workshop_start_date);
        durationTextView = findViewById(R.id.text_workshop_duration);
        priceTextView = findViewById(R.id.text_workshop_price);
        isOnlineTextView = findViewById(R.id.text_workshop_is_online);
        speakerIdTextView = findViewById(R.id.text_workshop_speaker_id);

        workshopId = getIntent().getLongExtra("workshop_id", -1);
        presenter = new WorkshopDetailPresenter(this);

        findViewById(R.id.button_edit_workshop).setOnClickListener(view -> openEdit());

        if (nameTextView == null) {
            Toast.makeText(this, "Layout/ids incorrectos", Toast.LENGTH_SHORT).show();
            return;
        }

        long id = getIntent().getLongExtra("workshop_id", -1);
        if (id == -1) {
            Toast.makeText(this, "ID del taller no recibido", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        presenter.loadWorkshop(id);
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

    // Confirmando la eliminación evitamos borrados accidentales
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

    // ===== MENÚ DE OPCIONES (ActionBar) =====

    // Carga el menú del detalle del workshop.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_workshop_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_edit_workshop) {
            openEdit();
            return true;
        }

        if (item.getItemId() == R.id.action_delete_workshop) {
            confirmDelete();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showWorkshop(Workshop workshop) {
        Toast.makeText(this, workshop.getName(), Toast.LENGTH_SHORT).show();
        nameTextView.setText(workshop.getName());
        descriptionTextView.setText(workshop.getDescription());
        startDateTextView.setText(workshop.getStartDate() != null ? workshop.getStartDate().toString() : "");
        durationTextView.setText(workshop.getDurationMinutes() + " minutos");
        priceTextView.setText(workshop.getPrice() + " €");
        isOnlineTextView.setText(workshop.isOnline() ? "Sí" : "No");
        speakerIdTextView.setText(String.valueOf(workshop.getSpeakerId()));
    }

    @Override
    public void showMessage(String message) {}

    @Override
    public void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}