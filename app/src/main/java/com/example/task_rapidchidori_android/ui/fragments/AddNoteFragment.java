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

import com.example.task_rapidchidori_android.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class AddNoteFragment extends Fragment implements View.OnClickListener {
    private FloatingActionButton fabAdd;
    private FloatingActionButton fabImage;
    private FloatingActionButton fabAudio;
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
        setUpListeners();
    }

    private void initViews(View view) {
        fabAdd = view.findViewById(R.id.fab_add);
        fabImage = view.findViewById(R.id.fab_add_image);
        fabAudio = view.findViewById(R.id.fab_add_audio);
    }


    private void configViews() {
        animOpen = AnimationUtils.loadAnimation(getActivity(), R.anim.fab_open);
        animClose = AnimationUtils.loadAnimation(getActivity(), R.anim.fab_close);
        animForward = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate_forward);
        animBackward = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate_backward);
    }


    private void setUpListeners() {
        fabAdd.setOnClickListener(this);
        fabImage.setOnClickListener(this);
        fabAudio.setOnClickListener(this);
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
        }
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