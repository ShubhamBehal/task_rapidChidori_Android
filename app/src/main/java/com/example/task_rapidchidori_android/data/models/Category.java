package com.example.task_rapidchidori_android.data.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "categories")
public class Category {
    @PrimaryKey
    @NonNull
    public String category;

    public Category(@NonNull String category) {
        this.category = category;
    }
}
