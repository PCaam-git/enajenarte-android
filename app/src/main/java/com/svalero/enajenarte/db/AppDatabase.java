package com.svalero.enajenarte.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.svalero.enajenarte.db.dao.WorkshopDao;
import com.svalero.enajenarte.db.entity.WorkshopEntity;

@Database(entities = {WorkshopEntity.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract WorkshopDao workshopDao();
}