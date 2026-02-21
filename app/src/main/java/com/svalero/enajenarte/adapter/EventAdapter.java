package com.svalero.enajenarte.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.svalero.enajenarte.R;
import com.svalero.enajenarte.domain.Event;
import com.svalero.enajenarte.util.DateUtil;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {
    public interface OnEventClickListener {
        void onEventClick(Event event);
    }

    public interface OnEventLongClickListener {
        void onEventLongClick(Event event);
    }

    private final Context context;
    private final List<Event> eventList;
    private final OnEventClickListener clickListener;
    private final OnEventLongClickListener longClickListener;

    public EventAdapter(Context context, List<Event> eventList, OnEventClickListener clickListener, OnEventLongClickListener longClickListener) {
        this.context = context;
        this.eventList = eventList;
        this.clickListener = clickListener;
        this.longClickListener = longClickListener;
    }

    @NotNull
    @Override
    public EventViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_event, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Event event = eventList.get(position);

        holder.eventTitleTextView.setText(event.getTitle());
        holder.eventDateTextView.setText(
                DateUtil.formatDateTime(event.getEventDate())
        );

        holder.itemView.setOnClickListener(view ->
                clickListener.onEventClick(event));


        holder.itemView.setOnLongClickListener(view -> {
            longClickListener.onEventLongClick(event);
            return true;
        });
    }


    @Override
    public int getItemCount() {
        return eventList.size();
    }

     static class EventViewHolder extends RecyclerView.ViewHolder {

        TextView eventTitleTextView;
        TextView eventDateTextView;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            eventTitleTextView = itemView.findViewById(R.id.event_title);
            eventDateTextView = itemView.findViewById(R.id.event_date);
        }

    }
}
