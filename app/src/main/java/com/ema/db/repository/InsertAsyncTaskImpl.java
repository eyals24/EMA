package com.ema.db.repository;

import android.os.AsyncTask;
import android.util.Log;

import com.ema.db.entity.IdPopulator;

import lombok.AllArgsConstructor;

@AllArgsConstructor
class InsertAsyncTaskImpl<T extends Object> extends AsyncTask<T, Void, Long> {

    private InsertAsyncTask<T> asyncTaskDao;
    private IdPopulator idPopulator;

    @SafeVarargs
    @Override
    protected final Long doInBackground(final T... ts) {
        return asyncTaskDao.insert(ts[0]);
    }

    protected void onPostExecute(Long result) {
        idPopulator.populateId(result);
        Log.d("POPULATE ID", Long.toString(result));
    }
}
