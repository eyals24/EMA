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

import com.ema.R.*;
import com.ema.activity.MissionActivity;
import com.ema.db.entity.Mission;
import com.ema.viewmodel.MissionViewModel;

import java.text.SimpleDateFormat;
import java.util.List;

import static com.ema.activity.MissionActivityStart.MISSION_ACTIVITY_REQUEST_CODE;
import static java.util.Locale.ROOT;

public class MissionListAdapter extends RecyclerView.Adapter<MissionListAdapter.MissionViewHolder> {

    static class MissionViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewMissionName, textViewMissionPlace,
                textViewMissionStartDate, textViewMissionEndDate, textViewMissionDescription;
        private final ImageView imageViewMissionMenu;

        private MissionViewHolder(View itemView) {
            super(itemView);
            textViewMissionName = itemView.findViewById(id.textViewMissionName);
            textViewMissionPlace = itemView.findViewById(id.textViewMissionPlace);
            textViewMissionStartDate = itemView.findViewById(id.textViewMissionStartDate);
            textViewMissionEndDate = itemView.findViewById(id.textViewMissionEndDate);
            textViewMissionDescription = itemView.findViewById(id.textViewMissionDescription);
            imageViewMissionMenu = itemView.findViewById(id.imageViewMissionMenu);
        }
    }

    private final LayoutInflater inflater;
    private List<Mission> missions;
    private final MissionViewModel missionViewModel;

    public MissionListAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        missionViewModel = ViewModelProviders
                .of((FragmentActivity) context)
                .get(MissionViewModel.class);
    }

    @NonNull
    @Override
    public MissionViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
        View itemView = inflater
                .inflate(layout.recyclerview_mission_item, parent, false);
        return new MissionViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MissionViewHolder holder, final int position) {
        if (missions != null) {
            final Mission current = missions.get(position);
            holder.textViewMissionName.setText(current.getMissionName());

            if (current.getMissionPlace() != null && !current.getMissionPlace().isEmpty()) {
                holder.textViewMissionPlace.setText(current.getMissionPlace());
            }

            if (current.getMissionDescription() != null && !current.getMissionDescription().isEmpty()) {
                holder.textViewMissionDescription.setText(current.getMissionDescription());
            }

            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm", ROOT);
            if (current.getMissionStartDate() != null)
                holder.textViewMissionStartDate.setText(format.format(current.getMissionStartDate()));
            if (current.getMissionEndDate() != null)
                holder.textViewMissionEndDate.setText(format.format(current.getMissionEndDate()));

            holder.imageViewMissionMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    PopupMenu popupMenu = new PopupMenu(inflater.getContext(),
                            holder.imageViewMissionMenu);
                    popupMenu.inflate(menu.menu_mission);
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case id.missionMenuItemDelete:
                                    missionViewModel.delete(current);
                                    break;
                                case id.missionMenuItemEdit: {
                                    Bundle bundle = new Bundle();
                                    bundle.putLong("missionId", current.getId());
                                    bundle.putString("missionName", holder.textViewMissionName.getText().toString());
                                    bundle.putString("missionPlace", holder.textViewMissionPlace.getText().toString());
                                    bundle.putString("missionStartDate", holder.textViewMissionStartDate.getText().toString());
                                    bundle.putString("missionEndDate", holder.textViewMissionEndDate.getText().toString());
                                    bundle.putString("missionDescription", holder.textViewMissionDescription.getText().toString());
                                    Intent intent = new Intent(v.getContext(), MissionActivity.class);
                                    intent.putExtra("missionDataForUpdate", bundle);
                                    ((Activity) v.getContext()).startActivityForResult(intent, MISSION_ACTIVITY_REQUEST_CODE);
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
            holder.textViewMissionName.setText(string.mission_no_data);
        }
    }

    public void setMissions(List<Mission> missions) {
        this.missions = missions;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return missions != null ? missions.size() : 0;
    }

}
