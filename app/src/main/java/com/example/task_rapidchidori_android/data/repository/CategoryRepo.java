package com.example.task_rapidchidori_android.data.repository;

import static com.example.task_rapidchidori_android.helper.Constants.DEFAULT_CATEGORY;

import androidx.lifecycle.MutableLiveData;

import com.example.task_rapidchidori_android.data.db.TaskDB;
import com.example.task_rapidchidori_android.data.models.CategoryInfo;

import java.util.List;

import javax.inject.Inject;

public class CategoryRepo {
    private final TaskDB database;
    private final MutableLiveData<List<CategoryInfo>> categoryLiveData = new MutableLiveData<>();

    @Inject
    CategoryRepo(TaskDB database) {
        this.database = database;
    }

    public void addCategoriesToRepo(List<CategoryInfo> categories) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                super.run();

                database.categoryDao().insertAll(categories);

                categoryLiveData.postValue(database.categoryDao().getAllCategories());
            }
        };
        thread.start();
    }


    public void getAllCategories() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                super.run();
                categoryLiveData.postValue(database.categoryDao().getAllCategories());
            }
        };
        thread.start();
    }


    public MutableLiveData<List<CategoryInfo>> getCategoryLiveData() {
        return categoryLiveData;
    }


    public void removeCategoryFromRepo(CategoryInfo selectedCategory) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                super.run();
                database.categoryDao().removeCategory(selectedCategory.category);
                database.taskDao().removeTaskByCategories(selectedCategory.category);


                if (selectedCategory.isSelected) {
                    database.taskDao().upDateSelectedCategory(DEFAULT_CATEGORY);
                }
                categoryLiveData.postValue(database.categoryDao().getAllCategories());
            }
        };
        thread.start();
    }

    public void editCategoryFromRepo(String oldCategory, String selectedCategory) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                super.run();
                database.categoryDao().editCategory(oldCategory, selectedCategory);
                database.taskDao().updateCategoryName(oldCategory, selectedCategory);
                categoryLiveData.postValue(database.categoryDao().getAllCategories());
            }

        };
        thread.start();
    }
}



