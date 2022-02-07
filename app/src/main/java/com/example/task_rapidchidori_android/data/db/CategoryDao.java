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


    @Query("SELECT * FROM  categories WHERE category LIKE :category")
    List<CategoryInfo> getCategory(String category);

    @Query("SELECT * FROM  categories")
    List<CategoryInfo> getAllCategories();


    @Query("DELETE FROM categories WHERE category LIKE :category")
    void removeCategoryByCategoryID(String category);

}
