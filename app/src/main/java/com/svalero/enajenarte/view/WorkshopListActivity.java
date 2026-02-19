package com.svalero.enajenarte.view;

import android.content.Intent;
import android.os.Bundle;
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