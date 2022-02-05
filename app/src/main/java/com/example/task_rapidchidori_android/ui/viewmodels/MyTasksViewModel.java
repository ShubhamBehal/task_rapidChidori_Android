package com.example.task_rapidchidori_android.ui.viewmodels;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.task_rapidchidori_android.data.models.CategoryInfo;
import com.example.task_rapidchidori_android.data.models.TaskInfo;
import com.example.task_rapidchidori_android.data.repository.CategoryRepo;
import com.example.task_rapidchidori_android.data.repository.TaskRepo;
import com.example.task_rapidchidori_android.helper.SingleLiveEvent;

import java.util.ArrayList;
import java.util.List;

public class MyTasksViewModel extends ViewModel {
    private final CategoryRepo categoryRepo;
    private final TaskRepo taskRepo;

    public MyTasksViewModel(Application mApplication) {
        categoryRepo = CategoryRepo.getInstance(mApplication.getApplicationContext());
        taskRepo = TaskRepo.getInstance(mApplication.getApplicationContext());
    }

    public void addDefaultCategories() {
        List<CategoryInfo> categories = new ArrayList<>();
        CategoryInfo work = new CategoryInfo("Work");
        work.isSelected = true;
        categories.add(work);
        categories.add(new CategoryInfo("School"));
        categories.add(new CategoryInfo("Shopping"));
        categories.add(new CategoryInfo("Groceries"));
        categoryRepo.addCategoriesToRepo(categories);
    }

    public void getCategoriesFromRepo() {
        categoryRepo.getAllCategories();
    }

    public MutableLiveData<List<CategoryInfo>> getCategoryLiveData() {
        return categoryRepo.getCategoryLiveData();
    }

    public void addCategoryToRepo(List<CategoryInfo> category) {
        categoryRepo.addCategoriesToRepo(category);
    }

    public void addCategoryToRepo(String category) {
        List<CategoryInfo> categories = new ArrayList<>();
        categories.add(new CategoryInfo(category));
        categoryRepo.addCategoriesToRepo(categories);
    }

    public void getTasksFromRepo(String category) {
        taskRepo.getTasksFromRepo(category);
    }

    public SingleLiveEvent<List<TaskInfo>> getTasksLiveData() {
        return taskRepo.getTasksLiveData();
    }

    public void removeTaskFromRepo(int taskId, String selectedCategory) {
        taskRepo.removeTaskFromRepo(taskId, selectedCategory);
    }
}
