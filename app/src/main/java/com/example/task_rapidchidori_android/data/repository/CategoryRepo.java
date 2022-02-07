package com.example.task_rapidchidori_android.data.repository;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import com.example.task_rapidchidori_android.data.db.TaskDB;
import com.example.task_rapidchidori_android.data.models.CategoryInfo;

import java.util.List;

public class CategoryRepo {

    private static CategoryRepo instance;
    private final TaskDB database;
    private final MutableLiveData<List<CategoryInfo>> categoryLiveData = new MutableLiveData<>();
    private CategoryRepo(TaskDB database) {
        this.database = database;
    }

    public static CategoryRepo getInstance(Context context) {
        if (instance == null) {
            instance = new CategoryRepo(
                    TaskDB.getInstance(context));
        }
        return instance;
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


    public void removeCategoryFromRepo(String selectedCategory) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                super.run();
                database.categoryDao().removeCategory(selectedCategory);
                categoryLiveData.postValue(database.categoryDao().getCategory(selectedCategory));
            }
        };
        thread.start();
    }
}
