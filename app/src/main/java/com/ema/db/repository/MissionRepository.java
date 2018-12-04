package com.ema.db.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;

import com.ema.db.EMARoomDB;
import com.ema.db.dao.MissionDao;
import com.ema.db.entity.Mission;

import java.util.List;

import lombok.Getter;

@Getter
public class MissionRepository {

    private final MissionDao missionDao;
    private final LiveData<List<Mission>> allMissions;

    public MissionRepository(Application application) {
        EMARoomDB db = EMARoomDB.getDB(application);
        missionDao = db.missionDao();
        allMissions = missionDao.getAllMissions();
    }

    public final void insert(Mission mission) {
        new InsertAsyncTaskImpl<Mission>(missionDao, mission).execute(mission);
    }

    public final void delete(Mission mission) {
        new DeleteAsyncTaskImpl<Mission>(missionDao).execute(mission);
    }

    public final void update(Mission mission) {
        new UpdateAsyncTaskImpl<Mission>(missionDao).execute(mission);
    }

    public final LiveData<List<Mission>> getAllMissionsForEvent(long eventId) {
        return missionDao.getAllMissionsForEvent(eventId);
    }
}
