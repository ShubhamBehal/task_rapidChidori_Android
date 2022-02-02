package com.example.task_rapidchidori_android.data.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.task_rapidchidori_android.data.models.ImagesInfo;
import com.example.task_rapidchidori_android.data.models.TaskInfo;

import java.util.List;

@Dao
public interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTask(TaskInfo task);

    @Query("SELECT * FROM  tasks WHERE category LIKE :category")
    List<TaskInfo> getTasks(String category);

    @Query("DELETE FROM tasks WHERE taskID LIKE :taskId")
    void removeTaskByTaskId(int taskId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertImages(List<ImagesInfo> images);

}
