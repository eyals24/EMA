package com.ema.db.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.ema.db.converter.DateConverter;

import java.util.Date;
import java.util.Observable;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Data
@NoArgsConstructor
@Entity(tableName = "missions", foreignKeys = @ForeignKey(entity = Event.class,
        parentColumns = "id", childColumns = "event_id_fky", onDelete = CASCADE),
        indices = @Index(value = "event_id_fky"))
@EqualsAndHashCode(callSuper = true)
public class Mission extends Observable implements IdPopulator {

    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "event_id_fky")
    private long eventId;

    @ColumnInfo(name = "mission_name")
    @lombok.NonNull
    private String missionName;

    @ColumnInfo(name = "mission_place")
    private String missionPlace;

    @ColumnInfo(name = "mission_start_date")
    @TypeConverters({DateConverter.class})
    private Date missionStartDate;

    @ColumnInfo(name = "mission_end_date")
    @TypeConverters({DateConverter.class})
    private Date missionEndDate;

    @ColumnInfo(name = "mission_description")
    private String missionDescription;

    @Override
    public void populateId(Long id) {
        this.id = id;
        setChanged();
        notifyObservers();
    }

}
