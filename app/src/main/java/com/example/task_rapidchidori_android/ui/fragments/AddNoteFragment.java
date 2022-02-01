package com.example.task_rapidchidori_android.ui.fragments;

import static com.example.task_rapidchidori_android.helper.Constants.DATE_TIME_FORMAT;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.task_rapidchidori_android.R;
import com.example.task_rapidchidori_android.data.models.CategoryInfo;
import com.example.task_rapidchidori_android.ui.viewmodelfactories.AddNoteViewModelFactory;
import com.example.task_rapidchidori_android.ui.viewmodels.AddNoteViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;
import java.util.List;

public class AddNoteFragment extends Fragment implements View.OnClickListener {
    private FloatingActionButton fabAdd;
    private FloatingActionButton fabImage;
    private FloatingActionButton fabAudio;
    private boolean isFabOpen;
    private Animation animOpen;
    private Animation animClose;
    private Animation animForward;
    private Animation animBackward;
    private Spinner spCategories;
    private AddNoteViewModel viewModel;
    private TextView tvDueDate;
    private Calendar date;
    private TextInputEditText tietDesc;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_note, container, false);
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
        tietDesc = view.findViewById(R.id.tiet_desc);
    }


    private void configViews() {
        viewModel = new ViewModelProvider(this,
                new AddNoteViewModelFactory(requireActivity().getApplication()))
                .get(AddNoteViewModel.class);

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
    }

    private void setUpSpinner(List<CategoryInfo> categoryInfos) {
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(requireActivity(),
                android.R.layout.simple_spinner_item, viewModel.getCategoryNameList(categoryInfos));
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategories.setAdapter(dataAdapter);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.add_note_menu, menu);
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
                new TimePickerDialog(AddNoteFragment.this.requireActivity(), (view1, hourOfDay, minute) -> {
                    date.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    date.set(Calendar.MINUTE, minute);
                    AddNoteFragment.this.onDateTimeSelected();
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
}