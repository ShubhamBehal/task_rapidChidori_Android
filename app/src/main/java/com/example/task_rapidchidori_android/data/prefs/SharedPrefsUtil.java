package com.example.task_rapidchidori_android.data.prefs;

import static com.example.task_rapidchidori_android.helper.Constants.IS_FIRST_LAUNCH;
import static com.example.task_rapidchidori_android.helper.Constants.PREFS_NAME;

import android.content.Context;

import androidx.annotation.NonNull;

public class SharedPrefsUtil {
    private static SharedPrefsUtil instance;


    private SharedPrefsUtil() {
    }

    public synchronized static SharedPrefsUtil getInstance() {
        if (instance == null) {
            instance = new SharedPrefsUtil();
        }
        return instance;
    }

    public boolean isAlreadyLaunched(@NonNull Context context) {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .getBoolean(IS_FIRST_LAUNCH, false);
    }

    public void setAlreadyLaunched(@NonNull Context context, boolean value) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .edit().putBoolean(IS_FIRST_LAUNCH, value).apply();
    }
}