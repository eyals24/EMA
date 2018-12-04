package com.ema.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import com.ema.db.entity.EventJoinContact;
import com.ema.db.repository.EventJoinContactRepository;

public class EventJoinContactViewModel extends AndroidViewModel {

    private final EventJoinContactRepository eventJoinContactRepository;

    public EventJoinContactViewModel(@NonNull Application application) {
        super(application);
        eventJoinContactRepository = new EventJoinContactRepository(application);
    }

    public void insert(EventJoinContact eventJoinContact) {
        eventJoinContactRepository.insert(eventJoinContact);
    }
}
