package com.example.task_rapidchidori_android.data.repository;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import com.example.task_rapidchidori_android.data.db.CategoryDB;
import com.example.task_rapidchidori_android.data.models.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoryRepo {

    private static CategoryRepo instance;
    private final CategoryDB database;
    private final MutableLiveData<List<Category>> categoryLiveData = new MutableLiveData<>();

    private CategoryRepo(CategoryDB database) {
        this.database = database;
    }

    public static CategoryRepo getInstance(Context context) {
        if (instance == null) {
            instance = new CategoryRepo(
                    CategoryDB.getInstance(context));
        }
        return instance;
    }

    public void addDefaultCategories() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                super.run();
                List<Category> categories = new ArrayList<>();
                categories.add(new Category("Work"));
                categories.add(new Category("School"));
                categories.add(new Category("Shopping"));
                categories.add(new Category("Groceries"));
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

    public MutableLiveData<List<Category>> getCategoryLiveData() {
        return categoryLiveData;
    }
}
