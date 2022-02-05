package com.example.task_rapidchidori_android.data.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "subTasks")
public class SubTaskInfo {

    @PrimaryKey(autoGenerate = true)
    public int subTaskId;

    public int taskId;

    public String subTaskTitle;

    public SubTaskInfo(int taskId, String subTaskTitle) {
        this.taskId = taskId;
        this.subTaskTitle = subTaskTitle;
    }
}
