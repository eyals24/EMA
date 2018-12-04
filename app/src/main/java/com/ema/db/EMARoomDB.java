package com.ema.db;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.ema.db.dao.ContactDao;
import com.ema.db.dao.EventDao;
import com.ema.db.dao.EventJoinContactDao;
import com.ema.db.dao.MissionDao;
import com.ema.db.entity.Contact;
import com.ema.db.entity.Event;
import com.ema.db.entity.EventJoinContact;
import com.ema.db.entity.Mission;

@Database(entities = {Event.class,
        Mission.class,
        Contact.class,
        EventJoinContact.class}, version = 1, exportSchema = false)
public abstract class EMARoomDB extends RoomDatabase {

    public abstract EventDao eventDao();

    public abstract MissionDao missionDao();

    public abstract ContactDao contactDao();

    public abstract EventJoinContactDao eventJoinContactDao();

    private static volatile EMARoomDB INSTANCE;

    private static RoomDatabase.Callback sRoomDatabaseCallback =
            new RoomDatabase.Callback() {

                @Override
                public void onOpen(@NonNull SupportSQLiteDatabase db) {
                    super.onOpen(db);
                    new PopulateDbAsync(INSTANCE).execute();
                }
            };

    public static EMARoomDB getDB(final Context context) {
        if (INSTANCE == null) {
            synchronized (EMARoomDB.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            EMARoomDB.class, "EMARoomDB")
                            //.addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final EventDao eventDao;

        PopulateDbAsync(EMARoomDB db) {
            eventDao = db.eventDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            eventDao.deleteAllEvents();
            Event event = new Event();
            event.setEventName("test1");
            eventDao.insert(event);
            event = new Event();
            event.setEventName("test2");
            eventDao.insert(event);
            return null;
        }
    }
}
