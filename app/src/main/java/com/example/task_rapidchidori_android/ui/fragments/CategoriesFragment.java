package com.example.task_rapidchidori_android.ui.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.task_rapidchidori_android.R;
import com.example.task_rapidchidori_android.data.models.CategoryInfo;
import com.example.task_rapidchidori_android.ui.adapters.EditCategoriesListAdapter;
import com.example.task_rapidchidori_android.ui.interfaces.OnCategoriesEditDeleteListener;
import com.example.task_rapidchidori_android.ui.viewmodelfactories.CategoriesViewModelFactory;
import com.example.task_rapidchidori_android.ui.viewmodels.CategoriesViewModel;

import java.util.ArrayList;
import java.util.List;


public class CategoriesFragment extends Fragment implements OnCategoriesEditDeleteListener {

    private List<CategoryInfo> categories;
    private EditCategoriesListAdapter adapter;
    private RecyclerView rvEditCategory;
    public CategoriesViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_categories, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        configViews();
        setUpObservers();
        setAdapter();
    }


    private void initViews(View view) {
        rvEditCategory = view.findViewById(R.id.rvEditCategory);
    }

    private void configViews() {
        viewModel = new ViewModelProvider(this,
                new CategoriesViewModelFactory(requireActivity().getApplication()))
                .get(CategoriesViewModel.class);

    }

    private void setUpObservers() {
        viewModel.getCategoryLiveData().observe(getViewLifecycleOwner(), this::showCategoryList);
    }

    private void setAdapter() {
        if (categories == null) {
            categories = new ArrayList<>();
        }
        adapter = new EditCategoriesListAdapter(categories, this);
        rvEditCategory.setLayoutManager(new LinearLayoutManager(requireActivity(),
                LinearLayoutManager.VERTICAL, false));
        rvEditCategory.setAdapter(adapter);
    }


    private void showCategoryList(List<CategoryInfo> categories) {
        this.categories = categories;
        adapter.setData(categories);
    }

    @Override
    public void onCategoryDelete(CategoryInfo category) {
        showWarningDialog(category);
    }

    private void showWarningDialog(CategoryInfo category) {
        new AlertDialog.Builder(requireActivity())
                .setTitle(R.string.delete_cat_head)
                .setMessage(R.string.delete_cat_desc)
                .setPositiveButton(R.string.yes, (dialog, which) ->
                        viewModel.removeCategoryFromRepo(category))
                .setNegativeButton(R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}


