package com.example.task_rapidchidori_android.data.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.task_rapidchidori_android.data.models.CategoryInfo;
import com.example.task_rapidchidori_android.data.models.ImagesInfo;
import com.example.task_rapidchidori_android.data.models.TaskInfo;
import com.example.task_rapidchidori_android.data.typeconverters.ImageBitmapString;

@Database(entities = {CategoryInfo.class, TaskInfo.class, ImagesInfo.class}, version = 1)
@TypeConverters({ImageBitmapString.class})
public abstract class TaskDB extends RoomDatabase {
    private static volatile TaskDB instance;

    public static synchronized TaskDB getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    TaskDB.class, "task_db")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

    public abstract CategoryDao categoryDao();

    public abstract TaskDao taskDao();
}
