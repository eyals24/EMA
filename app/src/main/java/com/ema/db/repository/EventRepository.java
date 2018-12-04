package com.ema.db.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;

import com.ema.db.EMARoomDB;
import com.ema.db.dao.EventDao;
import com.ema.db.entity.Event;

import java.util.List;

import lombok.Getter;

@Getter
public class EventRepository {

    private final EventDao eventDao;
    private final LiveData<List<Event>> allEvents;

    public EventRepository(Application application) {
        EMARoomDB db = EMARoomDB.getDB(application);
        eventDao = db.eventDao();
        allEvents = eventDao.getAllEvents();
    }

    public final void insert(Event event) {
        new InsertAsyncTaskImpl<Event>(eventDao, event).execute(event);
    }

    public final void delete(Event event) {
        new DeleteAsyncTaskImpl<Event>(eventDao).execute(event);
    }

    public final void update(Event event) {
        new UpdateAsyncTaskImpl<Event>(eventDao).execute(event);
    }
}
