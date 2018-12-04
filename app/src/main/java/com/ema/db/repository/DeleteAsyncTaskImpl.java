package com.ema.db.repository;

import android.os.AsyncTask;

import lombok.AllArgsConstructor;

@AllArgsConstructor
class DeleteAsyncTaskImpl<T extends Object> extends AsyncTask<T, Void, Void> {

    private DeleteAsyncTask<T> asyncTaskDao;

    @SafeVarargs
    @Override
    protected final Void doInBackground(T... ts) {
        asyncTaskDao.delete(ts[0]);
        return null;
    }
}
