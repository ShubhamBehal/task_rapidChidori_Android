package com.example.task_rapidchidori_android.ui.viewmodelfactories;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.task_rapidchidori_android.ui.viewmodels.AddTaskViewModel;
import com.example.task_rapidchidori_android.ui.viewmodels.CategoriesViewModel;

public class CategoriesViewModelFactory implements ViewModelProvider.Factory {

    private final Application mApplication;

    public CategoriesViewModelFactory(Application mApplication) {
        this.mApplication = mApplication;
    }

    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new CategoriesViewModel(mApplication);
    }
}
