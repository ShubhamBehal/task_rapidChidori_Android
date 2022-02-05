package com.example.task_rapidchidori_android.data.repository;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import com.example.task_rapidchidori_android.data.db.TaskDB;
import com.example.task_rapidchidori_android.data.models.ImagesInfo;
import com.example.task_rapidchidori_android.data.models.SubTaskInfo;
import com.example.task_rapidchidori_android.data.models.TaskInfo;
import com.example.task_rapidchidori_android.helper.SingleLiveEvent;

import java.util.List;

public class TaskRepo {

    private static TaskRepo instance;
    private final TaskDB database;
    private final MutableLiveData<Boolean> isSaved = new MutableLiveData<>();
    private final SingleLiveEvent<List<TaskInfo>> tasksLiveData = new SingleLiveEvent<>();
    private final SingleLiveEvent<TaskInfo> taskInfoSingleLiveEvent = new SingleLiveEvent<>();
    private final SingleLiveEvent<List<ImagesInfo>> imagesInfoSingleLiveEvent = new SingleLiveEvent<>();
    private final SingleLiveEvent<List<SubTaskInfo>> subtasksInfoSingleLiveEvent = new SingleLiveEvent<>();

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

    public void saveTaskToRepo(TaskInfo taskInfo, List<ImagesInfo> imageSources, List<SubTaskInfo> subTasks, boolean isEdit) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                super.run();

                if (isEdit) {
                    database.taskDao().updateTask(taskInfo.taskID, taskInfo.taskTitle, taskInfo.taskDescription,
                            taskInfo.category, taskInfo.dueDate, taskInfo.audioURIString);
                    database.taskDao().deleteImagesOfTask(taskInfo.taskID);
                    database.taskDao().deleteSubtasksOfTask(taskInfo.taskID);
                } else {
                    database.taskDao().insertTask(taskInfo);
                }

                database.taskDao().insertImages(imageSources);
                database.taskDao().insertSubTasks(subTasks);

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

    public void removeTaskFromRepo(long taskId, String selectedCategory) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                super.run();
                database.taskDao().removeTaskByTaskId(taskId);

                tasksLiveData.postValue(database.taskDao().getTasks(selectedCategory));
            }
        };
        thread.start();
    }

    public void getDataByTaskId(long taskId) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                super.run();
                taskInfoSingleLiveEvent.postValue(database.taskDao().getTaskByTaskId(taskId));
                imagesInfoSingleLiveEvent.postValue(database.taskDao().getImagesByTaskId(taskId));
                subtasksInfoSingleLiveEvent.postValue(database.taskDao().getSubtasksByTaskId(taskId));
            }
        };
        thread.start();
    }

    public SingleLiveEvent<TaskInfo> getTaskInfoSingleLiveEvent() {
        return taskInfoSingleLiveEvent;
    }

    public SingleLiveEvent<List<ImagesInfo>> getImagesInfoSingleLiveEvent() {
        return imagesInfoSingleLiveEvent;
    }

    public SingleLiveEvent<List<SubTaskInfo>> getSubtasksInfoSingleLiveEvent() {
        return subtasksInfoSingleLiveEvent;
    }
}
