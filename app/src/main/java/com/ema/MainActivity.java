package com.ema;

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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.ema.activity.ContactActivityStart;
import com.ema.activity.EventActivity;
import com.ema.activity.MissionActivityStart;
import com.ema.db.entity.Event;
import com.ema.db.entity.EventJoinContact;
import com.ema.view.EventListAdapter;
import com.ema.viewmodel.EventJoinContactViewModel;
import com.ema.viewmodel.EventViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Observable;

import static java.util.Locale.ROOT;

public class MainActivity extends AppCompatActivity {

    public static final int NEW_EVENT_ACTIVITY_REQUEST_CODE = 1;
    private EventViewModel eventViewModel;
    private EventJoinContactViewModel eventJoinContactViewModel;
    private Long contactId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EventActivity.class);
                startActivityForResult(intent, NEW_EVENT_ACTIVITY_REQUEST_CODE);
            }
        });
        RecyclerView recyclerView = findViewById(R.id.recyclerview_event);
        final EventListAdapter adapter = new EventListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        final Intent intent = getIntent();
        contactId = intent.getLongExtra("contactId", 0);

        eventViewModel = ViewModelProviders
                .of(this)
                .get(EventViewModel.class);
        eventJoinContactViewModel = ViewModelProviders
                .of(this)
                .get(EventJoinContactViewModel.class);
        if (contactId > 0) {
            eventViewModel.getAllEventsForContact(contactId)
                    .observe(this, new Observer<List<Event>>() {
                        @Override
                        public void onChanged(@Nullable List<Event> events) {
                            adapter.setEvents(events);
                        }
                    });
        } else {
            eventViewModel.getAllEvents()
                    .observe(this, new Observer<List<Event>>() {
                        @Override
                        public void onChanged(@Nullable List<Event> events) {
                            adapter.setEvents(events);
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
                Intent intent = new Intent(MainActivity.this,
                        MainActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.action_missions: {
                Intent intent = new Intent(MainActivity.this,
                        MissionActivityStart.class);
                startActivity(intent);
                break;
            }
            case R.id.action_contacts: {
                Intent intent = new Intent(MainActivity.this,
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

        if (requestCode == NEW_EVENT_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            Bundle bundle = data.getBundleExtra(EventActivity.EXTRA_REPLY);
            Event event = new Event();
            event.setEventName(bundle.getString("eventName"));
            event.setEventPlace(bundle.getString("eventPlace"));
            event.setEventBudget(bundle.getInt("eventBudget"));
            event.setId(bundle.getInt("eventId"));
            String eventDate = bundle.getString("eventDate");

            try {
                if (eventDate != null && !eventDate.isEmpty()) {
                    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm", ROOT);
                    event.setEventDate(format.parse(bundle.getString("eventDate")));
                }
            } catch (ParseException e) {
                e.printStackTrace();
                Toast.makeText(
                        getApplicationContext(),
                        R.string.wrong_date_not_saved,
                        Toast.LENGTH_LONG).show();
            }
            if (event.getId() > 0) {
                eventViewModel.update(event);
            } else {

                if (contactId > 0) {
                    event.addObserver(new java.util.Observer() {
                        @Override
                        public void update(Observable o, Object arg) {
                            EventJoinContact eventJoinContact = new EventJoinContact();
                            eventJoinContact.setContactId(contactId);
                            eventJoinContact.setEventId((((Event) o).getId()));
                            eventJoinContactViewModel.insert(eventJoinContact);
                        }
                    });
                }

                eventViewModel.insert(event);
            }
        } else {
            Toast.makeText(
                    getApplicationContext(),
                    R.string.empty_not_saved,
                    Toast.LENGTH_LONG).show();
        }
    }

}
