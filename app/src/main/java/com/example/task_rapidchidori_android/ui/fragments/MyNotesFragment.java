package com.example.task_rapidchidori_android.ui.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.task_rapidchidori_android.R;
import com.example.task_rapidchidori_android.data.models.CategoryInfo;
import com.example.task_rapidchidori_android.helper.SharedPrefsUtil;
import com.example.task_rapidchidori_android.ui.adapters.CategoriesListAdapter;
import com.example.task_rapidchidori_android.ui.interfaces.OnCategorySelect;
import com.example.task_rapidchidori_android.ui.viewmodelfactories.MyNotesViewModelFactory;
import com.example.task_rapidchidori_android.ui.viewmodels.MyNotesViewModel;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MyNotesFragment extends Fragment implements View.OnClickListener, OnCategorySelect {

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
    private List<CategoryInfo> categories;

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

        categoriesListAdapter = new CategoriesListAdapter(new ArrayList<>(), this);
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

    private void showCategoryList(List<CategoryInfo> categories) {
        this.categories = categories;
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
            showPopup();
        }
    }

    private void showPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = (requireActivity()).getLayoutInflater();
        builder.setTitle(R.string.add_category_title);
        builder.setCancelable(false);
        View view = inflater.inflate(R.layout.add_category_dialog_view, null);
        EditText etCategory = view.findViewById(R.id.et_category);
        builder.setView(view)
                .setPositiveButton(R.string.ok, (dialog, id) -> {
                    if (TextUtils.isEmpty(etCategory.getText())) {
                        Toast.makeText(requireActivity(),
                                getString(R.string.category_name_empty_error),
                                Toast.LENGTH_SHORT).show();
                    } else {
                        viewModel.addCategoryToRepo(etCategory.getText().toString());
                        Toast.makeText(requireActivity(),
                                getString(R.string.category_added_success_msg),
                                Toast.LENGTH_SHORT).show();
                    }
                });
        builder.create();
        builder.show();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
        if(menu instanceof MenuBuilder){
            MenuBuilder m = (MenuBuilder) menu;
            m.setOptionalIconsVisible(true);
        }
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

    @Override
    public void onCategorySelect(String category) {
        for (CategoryInfo c : categories) {
            c.isSelected = c.category.equalsIgnoreCase(category);
        }
        categoriesListAdapter.setData(categories);
    }
}

