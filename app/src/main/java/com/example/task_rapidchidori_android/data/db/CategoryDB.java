package com.example.task_rapidchidori_android.data.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.task_rapidchidori_android.data.models.Category;

@Database(entities = {Category.class}, version = 1)
public abstract class CategoryDB extends RoomDatabase {
    private static volatile CategoryDB instance;

    public static synchronized CategoryDB getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    CategoryDB.class, "news_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

    public abstract CategoryDao categoryDao();
}
