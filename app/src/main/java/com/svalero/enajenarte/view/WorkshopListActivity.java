package com.svalero.enajenarte.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.svalero.enajenarte.R;
import com.svalero.enajenarte.adapter.WorkshopAdapter;
import com.svalero.enajenarte.contract.WorkshopListContract;
import com.svalero.enajenarte.domain.Workshop;
import com.svalero.enajenarte.presenter.WorkshopListPresenter;
import com.svalero.enajenarte.view.PreferencesActivity;

import java.util.ArrayList;
import java.util.List;

public class WorkshopListActivity extends AppCompatActivity implements WorkshopListContract.View {

    // Borrado delegado desde detail
    private static final String EXTRA_DELETE_WORKSHOP_ID = "delete_workshop_id";

    private WorkshopAdapter workshopAdapter;
    private List<Workshop> workshopList;
    private WorkshopListPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workshop_list);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        presenter = new WorkshopListPresenter(this);
        workshopList = new ArrayList<>();

        RecyclerView recyclerView = findViewById(R.id.workshop_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        workshopAdapter = new WorkshopAdapter(this, workshopList, workshop -> {
            Intent intent = new Intent(this, WorkshopDetailActivity.class);
            intent.putExtra("workshop_id", workshop.getId());
            startActivity(intent);
        },
                workshop -> showDeleteDialog(workshop)
        );

        recyclerView.setAdapter(workshopAdapter);

        setTitle(getString(R.string.title_workshop_list));
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Cuando se da orden de borrado en detail, se hace aquí
        handleDeleteFromIntent(getIntent());

        presenter.loadWorkshops(null, null, null);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    private void handleDeleteFromIntent(Intent intent) {
        if (intent == null) return;

        long idToDelete = intent.getLongExtra(EXTRA_DELETE_WORKSHOP_ID, -1);
        if (idToDelete != -1) {
            presenter.deleteWorkshop(idToDelete);
            // Borrado de la pila
            intent.removeExtra(EXTRA_DELETE_WORKSHOP_ID);
        }
    }

    private void showDeleteDialog(Workshop workshop) {
        new AlertDialog.Builder(this)
        .setTitle(getString(R.string.title_workshop_delete))
                .setMessage(getString(R.string.dialog_delete_workshop_message_named, workshop.getName()))
                .setPositiveButton(getString(R.string.btn_delete), (dialog, which) -> presenter.deleteWorkshop(workshop.getId()))
                .setNegativeButton(getString(R.string.btn_cancel), null)
                .show();
    }

    // ACTIONBAR

    // carga el menú de opciones para esta pantalla.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_workshop_list, menu);
        return true;
    }

    // Cuando el usuario pulsa crear, se abre la pantalla de edición.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
            return true;
        } else if (id == R.id.action_create_workshop) {
            Intent intent = new Intent(this, WorkshopEditActivity.class);
            startActivity(intent);
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
    public void showWorkshops(List<Workshop> workshops) {
        workshopList.clear();
        workshopList.addAll(workshops);
        workshopAdapter.notifyDataSetChanged();
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