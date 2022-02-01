package com.example.task_rapidchidori_android.data.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


//todo add all the required properties

@Entity(tableName = "tasks")
public class TaskInfo {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    public int taskID;
}
