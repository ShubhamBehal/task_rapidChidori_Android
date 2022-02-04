package com.example.task_rapidchidori_android.ui.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.task_rapidchidori_android.R;
import com.example.task_rapidchidori_android.ui.interfaces.OnCategorySelect;


public class EditCategories extends Fragment implements View.OnClickListener, OnCategorySelect {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_categories, container, false);
    }

    @Override
    public void onClick(View v) {


    }

    @Override
    public void onCategorySelect(String category) {

    }
}