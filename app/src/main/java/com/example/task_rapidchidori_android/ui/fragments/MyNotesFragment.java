package com.example.task_rapidchidori_android.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.task_rapidchidori_android.R;
import com.example.task_rapidchidori_android.data.models.Category;
import com.example.task_rapidchidori_android.helper.SharedPrefsUtil;
import com.example.task_rapidchidori_android.ui.adapters.CategoriesListAdapter;
import com.example.task_rapidchidori_android.ui.viewmodels.MyNotesViewModel;
import com.example.task_rapidchidori_android.ui.viewmodels.factory.MyNotesViewModelFactory;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MyNotesFragment extends Fragment implements View.OnClickListener {

    private FloatingActionButton fabAdd;
    private ExtendedFloatingActionButton fabAddNotes;
    private ExtendedFloatingActionButton fabAddCategories;
    private boolean isFabOpen;
    private Animation animOpen;
    private Animation animClose;
    private Animation animForward;
    private Animation animBackward;
    private MyNotesViewModel viewModel;
    private RecyclerView rvCategories;
    private CategoriesListAdapter categoriesListAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_notes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        configViews();
        setUpListeners();

        //adding default categories only the first time app is launched
        if (!SharedPrefsUtil.getInstance().isAlreadyLaunched(requireActivity())) {
            viewModel.addDefaultCategories();
        }

        //getting all categories name saved in room db
        viewModel.getCategoriesFromRepo();

    }

    private void initViews(View view) {
        fabAdd = view.findViewById(R.id.fab_add);
        fabAddNotes = view.findViewById(R.id.fab_add_note);
        fabAddCategories = view.findViewById(R.id.fab_add_category);
        rvCategories = view.findViewById(R.id.rv_categories);
    }

    private void configViews() {
        viewModel = new ViewModelProvider(this,
                new MyNotesViewModelFactory(requireActivity().getApplication()))
                .get(MyNotesViewModel.class);

        animOpen = AnimationUtils.loadAnimation(getActivity(), R.anim.fab_open);
        animClose = AnimationUtils.loadAnimation(getActivity(), R.anim.fab_close);
        animForward = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate_forward);
        animBackward = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate_backward);

        categoriesListAdapter = new CategoriesListAdapter(new ArrayList<>());
        rvCategories.setLayoutManager(new LinearLayoutManager(requireActivity(),
                LinearLayoutManager.HORIZONTAL, false));
        rvCategories.setAdapter(categoriesListAdapter);
    }

    private void setUpListeners() {
        fabAdd.setOnClickListener(this);
        fabAddNotes.setOnClickListener(this);
        fabAddCategories.setOnClickListener(this);

        //live data observer for categories name
        viewModel.getCategoryLiveData().observe(getViewLifecycleOwner(), this::showCategoryList);
    }

    private void showCategoryList(List<Category> categories) {
        categoriesListAdapter.setData(categories);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.fab_add) {
            animateFAB();
        } else if (view.getId() == R.id.fab_add_note && getActivity() != null) {
            Navigation.findNavController(getActivity(), R.id.nav_host_fragment)
                    .navigate(R.id.action_myNotesFragment_to_addNoteFragment);
        } else if (view.getId() == R.id.fab_add_category) {
            //todo handle on category add click
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    public void animateFAB() {
        if (isFabOpen) {
            fabAdd.startAnimation(animBackward);
            fabAddNotes.startAnimation(animClose);
            fabAddCategories.startAnimation(animClose);
            fabAddNotes.setClickable(false);
            fabAddCategories.setClickable(false);
            isFabOpen = false;
        } else {
            fabAdd.startAnimation(animForward);
            fabAddNotes.startAnimation(animOpen);
            fabAddCategories.startAnimation(animOpen);
            fabAddNotes.setClickable(true);
            fabAddCategories.setClickable(true);
            isFabOpen = true;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPrefsUtil.getInstance().setAlreadyLaunched(requireActivity(), true);
    }
}

