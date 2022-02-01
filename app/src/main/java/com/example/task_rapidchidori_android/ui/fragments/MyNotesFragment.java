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
import androidx.navigation.Navigation;

import com.example.task_rapidchidori_android.R;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MyNotesFragment extends Fragment implements View.OnClickListener {

    private FloatingActionButton fabAdd;
    private ExtendedFloatingActionButton fabAddNotes;
    private ExtendedFloatingActionButton fabAddCategories;
    private boolean isFabOpen;
    private Animation animOpen;
    private Animation animClose;
    private Animation animForward;
    private Animation animBackward;

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
    }

    private void initViews(View view) {
        fabAdd = view.findViewById(R.id.fab_add);
        fabAddNotes = view.findViewById(R.id.fab_add_note);
        fabAddCategories = view.findViewById(R.id.fab_add_category);
    }


    private void configViews() {
        animOpen = AnimationUtils.loadAnimation(getActivity(), R.anim.fab_open);
        animClose = AnimationUtils.loadAnimation(getActivity(), R.anim.fab_close);
        animForward = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate_forward);
        animBackward = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate_backward);
    }


    private void setUpListeners() {
        fabAdd.setOnClickListener(this);
        fabAddNotes.setOnClickListener(this);
        fabAddCategories.setOnClickListener(this);
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
}