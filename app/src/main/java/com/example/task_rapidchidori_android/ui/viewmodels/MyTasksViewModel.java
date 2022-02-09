package com.example.task_rapidchidori_android.ui.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.task_rapidchidori_android.data.models.CategoryInfo;
import com.example.task_rapidchidori_android.data.models.TaskInfo;
import com.example.task_rapidchidori_android.data.repository.CategoryRepo;
import com.example.task_rapidchidori_android.data.repository.TaskRepo;
import com.example.task_rapidchidori_android.helper.SingleLiveEvent;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class MyTasksViewModel extends ViewModel {
    private final CategoryRepo categoryRepo;
    private final TaskRepo taskRepo;

    @Inject
    public MyTasksViewModel(TaskRepo taskRepo, CategoryRepo categoryRepo) {
        this.taskRepo = taskRepo;
        this.categoryRepo = categoryRepo;
    }

    public void addDefaultCategories() {
        List<CategoryInfo> categories = new ArrayList<>();
        CategoryInfo work = new CategoryInfo("Work", true);
        work.isSelected = true;
        categories.add(work);
        categories.add(new CategoryInfo("School", true));
        categories.add(new CategoryInfo("Shopping", true));
        categories.add(new CategoryInfo("Groceries", true));
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
        categories.add(new CategoryInfo(category, false));
        categoryRepo.addCategoriesToRepo(categories);
    }

    public void getTasksFromRepo(String category) {
        taskRepo.getTasksFromRepo(category);
    }

    public SingleLiveEvent<List<TaskInfo>> getTasksLiveData() {
        return taskRepo.getTasksLiveData();
    }

    public void removeTaskFromRepo(long taskId, String selectedCategory) {
        taskRepo.removeTaskFromRepo(taskId, selectedCategory);
    }
}
