package com.example.task_rapidchidori_android.ui.fragments;

import static com.example.task_rapidchidori_android.helper.Constants.DATE_TIME_FORMAT;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.task_rapidchidori_android.R;
import com.example.task_rapidchidori_android.data.models.CategoryInfo;
import com.example.task_rapidchidori_android.data.models.TaskInfo;
import com.example.task_rapidchidori_android.data.typeconverters.ImageBitmapString;
import com.example.task_rapidchidori_android.ui.adapters.ImagesAdapter;
import com.example.task_rapidchidori_android.ui.interfaces.ImagesClickListener;
import com.example.task_rapidchidori_android.ui.viewmodelfactories.AddTaskViewModelFactory;
import com.example.task_rapidchidori_android.ui.viewmodels.AddTaskViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class AddTaskFragment extends Fragment implements View.OnClickListener, ImagesClickListener {
    private FloatingActionButton fabAdd;
    private FloatingActionButton fabImageGallery;
    private FloatingActionButton fabImageCamera;
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
    private ActivityResultLauncher<String> cameraPermissionLauncher;
    private ActivityResultLauncher<Intent> cameraLauncher;
    private ActivityResultLauncher<Intent> galleryLauncher;
    private final ArrayList<Bitmap> bitmaps = new ArrayList<>();
    private final ArrayList<String> imageSources = new ArrayList<>();
    private RecyclerView rvImages;
    private ImagesAdapter imagesAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        cameraLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Bitmap bitmap;
                        if (result.getData() != null) {
                            bitmap = (Bitmap) result.getData().getExtras().get("data");
                            bitmaps.add(bitmap);
                            String imageSource = ImageBitmapString.BitMapToString(bitmap);
                            imageSources.add(imageSource);
                            imagesAdapter.setData(bitmaps);
                        }
                    }
                });

        galleryLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        handleOnGalleryPhotoSelect(result.getData());
                    }
                });


        cameraPermissionLauncher =
                registerForActivityResult(new ActivityResultContracts.RequestPermission(),
                        isGranted -> {
                            if (isGranted) {
                                handleOnCameraClick();
                            } else {
                                Toast.makeText(requireActivity(),
                                        getString(R.string.permission_denied), Toast.LENGTH_SHORT)
                                        .show();
                            }
                        });
    }

    private void handleOnGalleryPhotoSelect(Intent data) {
        ClipData clipData = data.getClipData();

        if (clipData != null) {
            for (int i = 0; i < clipData.getItemCount(); i++) {
                Uri imageUri = clipData.getItemAt(i).getUri();
                try {
                    InputStream is = requireActivity().getContentResolver()
                            .openInputStream(imageUri);
                    Bitmap bitmap = BitmapFactory.decodeStream(is);
                    bitmaps.add(bitmap);
                    String imageSource = ImageBitmapString.BitMapToString(bitmap);
                    imageSources.add(imageSource);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        } else {
            Uri imageUri = data.getData();
            try {
                InputStream is = requireActivity().getContentResolver().openInputStream(imageUri);
                Bitmap bitmap = BitmapFactory.decodeStream(is);
                bitmaps.add(bitmap);
                String imageSource = ImageBitmapString.BitMapToString(bitmap);
                imageSources.add(imageSource);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        imagesAdapter.setData(bitmaps);
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
        fabImageGallery = view.findViewById(R.id.fab_add_image_gallery);
        fabImageCamera = view.findViewById(R.id.fab_add_image_camera);
        fabAudio = view.findViewById(R.id.fab_add_audio);
        spCategories = view.findViewById(R.id.sp_categories);
        tvDueDate = view.findViewById(R.id.tv_due_date);
        tietTitle = view.findViewById(R.id.tiet_title);
        tietDesc = view.findViewById(R.id.tiet_desc);
        rvImages = view.findViewById(R.id.rv_images);
    }


    private void configViews() {
        viewModel = new ViewModelProvider(this,
                new AddTaskViewModelFactory(requireActivity().getApplication()))
                .get(AddTaskViewModel.class);

        animOpen = AnimationUtils.loadAnimation(getActivity(), R.anim.fab_open);
        animClose = AnimationUtils.loadAnimation(getActivity(), R.anim.fab_close);
        animForward = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate_forward);
        animBackward = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate_backward);

        imagesAdapter = new ImagesAdapter(bitmaps, this);
        rvImages.setLayoutManager(new LinearLayoutManager(requireActivity(),
                LinearLayoutManager.HORIZONTAL, false));
        rvImages.setAdapter(imagesAdapter);
    }


    private void setUpListeners() {
        fabAdd.setOnClickListener(this);
        fabImageGallery.setOnClickListener(this);
        fabAudio.setOnClickListener(this);
        fabImageCamera.setOnClickListener(this);
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
        } else if (view.getId() == R.id.fab_add_image_gallery) {
            handleOnGalleryClick();
            fabAdd.callOnClick();
        } else if (view.getId() == R.id.fab_add_image_camera) {
            handleOnCameraClick();
            fabAdd.callOnClick();
        } else if (view.getId() == R.id.tv_due_date) {
            openDateTimePicker();
        } else if (view.getId() == R.id.fab_add_audio) {
            //todo handle on audio add click
        }
    }

    private void handleOnGalleryClick() {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        galleryIntent.setType("image/*");
        galleryLauncher.launch(galleryIntent);
    }

    void handleOnCameraClick() {
        if (hasCameraPermissions()) {
            openCamera();
        } else {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA);
        }
    }

    private void openCamera() {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        cameraLauncher.launch(cameraIntent);
    }

    private boolean hasCameraPermissions() {
        return ContextCompat.checkSelfPermission(requireActivity(),
                Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    private void openDateTimePicker() {
        final Calendar currentDate = Calendar.getInstance();
        date = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(requireActivity(),
                (view, year, monthOfYear, dayOfMonth) -> {
                    date.set(year, monthOfYear, dayOfMonth);
                    new TimePickerDialog(AddTaskFragment.this.requireActivity(),
                            (view1, hourOfDay, minute) -> {
                                date.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                date.set(Calendar.MINUTE, minute);
                                AddTaskFragment.this.onDateTimeSelected();
                            }, currentDate.get(Calendar.HOUR_OF_DAY),
                            currentDate.get(Calendar.MINUTE),
                            false).show();
                }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH),
                currentDate.get(Calendar.DATE));
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
            fabImageGallery.startAnimation(animClose);
            fabImageCamera.startAnimation(animClose);
            fabAudio.startAnimation(animClose);
            fabImageGallery.setClickable(false);
            fabImageCamera.setClickable(false);
            fabAudio.setClickable(false);
            isFabOpen = false;
        } else {
            fabAdd.startAnimation(animForward);
            fabImageGallery.startAnimation(animOpen);
            fabImageCamera.startAnimation(animOpen);
            fabAudio.startAnimation(animOpen);
            fabImageGallery.setClickable(true);
            fabImageCamera.setClickable(true);
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
            viewModel.saveTaskToRepo(taskInfo, imageSources);
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

    @Override
    public void onImageDeleteClick(int position) {

        new AlertDialog.Builder(requireActivity())
                .setTitle(R.string.delete_task_head)
                .setMessage(R.string.delete_task_desc)
                .setPositiveButton(R.string.yes, (dialog, which) -> {
                    removeImage(position);
                })
                .setNegativeButton(R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

    }

    private void removeImage(int position) {
        bitmaps.remove(position);
        imageSources.remove(position);
        imagesAdapter.setData(bitmaps);
        Toast.makeText(requireActivity(), getString(R.string.image_removed_success), Toast.LENGTH_SHORT)
                .show();
    }
}