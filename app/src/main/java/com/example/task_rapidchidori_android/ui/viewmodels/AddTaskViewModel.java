package com.example.task_rapidchidori_android.ui.viewmodels;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.task_rapidchidori_android.data.models.CategoryInfo;
import com.example.task_rapidchidori_android.data.models.TaskInfo;
import com.example.task_rapidchidori_android.data.repository.CategoryRepo;
import com.example.task_rapidchidori_android.data.repository.TaskRepo;

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

    public void saveTaskToRepo(TaskInfo taskInfo) {
        taskRepo.saveTaskToRepo(taskInfo);
    }

    public MutableLiveData<Boolean> getIsSaved() {
        return taskRepo.getIsSaved();
    }

    public void resetIsSaved() {
        taskRepo.getIsSaved().postValue(false);
    }
}
