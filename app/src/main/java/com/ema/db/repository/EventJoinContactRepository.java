package com.ema.db.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;

import com.ema.db.EMARoomDB;
import com.ema.db.dao.EventJoinContactDao;
import com.ema.db.entity.Contact;
import com.ema.db.entity.Event;
import com.ema.db.entity.EventJoinContact;
import com.ema.db.entity.IdPopulator;

import java.util.List;

import lombok.Getter;

@Getter
public class EventJoinContactRepository {

    private final EventJoinContactDao eventJoinContactDao;

    public EventJoinContactRepository(Application application) {
        EMARoomDB db = EMARoomDB.getDB(application);
        eventJoinContactDao = db.eventJoinContactDao();
    }

    public final void insert(EventJoinContact eventJoinContact) {
        new InsertAsyncTaskImpl<EventJoinContact>(eventJoinContactDao, new IdPopulator() {
            @Override
            public void populateId(Long id) {
                //TODO do nothing
            }
        }).execute(eventJoinContact);
    }

    public final void delete(EventJoinContact eventJoinContact) {
        new DeleteAsyncTaskImpl<EventJoinContact>(eventJoinContactDao).execute(eventJoinContact);
    }

    public final LiveData<List<Event>> getAllEventsForContact(final long contactId) {
        return eventJoinContactDao.getAllEventsForContact(contactId);
    }

    public final LiveData<List<Contact>> getAllContactsForEvent(final long eventId) {
        return eventJoinContactDao.getAllContactsForEvent(eventId);
    }
}
