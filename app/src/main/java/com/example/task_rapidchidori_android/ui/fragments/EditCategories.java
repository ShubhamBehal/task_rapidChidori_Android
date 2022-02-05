package com.example.task_rapidchidori_android.ui.fragments;

import static com.example.task_rapidchidori_android.helper.Constants.DEFAULT_CATEGORY;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.task_rapidchidori_android.R;
import com.example.task_rapidchidori_android.data.models.CategoryInfo;
import com.example.task_rapidchidori_android.helper.SharedPrefsUtil;
import com.example.task_rapidchidori_android.ui.adapters.CategoriesListAdapter;
import com.example.task_rapidchidori_android.ui.adapters.EditCategoriesListAdapter;
import com.example.task_rapidchidori_android.ui.interfaces.OnCategorySelect;
import com.example.task_rapidchidori_android.ui.viewmodelfactories.AddTaskViewModelFactory;
import com.example.task_rapidchidori_android.ui.viewmodelfactories.CategoriesViewModelFactory;
import com.example.task_rapidchidori_android.ui.viewmodels.AddTaskViewModel;
import com.example.task_rapidchidori_android.ui.viewmodels.CategoriesViewModel;

import java.util.ArrayList;
import java.util.List;


public class EditCategories extends Fragment implements View.OnClickListener, OnCategorySelect {

    private List<CategoryInfo> categories;
    private EditCategoriesListAdapter ecla;
    RecyclerView rvEditCategory;
    public CategoriesViewModel cviewModel;
    private String selectedCategory = DEFAULT_CATEGORY;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_categories, container, false);

    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPrefsUtil.getInstance().setAlreadyLaunched(requireActivity(), true);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvEditCategory = view.findViewById(R.id.rvEditCategory);

        cviewModel= new ViewModelProvider(this,
                new CategoriesViewModelFactory(requireActivity().getApplication()))
                .get(CategoriesViewModel.class);
        setAdapter();
        cviewModel.getCategoryLiveData().observe(getViewLifecycleOwner(), this::showCategoryList);
        cviewModel.getCategoriesFromRepo();

    }

    @Override
    public void onClick(View v) {


    }
    @Override
    public void onCategorySelect(String category) {
     //   for (CategoryInfo c : categories) {
   //         c.isSelected = c.category.equalsIgnoreCase(category);
     //   }
    //    EditCategoriesListAdapter.setData(categories);
   //     cviewModel.addCategoryToRepo(categories);

     //   this.selectedCategory = category;
     //  cviewModel.getTasksFromRepo(category);



    }

    private void setAdapter(){
        if (categories == null)
        {
            categories = new ArrayList<>();
        }
         ecla  = new EditCategoriesListAdapter(categories,this);
        rvEditCategory.setLayoutManager(new LinearLayoutManager(requireActivity(),
                LinearLayoutManager.VERTICAL, false));
            rvEditCategory.setAdapter(ecla);
        }


    private void showCategoryList(List<CategoryInfo> categories) {
        this.categories = categories;
        ecla.setData(categories);
    }

    }


