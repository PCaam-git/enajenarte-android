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

import java.util.ArrayList;
import java.util.List;

public class WorkshopListActivity extends AppCompatActivity implements WorkshopListContract.View {

    private WorkshopAdapter workshopAdapter;
    private List<Workshop> workshopList;
    private WorkshopListPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workshop_list);

        presenter = new WorkshopListPresenter(this);
        workshopList = new ArrayList<>();

        Button createButton = findViewById(R.id.button_create_workshop);
        createButton.setOnClickListener(view -> {
            // Navegación a la pantalla de creación/edición
            Intent intent = new Intent(this, WorkshopEditActivity.class);
            startActivity(intent);
        });


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

        setTitle("Talleres");
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.loadWorkshops(null, null, null);
    }

    private void showDeleteDialog(Workshop workshop) {
        new AlertDialog.Builder(this)
        .setTitle("Eliminar taller")
        .setMessage("¿Seguro que quieres eliminar \"" + workshop.getName() + "\"?")
        .setPositiveButton("Eliminar", (dialog, which) -> presenter.deleteWorkshop(workshop.getId()))
        .setNegativeButton("Cancelar", null)
        .show();
    }

    // ACTIONBAR

    // carga el menú de opciones para esta pantalla.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_workshop_list, menu);
        return true;
    }

    // responde a las pulsaciones del menú. Cuando el usuario pulsa crear, se abre la pantalla de edición.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_create_workshop) {
            Intent intent = new Intent(this, WorkshopEditActivity.class);
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