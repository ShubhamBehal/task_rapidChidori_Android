package com.example.task_rapidchidori_android.data.repository;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import com.example.task_rapidchidori_android.data.db.NotesDB;
import com.example.task_rapidchidori_android.data.models.CategoryInfo;

import java.util.ArrayList;
import java.util.List;

public class CategoryRepo {

    private static CategoryRepo instance;
    private final NotesDB database;
    private final MutableLiveData<List<CategoryInfo>> categoryLiveData = new MutableLiveData<>();

    private CategoryRepo(NotesDB database) {
        this.database = database;
    }

    public static CategoryRepo getInstance(Context context) {
        if (instance == null) {
            instance = new CategoryRepo(
                    NotesDB.getInstance(context));
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
}
