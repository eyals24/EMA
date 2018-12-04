package com.ema.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.ema.db.entity.Event;
import com.ema.db.repository.DeleteAsyncTask;
import com.ema.db.repository.InsertAsyncTask;
import com.ema.db.repository.UpdateAsyncTask;

import java.util.List;

@Dao
public interface EventDao extends InsertAsyncTask<Event>,
        DeleteAsyncTask<Event>,
        UpdateAsyncTask<Event> {

    @Query("SELECT * FROM events ORDER BY event_name ASC")
    LiveData<List<Event>> getAllEvents();

    @Query("DELETE FROM events")
    void deleteAllEvents();
}

