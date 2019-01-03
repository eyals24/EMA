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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.ema.R;
import com.ema.db.entity.Contact;
import com.ema.db.entity.EventJoinContact;
import com.ema.view.ContactListAdapter;
import com.ema.viewmodel.ContactViewModel;
import com.ema.viewmodel.EventJoinContactViewModel;

import java.util.List;
import java.util.Observable;

public class ContactActivityStart extends AppCompatActivity {

    public final static int CONTACT_ACTIVITY_REQUEST_CODE = 1;
    private ContactViewModel contactViewModel;
    private EventJoinContactViewModel eventJoinContactViewModel;
    private Long eventId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_start);
        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final FloatingActionButton fab = findViewById(R.id.contact_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ContactActivityStart.this,
                        ContactActivity.class);
                startActivityForResult(intent, CONTACT_ACTIVITY_REQUEST_CODE);
            }
        });

        final RecyclerView recyclerView = findViewById(R.id.recyclerview_contact);
        final ContactListAdapter adapter = new ContactListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        final Intent intent = getIntent();
        eventId = intent.getLongExtra("eventId", 0);

        contactViewModel = ViewModelProviders
                .of(this)
                .get(ContactViewModel.class);
        eventJoinContactViewModel = ViewModelProviders
                .of(this)
                .get(EventJoinContactViewModel.class);
        if (eventId > 0) {
            contactViewModel.getAllContactsForEvent(eventId)
                    .observe(this, new Observer<List<Contact>>() {
                        @Override
                        public void onChanged(@Nullable List<Contact> contacts) {
                            adapter.setContacts(contacts);
                        }
                    });
        } else {
            contactViewModel.getAllContacts()
                    .observe(this, new Observer<List<Contact>>() {
                        @Override
                        public void onChanged(@Nullable List<Contact> contacts) {
                            adapter.setContacts(contacts);
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
                Intent intent = new Intent(ContactActivityStart.this,
                        EventActivityStart.class);
                startActivity(intent);
                break;
            }
            case R.id.action_missions: {
                Intent intent = new Intent(ContactActivityStart.this,
                        MissionActivityStart.class);
                startActivity(intent);
                break;
            }
            case R.id.action_contacts: {
                Intent intent = new Intent(ContactActivityStart.this,
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

        if (requestCode == CONTACT_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            Bundle bundle = data.getBundleExtra(ContactActivity.EXTRA_REPLY);
            Contact contact = new Contact();
            contact.setId(bundle.getLong("contactId"));
            contact.setContactFirstName(bundle.getString("contactFirstName"));
            contact.setContactLastName(bundle.getString("contactLastName"));
            contact.setContactPhone(bundle.getString("contactPhone"));
            contact.setContactEmail(bundle.getString("contactEmail"));

            if (contact.getId() > 0) {
                contactViewModel.update(contact);
            } else {

                if (eventId > 0) {
                    contact.addObserver(new java.util.Observer() {
                        @Override
                        public void update(Observable o, Object arg) {
                            EventJoinContact eventJoinContact = new EventJoinContact();
                            eventJoinContact.setEventId(eventId);
                            eventJoinContact.setContactId(((Contact) o).getId());
                            eventJoinContactViewModel.insert(eventJoinContact);
                        }
                    });
                }

                contactViewModel.insert(contact);
            }
        }

    }

}
