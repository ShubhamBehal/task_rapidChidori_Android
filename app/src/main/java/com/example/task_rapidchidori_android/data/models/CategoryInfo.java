package com.example.task_rapidchidori_android.data.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "categories")
public class CategoryInfo {
    @PrimaryKey
    @NonNull
    public String category;

    public boolean isDefaultCategory;

    public boolean isSelected;

    public CategoryInfo(@NonNull String category, boolean isDefaultCategory) {
        this.category = category;
        this.isDefaultCategory = isDefaultCategory;
    }
}
