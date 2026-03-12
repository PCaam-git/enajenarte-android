package com.svalero.enajenarte.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.svalero.enajenarte.db.entity.EventEntity;

import java.util.List;

@Dao
public interface EventDao {

    @Query("SELECT * FROM events")
    List<EventEntity> findAll();

    @Query("SELECT * FROM events WHERE id = :id LIMIT 1")
    EventEntity findById(long id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(EventEntity event);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<EventEntity> events);

    @Update
    void update(EventEntity event);

    @Delete
    void delete(EventEntity event);

    @Query("DELETE FROM events")
    void deleteAll();
}