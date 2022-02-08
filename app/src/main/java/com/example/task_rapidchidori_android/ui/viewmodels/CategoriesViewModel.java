package com.example.task_rapidchidori_android.ui.viewmodels;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.task_rapidchidori_android.data.models.CategoryInfo;
import com.example.task_rapidchidori_android.data.repository.CategoryRepo;

import java.util.List;

public class CategoriesViewModel extends ViewModel {

    private final CategoryRepo categoryRepo;

    public CategoriesViewModel(Application mApplication) {
        categoryRepo = CategoryRepo.getInstance(mApplication.getApplicationContext());
    }

    public MutableLiveData<List<CategoryInfo>> getCategoryLiveData() {
        return categoryRepo.getCategoryLiveData();
    }

    public void removeCategoryFromRepo(CategoryInfo selectedCategory) {
        categoryRepo.removeCategoryFromRepo(selectedCategory);
    }

    public void editCategoryFromRepo(String oldCategory, String selectedCategory) {
        categoryRepo.editCategoryFromRepo(oldCategory, selectedCategory);
    }
}
