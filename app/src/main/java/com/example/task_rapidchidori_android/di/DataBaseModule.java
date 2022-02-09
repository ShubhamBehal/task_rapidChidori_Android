package com.example.task_rapidchidori_android.di;

import android.content.Context;

import com.example.task_rapidchidori_android.data.db.TaskDB;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@InstallIn(SingletonComponent.class)
@Module
public class DataBaseModule {

    @Provides
    TaskDB providePropertyDb(@ApplicationContext Context context) {
        return TaskDB.getInstance(context);
    }
}
