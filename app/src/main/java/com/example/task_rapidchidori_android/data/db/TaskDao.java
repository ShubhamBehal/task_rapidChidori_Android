package com.example.task_rapidchidori_android.data.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.task_rapidchidori_android.data.models.TaskInfo;

import java.util.List;

@Dao
public interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTask(TaskInfo task);

    @Query("SELECT * FROM  tasks WHERE category LIKE :category")
    List<TaskInfo> getTasks(String category);
}
