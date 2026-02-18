package com.svalero.enajenarte.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.svalero.enajenarte.R;
import com.svalero.enajenarte.domain.Workshop;

import org.jetbrains.annotations.NotNull;

import java.util.List;

// Convierte Workshops en filas visuales
public class WorkshopAdapter extends RecyclerView.Adapter<WorkshopAdapter.WorkshopViewHolder> {

    // Da acceso a recursos Android
    private Context context;
    private List<Workshop> workshopList;

    // Se entregan los datos al adaptador
    public WorkshopAdapter(Context context, List<Workshop> workshopList) {
        this.context = context;
        this.workshopList = workshopList;
    }

    // Crea una fila vacía basada en el xml, todavía no hay datos
    @NotNull
    @Override
    public WorkshopViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_workshop, parent, false);
        return new WorkshopViewHolder(view);
    }

    // Rellena una fila con datos del workshop en la posición dada
    @Override
    public void onBindViewHolder(@NotNull WorkshopViewHolder holder, int position) {
        Workshop workshop = workshopList.get(position);
        holder.workshopName.setText(workshop.getName());
        holder.workshopDate.setText(workshop.getStartDate() !=null ? workshop.getStartDate().toString() : "");
    }

    // Devuelve el número de filas que hay que dibujar
    @Override
    public int getItemCount() {
        return workshopList.size();
    }

    // Clase que representa una fila visual
    // Cada fila contiene un nombre y una fecha
    public static class WorkshopViewHolder extends RecyclerView.ViewHolder {
        TextView workshopName;
        TextView workshopDate;

        public WorkshopViewHolder(@NotNull View itemView) {
            super(itemView);
            // R = resource index generado automáticamente por android
            // Devuelve el TextView cuyo id en XML es workshop_name
            workshopName = itemView.findViewById(R.id.workshop_name);
            workshopDate = itemView.findViewById(R.id.workshop_date);
        }
    }
}
