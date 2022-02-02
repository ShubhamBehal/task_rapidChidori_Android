package com.example.task_rapidchidori_android.data.repository;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import com.example.task_rapidchidori_android.data.db.TaskDB;
import com.example.task_rapidchidori_android.data.models.TaskInfo;
import com.example.task_rapidchidori_android.helper.SingleLiveEvent;

import java.util.List;

public class TaskRepo {

    private static TaskRepo instance;
    private final TaskDB database;
    private final MutableLiveData<Boolean> isSaved = new MutableLiveData<>();
    private final SingleLiveEvent<List<TaskInfo>> tasksLiveData = new SingleLiveEvent<>();

    private TaskRepo(TaskDB database) {
        this.database = database;
    }

    public static TaskRepo getInstance(Context context) {
        if (instance == null) {
            instance = new TaskRepo(
                    TaskDB.getInstance(context));
        }
        return instance;
    }

    public void saveTaskToRepo(TaskInfo taskInfo) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                super.run();

                database.taskDao().insertTask(taskInfo);

                isSaved.postValue(true);
            }
        };
        thread.start();
    }

    public MutableLiveData<Boolean> getIsSaved() {
        return isSaved;
    }

    public void getTasksFromRepo(String category) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                super.run();

                tasksLiveData.postValue(database.taskDao().getTasks(category));
            }
        };
        thread.start();
    }

    public SingleLiveEvent<List<TaskInfo>> getTasksLiveData() {
        return tasksLiveData;
    }
}
