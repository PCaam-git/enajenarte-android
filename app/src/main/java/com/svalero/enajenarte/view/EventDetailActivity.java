package com.svalero.enajenarte.view;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;
import androidx.appcompat.app.AppCompatActivity;

import com.mapbox.geojson.Point;
import com.mapbox.maps.CameraOptions;
import com.mapbox.maps.MapView;
import com.mapbox.maps.Style;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManager;
import com.mapbox.maps.plugin.gestures.GesturesPlugin;
import com.mapbox.maps.plugin.gestures.GesturesUtils;
import com.svalero.enajenarte.R;
import com.svalero.enajenarte.contract.EventDetailContract;
import com.svalero.enajenarte.db.DatabaseUtil;
import com.svalero.enajenarte.db.entity.EventEntity;
import com.svalero.enajenarte.domain.Event;
import com.svalero.enajenarte.presenter.EventDetailPresenter;
import com.svalero.enajenarte.util.MapUtils;

import com.svalero.enajenarte.util.DateUtil;

public class EventDetailActivity extends AppCompatActivity implements EventDetailContract.View {

    // La opción borrar se aplicará en el listado de eventos, no en el detalle.
    private static final String EXTRA_DELETE_EVENT_ID = "delete_event_id";

    private EventDetailPresenter presenter;
    private long eventId;
    private TextView textTitle;
    private TextView textLocation;
    private TextView textEntryFee;
    private TextView textPublic;
    private TextView textDate;

    private ImageView imageEventDetail;
    private MapView mapView;
    private PointAnnotationManager pointAnnotationManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        presenter = new EventDetailPresenter(this);

        textTitle = findViewById(R.id.text_event_title);
        textDate = findViewById(R.id.text_event_date);
        textLocation = findViewById(R.id.text_event_location);
        textEntryFee = findViewById(R.id.text_event_entry_fee);
        textPublic = findViewById(R.id.text_event_public);
        imageEventDetail = findViewById(R.id.image_event_detail);
        mapView = findViewById(R.id.map_event_detail);

        eventId = getIntent().getLongExtra("event_id", -1);

        if (textTitle == null) {
            Toast.makeText(this, "Layout/ids incorrectos", Toast.LENGTH_SHORT).show();
            return;
        }

        if (eventId == -1) {
            Toast.makeText(this, "ID del evento no recibido", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        setTitle("Detalle evento");

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        presenter.loadEvent(eventId);
        applyMapPreference();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (eventId != -1) {
            applyMapPreference();
        }
        presenter.loadEvent(eventId);
    }

    private void applyMapPreference() {
        SharedPreferences preferences =
                PreferenceManager.getDefaultSharedPreferences(this);

        boolean showMaps = preferences.getBoolean("show_maps", true);

        if (!showMaps) {
            mapView.setVisibility(View.GONE);
            return;
        }
            mapView.setVisibility(View.VISIBLE);
            mapView.getMapboxMap().loadStyleUri(Style.MAPBOX_STREETS);
            pointAnnotationManager = MapUtils.buildAnnotationManager(mapView);

            EventEntity localEvent = DatabaseUtil.getDb(this)
                    .eventDao()
                    .findById(eventId);

            if (localEvent != null &&
                    localEvent.getLatitude() != null &&
                    localEvent.getLongitude() != null) {

                Point point = Point.fromLngLat(
                        localEvent.getLongitude(),
                        localEvent.getLatitude()
                );

                MapUtils.addMarker(this, pointAnnotationManager, point);

                mapView.getMapboxMap().setCamera(
                        new CameraOptions.Builder()
                                .center(point)
                                .zoom(16.0)
                                .build()
                );
            }

            GesturesPlugin gesturesPlugin = GesturesUtils.getGestures(mapView);
            gesturesPlugin.setScrollEnabled(false);
            gesturesPlugin.setRotateEnabled(false);
            gesturesPlugin.setPitchEnabled(false);
    }


    // Se abre la pantalla de edición
    private void openEdit() {
        Intent intent = new Intent(this, EventEditActivity.class);
        intent.putExtra("event_id", eventId);
        startActivity(intent);
    }

    // Confirmando la eliminación para evitar borrados accidentales
    private void confirmDelete() {
        new AlertDialog.Builder(this)
                .setTitle("Eliminar evento")
                .setMessage("¿Seguro que quieres eliminar este evento?")
                .setPositiveButton("Eliminar", (dialog, which) -> redirectToListForDelete())
                .setNegativeButton("Cancelar", null)
                .show();
    }

    // Si se pulsa Delete, se vuelve al listado y se borra el evento.
    private void redirectToListForDelete() {

        Intent intent = new Intent(this, EventListActivity.class);
        intent.putExtra(EXTRA_DELETE_EVENT_ID, eventId);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        startActivity(intent);
        finish();
    }

    // ACTIONBAR

    // Carga el menú del detalle del evento
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_event_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
            return true;
        } else if (id == R.id.action_edit_event) {
            openEdit();
            return true;
        } else if (id == R.id.action_delete_event) {
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
    public void showEvent(Event event) {
        textTitle.setText(event.getTitle());
        textLocation.setText(event.getLocation());
        textDate.setText(DateUtil.formatDateTime(event.getEventDate()));
        textEntryFee.setText("Precio " + event.getEntryFee() +  " €");
        textPublic.setText("Evento público: " + (event.isPublic() ? "Sí" : "No"));
        showImageIfExists();
    }

    private void showImageIfExists() {

        // Busca la Uri guardada en Room para este evento
        EventEntity entity = DatabaseUtil.getDb(this).eventDao().findById(eventId);

        if (entity != null && entity.getImageUri() != null && !entity.getImageUri().isEmpty()) {
            imageEventDetail.setImageURI(Uri.parse(entity.getImageUri()));
            imageEventDetail.setVisibility(View.VISIBLE);
          //  Toast.makeText(this, "Room: no hay imagen para eventId=" + eventId, Toast.LENGTH_SHORT).show();
        } else {
            // Sin placeholder
            imageEventDetail.setVisibility(View.GONE);
           // Toast.makeText(this, "Room: imageUri=" + entity.getImageUri(), Toast.LENGTH_SHORT).show();
        }
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