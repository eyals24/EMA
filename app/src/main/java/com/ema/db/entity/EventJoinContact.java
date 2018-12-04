package com.ema.db.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;

import lombok.Data;
import lombok.NoArgsConstructor;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Data
@NoArgsConstructor
@Entity(tableName = "event_join_contact", primaryKeys = {"event_id_fky", "contact_id_fky"},
        foreignKeys = {
                @ForeignKey(entity = Event.class,
                        parentColumns = "id",
                        childColumns = "event_id_fky",
                        onDelete = CASCADE),
                @ForeignKey(entity = Contact.class,
                        parentColumns = "id",
                        childColumns = "contact_id_fky",
                        onDelete = CASCADE)},
        indices = {@Index(value = "event_id_fky"),
                @Index(value = "contact_id_fky")})
public class EventJoinContact {

    @ColumnInfo(name = "event_id_fky")
    private long eventId;

    @ColumnInfo(name = "contact_id_fky")
    private long contactId;
}
