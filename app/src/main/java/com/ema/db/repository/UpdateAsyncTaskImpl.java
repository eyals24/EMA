package com.ema.db.repository;

import android.os.AsyncTask;

import lombok.AllArgsConstructor;

@AllArgsConstructor
class UpdateAsyncTaskImpl<T extends Object> extends AsyncTask<T, Void, Void> {

    private UpdateAsyncTask<T> asyncTaskDao;

    @SafeVarargs
    @Override
    protected final Void doInBackground(T... ts) {
        asyncTaskDao.update(ts[0]);
        return null;
    }
}
