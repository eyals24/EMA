package com.ema.view;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.ema.R.id;
import com.ema.R.layout;
import com.ema.R.menu;
import com.ema.R.string;
import com.ema.activity.ContactActivityStart;
import com.ema.activity.EventActivity;
import com.ema.activity.MissionActivityStart;
import com.ema.db.entity.Event;
import com.ema.viewmodel.EventViewModel;

import java.text.SimpleDateFormat;
import java.util.List;

import static com.ema.activity.EventActivityStart.NEW_EVENT_ACTIVITY_REQUEST_CODE;
import static java.util.Locale.ROOT;

public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.EventViewHolder> {

    static class EventViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewEventName, textViewEventPlace,
                textViewEventBudget, textViewEventDate;
        private final ImageView imageViewEventMenu;

        private EventViewHolder(View itemView) {
            super(itemView);
            textViewEventName = itemView.findViewById(id.textViewEventName);
            textViewEventPlace = itemView.findViewById(id.textViewEventPlace);
            textViewEventBudget = itemView.findViewById(id.textViewEventBudget);
            textViewEventDate = itemView.findViewById(id.textViewEventDate);
            imageViewEventMenu = itemView.findViewById(id.imageViewEventMenu);
        }
    }

    private final LayoutInflater inflater;
    private List<Event> events; // Cached copy of words
    private final EventViewModel eventViewModel;

    public EventListAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        eventViewModel = ViewModelProviders
                .of((FragmentActivity) context)
                .get(EventViewModel.class);
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
        View itemView = inflater
                .inflate(layout.recyclerview_event_item, parent, false);
        return new EventViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final EventViewHolder holder, final int position) {
        if (events != null) {
            final Event current = events.get(position);
            holder.textViewEventName.setText(current.getEventName());

            if (current.getEventPlace() != null && !current.getEventPlace().isEmpty()) {
                holder.textViewEventPlace.setText(current.getEventPlace());
            }
            if (current.getEventBudget() > 0) {
                holder.textViewEventBudget.setText(String.valueOf(current.getEventBudget()));
            }
            if (current.getEventDate() != null) {
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm", ROOT);
                holder.textViewEventDate.setText(format.format(current.getEventDate()));
            }

            holder.imageViewEventMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    PopupMenu popupMenu = new PopupMenu(inflater.getContext(),
                            holder.imageViewEventMenu);
                    popupMenu.inflate(menu.menu_event);
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case id.eventMenuItemDelete:
                                    eventViewModel.delete(current);
                                    break;
                                case id.eventMenuItemEdit: {
                                    Bundle bundle = new Bundle();
                                    bundle.putString("eventName", holder.textViewEventName.getText().toString());
                                    bundle.putString("eventPlace", holder.textViewEventPlace.getText().toString());
                                    bundle.putString("eventDate", holder.textViewEventDate.getText().toString());
                                    if (!holder.textViewEventBudget.getText().toString().isEmpty())
                                        bundle.putInt("eventBudget", Integer.valueOf(holder.textViewEventBudget.getText().toString()));
                                    bundle.putLong("eventId", current.getId());
                                    Intent intent = new Intent(v.getContext(), EventActivity.class);
                                    intent.putExtra("eventDataForUpdate", bundle);
                                    ((Activity) v.getContext()).startActivityForResult(intent, NEW_EVENT_ACTIVITY_REQUEST_CODE);
                                    break;
                                }
                                case id.eventMenuShowMissions: {
                                    Intent intent = new Intent(v.getContext(),
                                            MissionActivityStart.class);
                                    intent.putExtra("eventId", current.getId());
                                    v.getContext().startActivity(intent);
                                    break;
                                }
                                case id.eventMenuShowContacts: {
                                    Intent intent = new Intent(v.getContext(),
                                            ContactActivityStart.class);
                                    intent.putExtra("eventId", current.getId());
                                    v.getContext().startActivity(intent);
                                    break;
                                }
                            }
                            return false;
                        }
                    });
                    popupMenu.show();
                }
            });

        } else {
            // Covers the case of data not being ready yet.
            holder.textViewEventName.setText(string.event_no_data);
        }
    }

    public void setEvents(List<Event> events) {
        this.events = events;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return events != null ? events.size() : 0;
    }

}
