package com.ema.db.repository;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Update;

@Dao
public interface UpdateAsyncTask<T extends Object> {
    @Update
    void update(T obj);
}
