package com.svalero.enajenarte.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.svalero.enajenarte.R;
import com.svalero.enajenarte.contract.WorkshopDetailContract;
import com.svalero.enajenarte.domain.Workshop;
import com.svalero.enajenarte.presenter.WorkshopDetailPresenter;

public class WorkshopDetailActivity extends AppCompatActivity implements WorkshopDetailContract.View {

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

        findViewById(R.id.button_edit_workshop).setOnClickListener(view -> {
            Intent intent = new Intent(this, WorkshopEditActivity.class);
            intent.putExtra("workshop_id", workshopId);
            startActivity(intent);
        });


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


//        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_workshop_detail);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
    }
}