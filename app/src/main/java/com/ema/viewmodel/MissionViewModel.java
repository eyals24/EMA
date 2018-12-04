package com.ema.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.ema.db.entity.Mission;
import com.ema.db.repository.MissionRepository;

import java.util.List;

import lombok.Getter;

@Getter
public class MissionViewModel extends AndroidViewModel {

    private final MissionRepository missionRepository;
    private final LiveData<List<Mission>> allMissions;

    public MissionViewModel(Application application) {
        super(application);
        missionRepository = new MissionRepository(application);
        allMissions = missionRepository.getAllMissions();
    }

    public final void insert(Mission mission) {
        missionRepository.insert(mission);
    }

    public final void delete(Mission mission) {
        missionRepository.delete(mission);
    }

    public final void update(Mission mission) {
        missionRepository.update(mission);
    }

    public final LiveData<List<Mission>> getAllMissionsForEvent(long eventId) {
        return missionRepository.getAllMissionsForEvent(eventId);
    }
}
