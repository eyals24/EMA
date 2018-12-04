package com.ema.db.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import com.ema.db.converter.DateConverter;

import java.util.Date;
import java.util.Observable;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity(tableName = "events")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Event extends Observable implements IdPopulator {

    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "event_name")
    @NonNull
    private String eventName;

    @ColumnInfo(name = "event_place")
    private String eventPlace;

    @ColumnInfo(name = "event_budget")
    private int eventBudget;

    @ColumnInfo(name = "event_date")
    @TypeConverters({DateConverter.class})
    private Date eventDate;

    @Override
    public void populateId(Long id) {
        this.id = id;
        setChanged();
        notifyObservers();
    }
}