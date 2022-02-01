package com.example.task_rapidchidori_android.ui.viewmodelfactories;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.task_rapidchidori_android.ui.viewmodels.MyNotesViewModel;

public class MyNotesViewModelFactory implements ViewModelProvider.Factory {
    private final Application mApplication;


    public MyNotesViewModelFactory(Application application) {
        mApplication = application;
    }


    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new MyNotesViewModel(mApplication);
    }
}