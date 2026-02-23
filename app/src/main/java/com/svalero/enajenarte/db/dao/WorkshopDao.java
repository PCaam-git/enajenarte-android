package com.svalero.enajenarte.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.OnConflictStrategy;
import androidx.room.Delete;
import androidx.room.Update;

import com.svalero.enajenarte.db.entity.WorkshopEntity;

import java.util.List;

@Dao
public interface WorkshopDao {

    // Permite que la base local esté como la api
    @Query("SELECT * FROM workshops")
    List<WorkshopEntity> findAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(WorkshopEntity workshop);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<WorkshopEntity> workshops);

    @Update
    void update(WorkshopEntity workshop);

    @Delete
    void delete(WorkshopEntity workshop);

    // Permite que la base local esté como la api
    @Query("DELETE FROM workshops")
    void deleteAll();
}