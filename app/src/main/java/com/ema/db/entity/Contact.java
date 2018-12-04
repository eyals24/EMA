package com.ema.db.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.Observable;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity(tableName = "contacts")
public class Contact extends Observable implements IdPopulator {

    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "contact_first_name")
    @NonNull
    private String contactFirstName;

    @ColumnInfo(name = "contact_last_name")
    private String contactLastName;

    @ColumnInfo(name = "contact_phone")
    private String contactPhone;

    @ColumnInfo(name = "contact_email")
    private String contactEmail;

    @Override
    public void populateId(Long id) {
        this.id = id;
        setChanged();
        notifyObservers();
    }
}
