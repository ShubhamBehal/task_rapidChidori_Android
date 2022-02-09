package com.example.task_rapidchidori_android.data.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "subTasks")
public class SubTaskInfo {

    @PrimaryKey(autoGenerate = true)
    public int subTaskId;

    public long taskId;

    public String subTaskTitle;

    public boolean isComplete;

    public SubTaskInfo(long taskId, String subTaskTitle, boolean isComplete) {
        this.taskId = taskId;
        this.subTaskTitle = subTaskTitle;
        this.isComplete = isComplete;
    }
}
