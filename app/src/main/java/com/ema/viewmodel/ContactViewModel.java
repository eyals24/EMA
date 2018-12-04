package com.ema.viewmodel;


import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.ema.db.entity.Contact;
import com.ema.db.repository.ContactRepository;
import com.ema.db.repository.EventJoinContactRepository;

import java.util.List;

import lombok.Getter;

@Getter
public class ContactViewModel extends AndroidViewModel {

    private final ContactRepository contactRepository;
    private final LiveData<List<Contact>> allContacts;

    public ContactViewModel(@NonNull Application application) {
        super(application);
        contactRepository = new ContactRepository(application);
        allContacts = contactRepository.getAllContacts();
    }

    public final void insert(Contact contact) {
        contactRepository.insert(contact);
    }

    public final void delete(Contact contact) {
        contactRepository.delete(contact);
    }

    public final void update(Contact contact) {
        contactRepository.update(contact);
    }

    public final LiveData<List<Contact>> getAllContactsForEvent(final long eventId) {
        final EventJoinContactRepository eventJoinContactRepository =
                new EventJoinContactRepository(super.getApplication());
        return eventJoinContactRepository.getAllContactsForEvent(eventId);
    }
}
