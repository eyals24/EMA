package com.ema.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.ema.MainActivity;
import com.ema.R;
import com.ema.db.entity.Mission;
import com.ema.view.MissionListAdapter;
import com.ema.viewmodel.MissionViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Observable;

import static java.util.Locale.ROOT;

public class MissionActivityStart extends AppCompatActivity {

    public final static int MISSION_ACTIVITY_REQUEST_CODE = 1;
    private MissionViewModel missionViewModel;
    private Long eventId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mission_start);
        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final FloatingActionButton fab = findViewById(R.id.mission_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MissionActivityStart.this,
                        MissionActivity.class);
                startActivityForResult(intent, MISSION_ACTIVITY_REQUEST_CODE);
            }
        });

        final RecyclerView recyclerView = findViewById(R.id.recyclerview_mission);
        final MissionListAdapter adapter = new MissionListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        final Intent intent = getIntent();
        eventId = intent.getLongExtra("eventId", 0);

        missionViewModel = ViewModelProviders.of(this).get(MissionViewModel.class);
        if (eventId > 0) {
            missionViewModel.getAllMissionsForEvent(eventId)
                    .observe(this, new Observer<List<Mission>>() {
                        @Override
                        public void onChanged(@Nullable List<Mission> missions) {
                            adapter.setMissions(missions);
                        }
                    });
        } else {
            fab.hide();
            missionViewModel.getAllMissions()
                    .observe(this, new Observer<List<Mission>>() {
                        @Override
                        public void onChanged(@Nullable List<Mission> missions) {
                            adapter.setMissions(missions);
                        }
                    });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_events: {
                Intent intent = new Intent(MissionActivityStart.this,
                        MainActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.action_missions: {
                Intent intent = new Intent(MissionActivityStart.this,
                        MissionActivityStart.class);
                startActivity(intent);
                break;
            }
            case R.id.action_contacts: {
                Intent intent = new Intent(MissionActivityStart.this,
                        ContactActivityStart.class);
                startActivity(intent);
                break;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == MISSION_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            Bundle bundle = data.getBundleExtra(MissionActivity.EXTRA_REPLY);
            Mission mission = new Mission();
            mission.setId(bundle.getLong("missionId"));
            mission.setEventId(eventId);
            mission.setMissionName(bundle.getString("missionName"));
            mission.setMissionPlace(bundle.getString("missionPlace"));
            mission.setMissionDescription(bundle.getString("missionDescription"));

            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm", ROOT);
            try {
                if (!bundle.getString("missionStartDate").isEmpty())
                    mission.setMissionStartDate(format.parse(bundle.getString("missionStartDate")));
                if (!bundle.getString("missionEndDate").isEmpty())
                    mission.setMissionEndDate(format.parse(bundle.getString("missionEndDate")));
                if (mission.getId() > 0)
                    missionViewModel.update(mission);
                else {
                    mission.addObserver(new java.util.Observer() {
                        @Override
                        public void update(Observable o, Object arg) {
                            Log.d("mission_id_after_insert",
                                    Long.toString(((Mission) o).getId()));
                        }
                    });
                    missionViewModel.insert(mission);
                }
            } catch (ParseException e) {
                e.printStackTrace();
                Toast.makeText(
                        getApplicationContext(),
                        R.string.wrong_date_not_saved,
                        Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(
                    getApplicationContext(),
                    R.string.empty_not_saved,
                    Toast.LENGTH_LONG).show();
        }
    }
}
