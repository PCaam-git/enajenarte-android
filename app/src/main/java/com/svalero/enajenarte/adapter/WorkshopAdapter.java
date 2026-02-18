package com.svalero.enajenarte.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.svalero.enajenarte.R;
import com.svalero.enajenarte.domain.Workshop;

import java.util.List;

public class WorkshopAdapter extends RecyclerView.Adapter<WorkshopAdapter.WorkshopViewHolder> {

    public interface OnWorkshopLongClickListener {
        void onWorkshopLongClick(Workshop workshop);
    }

    private final Context context;
    private final List<Workshop> workshopList;
    private final OnWorkshopLongClickListener longClickListener;

    public WorkshopAdapter(Context context, List<Workshop> workshopList, OnWorkshopLongClickListener longClickListener) {
        this.context = context;
        this.workshopList = workshopList;
        this.longClickListener = longClickListener;
    }

    @NonNull
    @Override
    public WorkshopViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rowView = LayoutInflater.from(context).inflate(R.layout.item_workshop, parent, false);
        return new WorkshopViewHolder(rowView);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkshopViewHolder holder, int position) {

        Workshop workshop = workshopList.get(position);

        holder.workshopNameTextView.setText(workshop.getName());
        holder.workshopDateTextView.setText(workshop.getStartDate() != null
                ? workshop.getStartDate().toString()
                : "");

        holder.itemView.setOnLongClickListener(v -> {
            if (longClickListener != null) {
                longClickListener.onWorkshopLongClick(workshop);
                return true;
            }
            return false;
        });
    }

    @Override
    public int getItemCount() {
        return workshopList.size();
    }

    public static class WorkshopViewHolder extends RecyclerView.ViewHolder {

        TextView workshopNameTextView;
        TextView workshopDateTextView;

        public WorkshopViewHolder(@NonNull View itemView) {
            super(itemView);
            workshopNameTextView = itemView.findViewById(R.id.workshop_name);
            workshopDateTextView = itemView.findViewById(R.id.workshop_date);
        }
    }
}
