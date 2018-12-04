package com.ema.db.repository;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;

@Dao
public interface InsertAsyncTask<T extends Object> {
    @Insert
    Long insert(T obj);
}

