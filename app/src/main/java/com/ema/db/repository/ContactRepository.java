package com.ema.db.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;

import com.ema.db.EMARoomDB;
import com.ema.db.dao.ContactDao;
import com.ema.db.entity.Contact;

import java.util.List;

import lombok.Getter;

@Getter
public class ContactRepository {

    private final ContactDao contactDao;
    private final LiveData<List<Contact>> allContacts;

    public ContactRepository(Application application) {
        EMARoomDB db = EMARoomDB.getDB(application);
        contactDao = db.contactDao();
        allContacts = contactDao.getAllContacts();
    }

    public final void insert(Contact contact) {
        new InsertAsyncTaskImpl<Contact>(contactDao, contact).execute(contact);
    }

    public final void delete(Contact contact) {
        new DeleteAsyncTaskImpl<Contact>(contactDao).execute(contact);
    }

    public final void update(Contact contact) {
        new UpdateAsyncTaskImpl<Contact>(contactDao).execute(contact);
    }
}
