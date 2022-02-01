package com.example.task_rapidchidori_android.ui.viewmodels;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.task_rapidchidori_android.data.models.CategoryInfo;
import com.example.task_rapidchidori_android.data.repository.CategoryRepo;

import java.util.ArrayList;
import java.util.List;

public class MyNotesViewModel extends ViewModel {
    private final CategoryRepo repo;

    public MyNotesViewModel(Application mApplication) {
        repo = CategoryRepo.getInstance(mApplication.getApplicationContext());
    }

    public void addDefaultCategories() {
        List<CategoryInfo> categories = new ArrayList<>();
        CategoryInfo work = new CategoryInfo("Work");
        work.isSelected = true;
        categories.add(work);
        categories.add(new CategoryInfo("School"));
        categories.add(new CategoryInfo("Shopping"));
        categories.add(new CategoryInfo("Groceries"));
        repo.addCategoriesToRepo(categories);
    }

    public void getCategoriesFromRepo() {
        repo.getAllCategories();
    }

    public MutableLiveData<List<CategoryInfo>> getCategoryLiveData() {
        return repo.getCategoryLiveData();
    }

    public void addCategoryToRepo(List<CategoryInfo> category) {
        repo.addCategoriesToRepo(category);
    }

    public void addCategoryToRepo(String category) {
        List<CategoryInfo> categories = new ArrayList<>();
        categories.add(new CategoryInfo(category));
        repo.addCategoriesToRepo(categories);
    }
}
