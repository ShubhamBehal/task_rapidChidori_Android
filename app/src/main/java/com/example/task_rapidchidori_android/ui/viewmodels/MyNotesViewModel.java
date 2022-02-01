package com.example.task_rapidchidori_android.ui.viewmodels;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.task_rapidchidori_android.data.models.Category;
import com.example.task_rapidchidori_android.data.repository.CategoryRepo;

import java.util.List;

public class MyNotesViewModel extends ViewModel {
    private final CategoryRepo repo;
    private final MutableLiveData<List<Category>> categoryLiveData = new MutableLiveData<>();

    public MyNotesViewModel(Application mApplication) {
        repo = CategoryRepo.getInstance(mApplication.getApplicationContext());
    }

    public void addDefaultCategories() {
        repo.addDefaultCategories();
    }

    public void getCategoriesFromRepo() {
        repo.getAllCategories();
    }

    public MutableLiveData<List<Category>> getCategoryLiveData() {
        return repo.getCategoryLiveData();
    }
}
