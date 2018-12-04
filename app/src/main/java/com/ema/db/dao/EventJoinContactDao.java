package com.ema.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.ema.db.entity.Contact;
import com.ema.db.entity.Event;
import com.ema.db.entity.EventJoinContact;
import com.ema.db.repository.DeleteAsyncTask;
import com.ema.db.repository.InsertAsyncTask;

import java.util.List;

@Dao
public interface EventJoinContactDao extends InsertAsyncTask<EventJoinContact>,
        DeleteAsyncTask<EventJoinContact> {

    @Query("SELECT events.* FROM events INNER JOIN event_join_contact ON" +
            " id=event_id_fky WHERE contact_id_fky=:contactId")
    LiveData<List<Event>> getAllEventsForContact(final long contactId);

    @Query("SELECT contacts.* FROM contacts INNER JOIN event_join_contact ON" +
            " id=contact_id_fky WHERE event_id_fky=:eventId")
    LiveData<List<Contact>> getAllContactsForEvent(final long eventId);
}
