package com.example.task_rapidchidori_android.ui.fragments;

import static com.example.task_rapidchidori_android.helper.Constants.DATE_TIME_FORMAT;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.task_rapidchidori_android.R;
import com.example.task_rapidchidori_android.data.models.CategoryInfo;
import com.example.task_rapidchidori_android.data.models.TaskInfo;
import com.example.task_rapidchidori_android.ui.viewmodelfactories.AddTaskViewModelFactory;
import com.example.task_rapidchidori_android.ui.viewmodels.AddTaskViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class AddTaskFragment extends Fragment implements View.OnClickListener {
    private FloatingActionButton fabAdd;
    private FloatingActionButton fabImage;
    private FloatingActionButton fabAudio;
    private boolean isFabOpen;
    private Animation animOpen;
    private Animation animClose;
    private Animation animForward;
    private Animation animBackward;
    private Spinner spCategories;
    private AddTaskViewModel viewModel;
    private TextView tvDueDate;
    private Calendar date;
    private TextInputEditText tietTitle;
    private TextInputEditText tietDesc;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_task, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        configViews();
        viewModel.getCategoriesFromRepo();
        setUpListeners();
    }

    private void initViews(View view) {
        fabAdd = view.findViewById(R.id.fab_add);
        fabImage = view.findViewById(R.id.fab_add_image);
        fabAudio = view.findViewById(R.id.fab_add_audio);
        spCategories = view.findViewById(R.id.sp_categories);
        tvDueDate = view.findViewById(R.id.tv_due_date);
        tietTitle = view.findViewById(R.id.tiet_title);
        tietDesc = view.findViewById(R.id.tiet_desc);
    }


    private void configViews() {
        viewModel = new ViewModelProvider(this,
                new AddTaskViewModelFactory(requireActivity().getApplication()))
                .get(AddTaskViewModel.class);

        animOpen = AnimationUtils.loadAnimation(getActivity(), R.anim.fab_open);
        animClose = AnimationUtils.loadAnimation(getActivity(), R.anim.fab_close);
        animForward = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate_forward);
        animBackward = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate_backward);
    }


    private void setUpListeners() {
        fabAdd.setOnClickListener(this);
        fabImage.setOnClickListener(this);
        fabAudio.setOnClickListener(this);
        tvDueDate.setOnClickListener(this);

        viewModel.getCategoryLiveData().observe(getViewLifecycleOwner(), this::setUpSpinner);

        tietDesc.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                tietDesc.clearFocus();
            }
            return false;
        });

        viewModel.getIsSaved().observe(getViewLifecycleOwner(), isSaved -> {
            if (isSaved) {
                Toast.makeText(requireActivity(), getString(R.string.task_save_success),
                        Toast.LENGTH_SHORT).show();
                Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
                        .navigateUp();
                viewModel.resetIsSaved();
            }
        });
    }

    private void setUpSpinner(List<CategoryInfo> categoryInfos) {
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(requireActivity(),
                android.R.layout.simple_spinner_item, viewModel.getCategoryNameList(categoryInfos));
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategories.setAdapter(dataAdapter);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.add_task_menu, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.fab_add) {
            animateFAB();
        } else if (view.getId() == R.id.fab_add_image) {
            //todo handle on add image click
        } else if (view.getId() == R.id.fab_add_audio) {
            //todo handle on add audio click
        } else if (view.getId() == R.id.tv_due_date) {
            openDateTimePicker();
        }
    }

    private void openDateTimePicker() {
        final Calendar currentDate = Calendar.getInstance();
        date = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(requireActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                date.set(year, monthOfYear, dayOfMonth);
                new TimePickerDialog(AddTaskFragment.this.requireActivity(), (view1, hourOfDay, minute) -> {
                    date.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    date.set(Calendar.MINUTE, minute);
                    AddTaskFragment.this.onDateTimeSelected();
                }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), false).show();
            }
        }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE));
        datePickerDialog.getDatePicker().setMinDate(currentDate.getTimeInMillis());
        datePickerDialog.show();
    }

    private void onDateTimeSelected() {
        tvDueDate.setTextColor(ContextCompat.getColor(requireActivity(), R.color.black));
        tvDueDate.setText(DateFormat.format(DATE_TIME_FORMAT, date.getTime()));
    }

    public void animateFAB() {
        if (isFabOpen) {
            fabAdd.startAnimation(animBackward);
            fabImage.startAnimation(animClose);
            fabAudio.startAnimation(animClose);
            fabImage.setClickable(false);
            fabAudio.setClickable(false);
            isFabOpen = false;
        } else {
            fabAdd.startAnimation(animForward);
            fabImage.startAnimation(animOpen);
            fabAudio.startAnimation(animOpen);
            fabImage.setClickable(true);
            fabAudio.setClickable(true);
            isFabOpen = true;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.save_task) {
            handleTaskSaveClick();
            return false;
        }
        return super.onOptionsItemSelected(item);
    }

    private void handleTaskSaveClick() {
        if (isInputValid()) {
            TaskInfo taskInfo =
                    new TaskInfo(Objects.requireNonNull(tietTitle.getText()).toString(),
                            Objects.requireNonNull(tietDesc.getText()).toString(),
                            spCategories.getSelectedItem().toString(),
                            tvDueDate.getText().toString(),
                            DateFormat.format(DATE_TIME_FORMAT,
                                    Calendar.getInstance().getTimeInMillis()).toString()
                    );
            viewModel.saveTaskToRepo(taskInfo);
        }
    }

    private boolean isInputValid() {
        if (TextUtils.isEmpty(Objects.requireNonNull(tietTitle.getText()).toString())) {
            Toast.makeText(requireActivity(), getString(R.string.empty_title_error),
                    Toast.LENGTH_SHORT)
                    .show();
            return false;
        }
        if (TextUtils.isEmpty(Objects.requireNonNull(tietDesc.getText()).toString())) {
            Toast.makeText(requireActivity(), getString(R.string.empty_description_error),
                    Toast.LENGTH_SHORT)
                    .show();
            return false;
        }
        if (tvDueDate.getText().toString().equalsIgnoreCase(getString(R.string.due_date_label))) {
            Toast.makeText(requireActivity(), getString(R.string.empty_due_date_error),
                    Toast.LENGTH_SHORT)
                    .show();
            return false;
        }
        return true;
    }
}