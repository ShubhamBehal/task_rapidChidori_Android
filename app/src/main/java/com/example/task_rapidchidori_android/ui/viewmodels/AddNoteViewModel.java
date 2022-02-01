package com.example.task_rapidchidori_android.ui.viewmodels;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.task_rapidchidori_android.data.models.CategoryInfo;
import com.example.task_rapidchidori_android.data.repository.CategoryRepo;

import java.util.ArrayList;
import java.util.List;

public class AddNoteViewModel extends ViewModel {
    private final CategoryRepo repo;

    public AddNoteViewModel(Application mApplication) {
        repo = CategoryRepo.getInstance(mApplication.getApplicationContext());
    }

    public void getCategoriesFromRepo() {
        repo.getAllCategories();
    }

    public MutableLiveData<List<CategoryInfo>> getCategoryLiveData() {
        return repo.getCategoryLiveData();
    }

    public List<String> getCategoryNameList(List<CategoryInfo> categoryInfos) {
        List<String> categoryNames = new ArrayList<>();
        for (CategoryInfo categoryInfo : categoryInfos) {
            categoryNames.add(categoryInfo.category);
        }
        return categoryNames;
    }
}
