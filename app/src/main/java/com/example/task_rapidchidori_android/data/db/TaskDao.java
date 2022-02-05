package com.example.task_rapidchidori_android.data.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.task_rapidchidori_android.data.models.ImagesInfo;
import com.example.task_rapidchidori_android.data.models.SubTaskInfo;
import com.example.task_rapidchidori_android.data.models.TaskInfo;

import java.util.List;

@Dao
public interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTask(TaskInfo task);

    @Query("SELECT * FROM  tasks WHERE category LIKE :category")
    List<TaskInfo> getTasks(String category);

    @Query("DELETE FROM tasks WHERE taskID LIKE :taskId")
    void removeTaskByTaskId(long taskId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertImages(List<ImagesInfo> images);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSubTasks(List<SubTaskInfo> subTasks);

    @Query("SELECT * FROM tasks WHERE taskID LIKE :taskId")
    TaskInfo getTaskByTaskId(long taskId);

    @Query("SELECT * FROM images WHERE taskID LIKE :taskId")
    List<ImagesInfo> getImagesByTaskId(long taskId);

    @Query("SELECT * FROM subTasks WHERE taskID LIKE :taskId")
    List<SubTaskInfo> getSubtasksByTaskId(long taskId);
}
