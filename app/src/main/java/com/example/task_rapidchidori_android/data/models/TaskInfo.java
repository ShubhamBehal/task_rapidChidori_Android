package com.example.task_rapidchidori_android.data.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tasks")
public class TaskInfo {

    @PrimaryKey(autoGenerate = true)
    public int taskID;

    public String taskTitle;

    public String taskDescription;

    public String category;

    public String dueDate;

    public String dateCreated;

    public String audioURIString;

    public TaskInfo(String taskTitle, String taskDescription,
                    String category, String dueDate, String dateCreated, String audioURIString) {
        this.taskTitle = taskTitle;
        this.taskDescription = taskDescription;
        this.category = category;
        this.dueDate = dueDate;
        this.dateCreated = dateCreated;
        this.audioURIString = audioURIString;
    }
}
