package com.example.task_rapidchidori_android.ui.viewmodels;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.task_rapidchidori_android.data.models.CategoryInfo;
import com.example.task_rapidchidori_android.data.repository.CategoryRepo;

import java.util.List;

public class MyNotesViewModel extends ViewModel {
    private final CategoryRepo repo;

    public MyNotesViewModel(Application mApplication) {
        repo = CategoryRepo.getInstance(mApplication.getApplicationContext());
    }

    public void addDefaultCategories() {
        repo.addDefaultCategories();
    }

    public void getCategoriesFromRepo() {
        repo.getAllCategories();
    }

    public MutableLiveData<List<CategoryInfo>> getCategoryLiveData() {
        return repo.getCategoryLiveData();
    }

    public void addCategoryToRepo(String category) {
        repo.addCategoryToRepo(category);
    }
}
