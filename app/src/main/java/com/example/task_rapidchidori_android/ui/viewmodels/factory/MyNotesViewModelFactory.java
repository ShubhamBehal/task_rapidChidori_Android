package com.example.task_rapidchidori_android.ui.viewmodels.factory;

import android.app.Application;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.task_rapidchidori_android.ui.viewmodels.MyNotesViewModel;

public class MyNotesViewModelFactory implements ViewModelProvider.Factory {
    private Application mApplication;
    private String mParam;


    public MyNotesViewModelFactory(Application application) {
        mApplication = application;
    }


    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new MyNotesViewModel(mApplication);
    }
}