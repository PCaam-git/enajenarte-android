package com.svalero.enajenarte.db;

import android.content.Context;

import androidx.room.Room;

public class DatabaseUtil {

    private static AppDatabase db;

    public static AppDatabase getDb(Context context) {
        if (db == null) {
            db = Room.databaseBuilder(
                            context,
                            AppDatabase.class,
                            "enajenarte_db"
                    )
                    .allowMainThreadQueries()
                    // Si cambiar la versión de la bbdd y no hay migración, la borra y recrea
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return db;
    }
}