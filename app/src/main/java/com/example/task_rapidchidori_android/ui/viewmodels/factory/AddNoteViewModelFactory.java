package com.example.task_rapidchidori_android.ui.viewmodels.factory;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.task_rapidchidori_android.ui.viewmodels.AddNoteViewModel;
import com.example.task_rapidchidori_android.ui.viewmodels.MyNotesViewModel;

public class AddNoteViewModelFactory implements ViewModelProvider.Factory {
    private final Application mApplication;


    public AddNoteViewModelFactory(Application application) {
        mApplication = application;
    }


    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new AddNoteViewModel(mApplication);
    }
}