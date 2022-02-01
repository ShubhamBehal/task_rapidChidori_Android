package com.example.task_rapidchidori_android.data.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.task_rapidchidori_android.data.models.CategoryInfo;
import com.example.task_rapidchidori_android.data.models.TaskInfo;

@Database(entities = {CategoryInfo.class, TaskInfo.class}, version = 1)
public abstract class NotesDB extends RoomDatabase {
    private static volatile NotesDB instance;

    public static synchronized NotesDB getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    NotesDB.class, "notes_db")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

    public abstract CategoryDao categoryDao();
}