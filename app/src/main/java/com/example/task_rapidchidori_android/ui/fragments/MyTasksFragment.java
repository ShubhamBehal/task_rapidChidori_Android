package com.example.task_rapidchidori_android.ui.fragments;

import static com.example.task_rapidchidori_android.helper.Constants.DEFAULT_CATEGORY;
import static com.example.task_rapidchidori_android.helper.Constants.TASK_ID;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.task_rapidchidori_android.R;
import com.example.task_rapidchidori_android.data.models.CategoryInfo;
import com.example.task_rapidchidori_android.data.models.TaskInfo;
import com.example.task_rapidchidori_android.helper.SharedPrefsUtil;
import com.example.task_rapidchidori_android.ui.adapters.CategoriesListAdapter;
import com.example.task_rapidchidori_android.ui.adapters.TaskListAdapter;
import com.example.task_rapidchidori_android.ui.interfaces.OnCategorySelect;
import com.example.task_rapidchidori_android.ui.interfaces.TaskItemClickListener;
import com.example.task_rapidchidori_android.ui.viewmodelfactories.MyTasksViewModelFactory;
import com.example.task_rapidchidori_android.ui.viewmodels.MyTasksViewModel;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MyTasksFragment extends Fragment implements View.OnClickListener, OnCategorySelect,
        TaskItemClickListener {

    private FloatingActionButton fabAdd;
    private ExtendedFloatingActionButton fabAddTask;
    private ExtendedFloatingActionButton fabAddCategories;
    private boolean isFabOpen;
    private Animation animOpen;
    private Animation animClose;
    private Animation animForward;
    private Animation animBackward;
    private MyTasksViewModel viewModel;
    private RecyclerView rvCategories;
    private RecyclerView rvTasks;
    private CategoriesListAdapter categoriesListAdapter;
    private TaskListAdapter taskListAdapter;
    private List<CategoryInfo> categories;
    private List<TaskInfo> tasks;
    private String selectedCategory = DEFAULT_CATEGORY;
    private TextView tvNoTasks;
    private SearchView svSearch;
    private boolean isSortByName;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_tasks, container, false);
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

        //getting all saved tasks from repository of a particular repository
        for (CategoryInfo category : categories) {
            if (category.isSelected) {
                selectedCategory = category.category;
            }
        }
        viewModel.getTasksFromRepo(selectedCategory);
    }

    private void initViews(View view) {
        fabAdd = view.findViewById(R.id.fab_add);
        fabAddTask = view.findViewById(R.id.fab_add_task);
        fabAddCategories = view.findViewById(R.id.fab_add_category);
        rvCategories = view.findViewById(R.id.rv_categories);
        rvTasks = view.findViewById(R.id.rv_tasks);
        tvNoTasks = view.findViewById(R.id.tv_no_tasks);
        svSearch = view.findViewById(R.id.sv_search);
    }

    private void configViews() {
        viewModel = new ViewModelProvider(this,
                new MyTasksViewModelFactory(requireActivity().getApplication()))
                .get(MyTasksViewModel.class);

        animOpen = AnimationUtils.loadAnimation(getActivity(), R.anim.fab_open);
        animClose = AnimationUtils.loadAnimation(getActivity(), R.anim.fab_close);
        animForward = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate_forward);
        animBackward = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate_backward);

        setUpCategoriesAdapter();
        setUpTasksAdapter();
    }

    private void setUpTasksAdapter() {
        if (tasks == null) {
            tasks = new ArrayList<>();
        }
        taskListAdapter = new TaskListAdapter(tasks, this);
        rvTasks.setLayoutManager(new LinearLayoutManager(requireActivity()));
        rvTasks.setAdapter(taskListAdapter);
    }

    private void setUpCategoriesAdapter() {
        if (categories == null) {
            categories = new ArrayList<>();
        }
        categoriesListAdapter = new CategoriesListAdapter(categories, this);
        rvCategories.setLayoutManager(new LinearLayoutManager(requireActivity(),
                LinearLayoutManager.HORIZONTAL, false));
        rvCategories.setAdapter(categoriesListAdapter);
    }

    private void setUpListeners() {
        fabAdd.setOnClickListener(this);
        fabAddTask.setOnClickListener(this);
        fabAddCategories.setOnClickListener(this);

        //live data observer for categories name
        viewModel.getCategoryLiveData().observe(getViewLifecycleOwner(), this::showCategoryList);

        //live data observer for all tasks in a category
        viewModel.getTasksLiveData().observe(getViewLifecycleOwner(), this::showHideTaskList);

        svSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                taskListAdapter.setData(tasks, s, isSortByName);
                return false;
            }
        });
    }

    private void showHideTaskList(List<TaskInfo> taskInfos) {
        if (taskInfos.isEmpty()) {
            tvNoTasks.setVisibility(View.VISIBLE);
            rvTasks.setVisibility(View.GONE);
        } else {
            tvNoTasks.setVisibility(View.GONE);
            rvTasks.setVisibility(View.VISIBLE);
            showTaskList(taskInfos);
        }
    }

    private void showTaskList(List<TaskInfo> tasks) {
        this.tasks = tasks;
        taskListAdapter.setData(tasks, "", isSortByName);
    }

    private void showCategoryList(List<CategoryInfo> categories) {
        this.categories = categories;
        categoriesListAdapter.setData(categories);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.fab_add) {
            animateFAB();
        } else if (view.getId() == R.id.fab_add_task && getActivity() != null) {
            Navigation.findNavController(getActivity(), R.id.nav_host_fragment)
                    .navigate(R.id.action_myTasksFragment_to_addTaskFragment);
            fabAdd.callOnClick();
        } else if (view.getId() == R.id.fab_add_category) {
            showPopup();
            fabAdd.callOnClick();
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

    @SuppressLint("RestrictedApi")
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
        if (menu instanceof MenuBuilder) {
            MenuBuilder m = (MenuBuilder) menu;
            m.setOptionalIconsVisible(true);
        }
        super.onCreateOptionsMenu(menu, inflater);
        if (menu instanceof MenuBuilder) {
            MenuBuilder m = (MenuBuilder) menu;
            m.setOptionalIconsVisible(true);
        }
    }

    public void animateFAB() {
        if (isFabOpen) {
            fabAdd.startAnimation(animBackward);
            fabAddTask.startAnimation(animClose);
            fabAddCategories.startAnimation(animClose);
            fabAddTask.setClickable(false);
            fabAddCategories.setClickable(false);
            isFabOpen = false;
        } else {
            fabAdd.startAnimation(animForward);
            fabAddTask.startAnimation(animOpen);
            fabAddCategories.startAnimation(animOpen);
            fabAddTask.setClickable(true);
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
        viewModel.addCategoryToRepo(categories);

        this.selectedCategory = category;
        viewModel.getTasksFromRepo(category);
    }

    @Override
    public void onDetach() {
        for (CategoryInfo c : categories) {
            c.isSelected = c.category.equalsIgnoreCase(DEFAULT_CATEGORY);
        }
        viewModel.addCategoryToRepo(categories);
        super.onDetach();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.sort_by_name) {
            isSortByName = true;
            taskListAdapter.setData(tasks, "", true);
            return false;
        } else if (item.getItemId() == R.id.sort_by_date) {
            isSortByName = false;
            taskListAdapter.setData(tasks, "", false);
            return false;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(TaskInfo taskInfo) {
        Bundle bundle = new Bundle();
        bundle.putLong(TASK_ID, taskInfo.taskID);

        Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
                .navigate(R.id.action_myTasksFragment_to_addTaskFragment, bundle);
    }

    @Override
    public void onTaskDelete(long taskId) {
        showWarningDialog(taskId);
    }

    private void showWarningDialog(long taskId) {
        new AlertDialog.Builder(requireActivity())
                .setTitle(R.string.delete_task_head)
                .setMessage(R.string.delete_task_desc)
                .setPositiveButton(R.string.yes, (dialog, which) ->
                        viewModel.removeTaskFromRepo(taskId, selectedCategory))
                .setNegativeButton(R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}

