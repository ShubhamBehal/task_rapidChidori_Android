package com.example.task_rapidchidori_android.data.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.task_rapidchidori_android.data.models.CategoryInfo;

import java.util.List;

@Dao
public interface CategoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<CategoryInfo> categories);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(CategoryInfo categoryInfo);

    @Query("SELECT * FROM  categories")
    List<CategoryInfo> getAllCategories();
}
