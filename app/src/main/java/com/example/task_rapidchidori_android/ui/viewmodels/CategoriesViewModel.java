package com.example.task_rapidchidori_android.ui.viewmodels;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.task_rapidchidori_android.data.models.CategoryInfo;
import com.example.task_rapidchidori_android.data.repository.CategoryRepo;

import java.util.ArrayList;
import java.util.List;

public class CategoriesViewModel extends ViewModel {

    private final CategoryRepo categoryRepo;

    public CategoriesViewModel(CategoryRepo categoryRepo) {
        this.categoryRepo = categoryRepo;
    }

    public CategoriesViewModel(Application mApplication) {
        categoryRepo = CategoryRepo.getInstance(mApplication.getApplicationContext());
    }

    public void getCategoriesFromRepo() {
        categoryRepo.getAllCategories();
    }

    public MutableLiveData<List<CategoryInfo>> getCategoryLiveData() {
        return categoryRepo.getCategoryLiveData();
    }


    public void removeCategoryFromRepo(String selectedCategory) {
        categoryRepo.removeCategoryFromRepo(selectedCategory);
    }

    public List<String> getCategoryNameList(List<CategoryInfo> categoryInfos) {
        List<String> categoryNames = new ArrayList<>();
        for (CategoryInfo categoryInfo : categoryInfos) {
            categoryNames.add(categoryInfo.category);
        }
        return categoryNames;
    }

}
