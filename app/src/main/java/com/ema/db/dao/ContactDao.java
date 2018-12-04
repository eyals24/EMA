package com.ema.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.ema.db.entity.Contact;
import com.ema.db.repository.DeleteAsyncTask;
import com.ema.db.repository.InsertAsyncTask;
import com.ema.db.repository.UpdateAsyncTask;

import java.util.List;

@Dao
public interface ContactDao extends InsertAsyncTask<Contact>,
        DeleteAsyncTask<Contact>,
        UpdateAsyncTask<Contact> {

    @Query("SELECT * FROM contacts ORDER BY contact_first_name ASC")
    LiveData<List<Contact>> getAllContacts();

    @Query("DELETE FROM contacts")
    void deleteAllContacts();

}
