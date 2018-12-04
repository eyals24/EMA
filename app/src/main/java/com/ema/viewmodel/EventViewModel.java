package com.ema.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.ema.db.entity.Event;
import com.ema.db.repository.EventJoinContactRepository;
import com.ema.db.repository.EventRepository;

import java.util.List;

import lombok.Getter;

@Getter
public class EventViewModel extends AndroidViewModel {

    private final EventRepository eventRepository;
    private final LiveData<List<Event>> allEvents;

    public EventViewModel(Application application) {
        super(application);
        eventRepository = new EventRepository(application);
        allEvents = eventRepository.getAllEvents();
    }

    public void insert(Event event) {
        eventRepository.insert(event);
    }

    public void delete(Event event) {
        eventRepository.delete(event);
    }

    public void update(Event event) {
        eventRepository.update(event);
    }

    public final LiveData<List<Event>> getAllEventsForContact(final long contactId) {
        final EventJoinContactRepository eventJoinContactRepository =
                new EventJoinContactRepository(super.getApplication());
        return eventJoinContactRepository.getAllEventsForContact(contactId);
    }
}

