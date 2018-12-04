package com.ema.db.repository;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;

@Dao
public interface DeleteAsyncTask<T extends Object> {
    @Delete
    void delete(T obj);
}
