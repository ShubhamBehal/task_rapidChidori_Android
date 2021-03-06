package com.example.task_rapidchidori_android.data.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "images")
public class ImagesInfo {

    @PrimaryKey(autoGenerate = true)
    public int imageId;

    public long taskId;

    public String image;

    public ImagesInfo(long taskId, @NonNull String image) {
        this.taskId = taskId;
        this.image = image;
    }
}
