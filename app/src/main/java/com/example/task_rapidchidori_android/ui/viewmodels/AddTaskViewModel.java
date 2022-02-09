package com.example.task_rapidchidori_android.ui.viewmodels;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.task_rapidchidori_android.data.models.CategoryInfo;
import com.example.task_rapidchidori_android.data.models.ImagesInfo;
import com.example.task_rapidchidori_android.data.models.SubTaskInfo;
import com.example.task_rapidchidori_android.data.models.TaskInfo;
import com.example.task_rapidchidori_android.data.repository.CategoryRepo;
import com.example.task_rapidchidori_android.data.repository.TaskRepo;
import com.example.task_rapidchidori_android.helper.SingleLiveEvent;

import java.util.ArrayList;
import java.util.List;

public class AddTaskViewModel extends ViewModel {
    private final CategoryRepo categoryRepo;
    private final TaskRepo taskRepo;

    public AddTaskViewModel(Application mApplication) {
        categoryRepo = CategoryRepo.getInstance(mApplication.getApplicationContext());
        taskRepo = TaskRepo.getInstance(mApplication.getApplicationContext());
    }

    public void getCategoriesFromRepo() {
        categoryRepo.getAllCategories();
    }

    public MutableLiveData<List<CategoryInfo>> getCategoryLiveData() {
        return categoryRepo.getCategoryLiveData();
    }

    public List<String> getCategoryNameList(List<CategoryInfo> categoryInfos) {
        List<String> categoryNames = new ArrayList<>();
        for (CategoryInfo categoryInfo : categoryInfos) {
            categoryNames.add(categoryInfo.category);
        }
        return categoryNames;
    }

    public void saveTaskToRepo(TaskInfo taskInfo, ArrayList<String> imageSources,
                               ArrayList<SubTaskInfo> subtaskList, boolean isEdit) {
        List<ImagesInfo> imagesInfoList = new ArrayList<>();
        for (String imageSource : imageSources) {
            imagesInfoList.add(new ImagesInfo(taskInfo.taskID, imageSource));
        }
        for (SubTaskInfo subTaskInfo : subtaskList) {
            subTaskInfo.taskId = taskInfo.taskID;
        }
        taskRepo.saveTaskToRepo(taskInfo, imagesInfoList, subtaskList, isEdit);
    }

    public MutableLiveData<Boolean> getIsSaved() {
        return taskRepo.getIsSaved();
    }

    public void resetIsSaved() {
        taskRepo.getIsSaved().postValue(false);
    }

    public void getDataFromRepo(long taskId) {
        taskRepo.getDataByTaskId(taskId);
    }

    public SingleLiveEvent<TaskInfo> getTaskInfo() {
        return taskRepo.getTaskInfoSingleLiveEvent();
    }

    public SingleLiveEvent<List<ImagesInfo>> getImageInfo() {
        return taskRepo.getImagesInfoSingleLiveEvent();
    }

    public SingleLiveEvent<List<SubTaskInfo>> getSubTaskInfo() {
        return taskRepo.getSubtasksInfoSingleLiveEvent();
    }

    public void markTaskComplete(long taskId) {
        taskRepo.markTaskComplete(taskId);
    }

    public MutableLiveData<Boolean> isCompleted() {
        return taskRepo.getIsCompleted();
    }

    public void resetIsCompleted() {
        taskRepo.getIsCompleted().postValue(false);
    }

    public void markSubtaskComplete(SubTaskInfo subTaskInfo) {
        taskRepo.markSubTaskComplete(subTaskInfo.taskId, subTaskInfo.subTaskId);
    }
}
