package com.ema.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.ema.db.entity.Mission;
import com.ema.db.repository.DeleteAsyncTask;
import com.ema.db.repository.InsertAsyncTask;
import com.ema.db.repository.UpdateAsyncTask;

import java.util.List;

@Dao
public interface MissionDao extends InsertAsyncTask<Mission>,
        DeleteAsyncTask<Mission>,
        UpdateAsyncTask<Mission> {

    @Query("SELECT * FROM missions ORDER BY mission_name ASC")
    LiveData<List<Mission>> getAllMissions();

    @Query("SELECT * FROM missions WHERE event_id_fky=:eventId")
    LiveData<List<Mission>> getAllMissionsForEvent(final long eventId);

    @Query("DELETE FROM missions")
    void deleteAllMissions();
}
