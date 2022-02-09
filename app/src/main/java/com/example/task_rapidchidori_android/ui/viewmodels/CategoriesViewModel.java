package com.example.task_rapidchidori_android.ui.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.task_rapidchidori_android.data.models.CategoryInfo;
import com.example.task_rapidchidori_android.data.repository.CategoryRepo;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class CategoriesViewModel extends ViewModel {

    private final CategoryRepo categoryRepo;

    @Inject
    public CategoriesViewModel(CategoryRepo categoryRepo) {
        this.categoryRepo = categoryRepo;
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

    public void getAllCategories() {
        categoryRepo.getAllCategories();
    }
}
