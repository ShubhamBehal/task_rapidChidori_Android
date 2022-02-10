package com.example.task_rapidchidori_android.ui.fragments;

import static com.example.task_rapidchidori_android.helper.Constants.DATA;
import static com.example.task_rapidchidori_android.helper.Constants.DATE_TIME_FORMAT;
import static com.example.task_rapidchidori_android.helper.Constants.IMAGE_TYPE;
import static com.example.task_rapidchidori_android.helper.Constants.NULL;
import static com.example.task_rapidchidori_android.helper.Constants.NULL_STRING;
import static com.example.task_rapidchidori_android.helper.Constants.TASK_ID;
import static com.example.task_rapidchidori_android.helper.Constants.TASK_TITLE;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.Group;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.task_rapidchidori_android.R;
import com.example.task_rapidchidori_android.data.models.CategoryInfo;
import com.example.task_rapidchidori_android.data.models.ImagesInfo;
import com.example.task_rapidchidori_android.data.models.SubTaskInfo;
import com.example.task_rapidchidori_android.data.models.TaskInfo;
import com.example.task_rapidchidori_android.data.typeconverters.ImageBitmapString;
import com.example.task_rapidchidori_android.receivers.TaskReminderReceiver;
import com.example.task_rapidchidori_android.ui.activities.TaskActivity;
import com.example.task_rapidchidori_android.ui.adapters.ImagesAdapter;
import com.example.task_rapidchidori_android.ui.adapters.SubTaskListAdapter;
import com.example.task_rapidchidori_android.ui.interfaces.ImagesClickListener;
import com.example.task_rapidchidori_android.ui.interfaces.SubTaskCompleteListener;
import com.example.task_rapidchidori_android.ui.viewmodels.AddTaskViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AddTaskFragment extends Fragment implements View.OnClickListener, ImagesClickListener,
        SubTaskCompleteListener {
    private final ArrayList<Bitmap> bitmaps = new ArrayList<>();
    private final ArrayList<String> imageSources = new ArrayList<>();
    private final ArrayList<SubTaskInfo> subtaskList = new ArrayList<>();
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
    private ActivityResultLauncher<String> readMediaPermissionLauncher;
    private ActivityResultLauncher<String> micPermissionLauncher;
    private RecyclerView rvImages;
    private ImagesAdapter imagesAdapter;
    private SubTaskListAdapter subTaskListAdapter;
    private RecyclerView rvSubtasks;
    private Button btnAddSubtask;
    private ActivityResultLauncher<Intent> audioFileLauncher;
    private Uri audioFile;
    private boolean isAudioPlaying;
    private MediaPlayer mediaPlayer;
    private boolean isAudioRecording;
    private MediaRecorder mRecorder;
    private String recordedAudioPath;
    private ImageView ivStartRecording;
    private Group audioGroup;
    private Button btnPlayStopAudio;
    private Button btnDeleteAudio;
    private boolean isRecordingPlaying;
    private TextView tvImageListHead;
    private TextView tvSubtaskListHead;
    private TextView tvAudioHead;
    private long taskId;
    private Button btnMarkTaskComplete;
    private ImageView ivAddCategory;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setUpLaunchers();
    }

    private void setUpLaunchers() {
        audioFileLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        if (result.getData() != null) {
                            audioFile = result.getData().getData();
                        }
                    }
                });

        cameraLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Bitmap bitmap;
                        if (result.getData() != null) {
                            bitmap = (Bitmap) result.getData().getExtras().get(DATA);
                            bitmaps.add(bitmap);
                            String imageSource = ImageBitmapString.BitMapToString(bitmap);
                            imageSources.add(imageSource);
                            refreshImagesList();
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

        readMediaPermissionLauncher =
                registerForActivityResult(new ActivityResultContracts.RequestPermission(),
                        isGranted -> {
                            if (isGranted) {
                                openMediaStorage();
                            } else {
                                Toast.makeText(requireActivity(),
                                        getString(R.string.permission_denied), Toast.LENGTH_SHORT)
                                        .show();
                            }
                        });

        micPermissionLauncher =
                registerForActivityResult(new ActivityResultContracts.RequestPermission(),
                        isGranted -> {
                            if (isGranted) {
                                startStopRecording();
                            } else {
                                Toast.makeText(requireActivity(),
                                        getString(R.string.permission_denied), Toast.LENGTH_SHORT)
                                        .show();
                            }
                        });
    }

    private void refreshImagesList() {
        if (bitmaps.size() > 0) {
            rvImages.setVisibility(View.VISIBLE);
            tvImageListHead.setText(R.string.attached_images_head);
            imagesAdapter.setData(bitmaps);
        } else {
            tvImageListHead.setText(R.string.no_images_attached);
            rvImages.setVisibility(View.GONE);
        }
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

        refreshImagesList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_task, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getArgumentData();
        initViews(view);
        configViews();
        viewModel.getCategoriesFromRepo();
        setUpListeners();
    }

    private void getArgumentData() {
        if (getArguments() != null) {
            taskId = getArguments().getLong(TASK_ID, 0);
        }
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
        btnAddSubtask = view.findViewById(R.id.btn_add_subtask);
        rvSubtasks = view.findViewById(R.id.rv_subtasks);
        audioGroup = view.findViewById(R.id.grp_audio);
        btnPlayStopAudio = view.findViewById(R.id.btn_play_audio);
        btnDeleteAudio = view.findViewById(R.id.btn_delete_audio);
        tvImageListHead = view.findViewById(R.id.tv_attached_images);
        tvSubtaskListHead = view.findViewById(R.id.tv_attached_subtasks_head);
        tvAudioHead = view.findViewById(R.id.tv_audio_head);
        btnMarkTaskComplete = view.findViewById(R.id.btn_mark_complete);
        ivAddCategory = view.findViewById(R.id.iv_add_category);
    }


    private void configViews() {
        viewModel = new ViewModelProvider(this).get(AddTaskViewModel.class);

        Objects.requireNonNull(((TaskActivity) requireActivity()).getSupportActionBar())
                .setTitle(taskId != 0 ? R.string.edit_task_head : R.string.add_task_head);

        animOpen = AnimationUtils.loadAnimation(getActivity(), R.anim.fab_open);
        animClose = AnimationUtils.loadAnimation(getActivity(), R.anim.fab_close);
        animForward = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate_forward);
        animBackward = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate_backward);

        imagesAdapter = new ImagesAdapter(bitmaps, this);
        rvImages.setLayoutManager(new LinearLayoutManager(requireActivity(),
                LinearLayoutManager.HORIZONTAL, false));
        rvImages.setAdapter(imagesAdapter);

        subTaskListAdapter = new SubTaskListAdapter(subtaskList, this);
        rvSubtasks.setLayoutManager(new LinearLayoutManager(requireActivity(),
                LinearLayoutManager.HORIZONTAL, false));
        rvSubtasks.setAdapter(subTaskListAdapter);

        //edit task case
        if (taskId != 0) {
            getDataFromRepoAndFill();
        }
    }

    private void getDataFromRepoAndFill() {
        viewModel.getDataFromRepo(taskId);
    }


    private void setUpListeners() {
        fabAdd.setOnClickListener(this);
        fabImageGallery.setOnClickListener(this);
        fabAudio.setOnClickListener(this);
        fabImageCamera.setOnClickListener(this);
        tvDueDate.setOnClickListener(this);
        btnAddSubtask.setOnClickListener(this);
        btnPlayStopAudio.setOnClickListener(this);
        btnDeleteAudio.setOnClickListener(this);
        btnMarkTaskComplete.setOnClickListener(this);
        ivAddCategory.setOnClickListener(this);

        viewModel.getCategoryLiveData().observe(getViewLifecycleOwner(), this::setUpSpinner);

        tietDesc.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                tietDesc.clearFocus();
            }
            return false;
        });

        viewModel.getIsSaved().observe(getViewLifecycleOwner(), isSaved -> {
            String msg = getString(R.string.task_save_success);
            if (taskId != 0) {
                getString(R.string.task_edit_success);
            }
            if (isSaved) {
                Toast.makeText(requireActivity(), msg, Toast.LENGTH_SHORT).show();
                Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
                        .navigateUp();
                viewModel.resetIsSaved();
            }
        });

        viewModel.isCompleted().observe(getViewLifecycleOwner(), isSuccess -> {
            if (isSuccess) {
                Toast.makeText(requireActivity(), getString(R.string.marked_completed),
                        Toast.LENGTH_SHORT).show();
                Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
                        .navigateUp();
                viewModel.resetIsCompleted();
            }
        });

        viewModel.getTaskInfo().observe(getViewLifecycleOwner(), this::fillData);
        viewModel.getImageInfo().observe(getViewLifecycleOwner(), imagesInfo -> {
            for (ImagesInfo info : imagesInfo) {
                bitmaps.add(ImageBitmapString.StringToBitMap(info.image));
                imageSources.add(info.image);
            }
            refreshImagesList();
        });

        viewModel.getSubTaskInfo().observe(getViewLifecycleOwner(), subTaskInfos -> {
            this.subtaskList.clear();
            this.subtaskList.addAll(subTaskInfos);
            refreshSubtaskList();
        });
    }

    private void fillData(TaskInfo taskInfo) {
        tietTitle.setText(taskInfo.taskTitle);
        tietDesc.setText(taskInfo.taskDescription);
        spCategories.setSelection(getIndex(spCategories, taskInfo.category));
        tvDueDate.setText(taskInfo.dueDate);
        tvDueDate.setTextColor(ContextCompat.getColor(requireActivity(), R.color.black));

        if (!taskInfo.audioURIString.trim().equalsIgnoreCase(NULL)) {
            audioFile = Uri.parse(taskInfo.audioURIString);
            setUpAudioUI();
        }
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
        tietTitle.clearFocus();
        tietDesc.clearFocus();
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
            handleOnAudioClick();
        } else if (view.getId() == R.id.btn_add_subtask) {
            openSubTaskPopup();
        } else if (view.getId() == R.id.btn_play_audio) {
            startStopAudio();
        } else if (view.getId() == R.id.btn_delete_audio) {
            showDeleteAudioWarningDialog();
        } else if (view.getId() == R.id.btn_mark_complete) {
            showCompleteWarningDialog();
        } else if (view.getId() == R.id.iv_add_category) {
            openAddCategoryDialog();
        }
    }

    private void openAddCategoryDialog() {
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

    private void showCompleteWarningDialog() {
        new AlertDialog.Builder(requireActivity())
                .setTitle(R.string.subtask_complete_head)
                .setMessage(R.string.task_complete_msg)
                .setPositiveButton(R.string.yes, (dialog, which) -> markTaskComplete())
                .setNegativeButton(R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void markTaskComplete() {
        viewModel.markTaskComplete(taskId);
    }

    private void showDeleteAudioWarningDialog() {
        new AlertDialog.Builder(requireActivity())
                .setTitle(R.string.subtask_complete_head)
                .setMessage(R.string.image_delete_body)
                .setPositiveButton(R.string.yes, (dialog, which) -> {
                    audioFile = null;
                    setUpAudioUI();
                })
                .setNegativeButton(R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void startStopAudio() {
        if (isRecordingPlaying) {
            stopPlayingRecording();
        } else {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioAttributes(
                    new AudioAttributes.Builder()
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .build());
            try {
                mediaPlayer.setDataSource(requireActivity(), audioFile);
                mediaPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mediaPlayer.start();
            btnPlayStopAudio.setText(R.string.stop_audio);
            isRecordingPlaying = true;
        }

        mediaPlayer.setOnCompletionListener(mediaPlayer -> stopPlayingRecording());
    }

    private void stopPlayingRecording() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
        isRecordingPlaying = false;
        btnPlayStopAudio.setText(R.string.play_audio);
    }

    private void handleOnAudioClick() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = (requireActivity()).getLayoutInflater();
        builder.setTitle(R.string.audio_title);
        builder.setCancelable(false);
        View view = inflater.inflate(R.layout.add_audio_dialog_view, null);
        ivStartRecording = view.findViewById(R.id.iv_start_recording);
        ImageView ivPlayStopRecording = view.findViewById(R.id.iv_stop_play_recording);
        ImageView ivAttachAudio = view.findViewById(R.id.iv_insert_audio);

        ivStartRecording.setOnClickListener(view13 -> onStartRecordingClick());

        ivPlayStopRecording.setOnClickListener(view12 -> {
            if (audioFile != null) {
                if (isAudioPlaying) {
                    stopMusic(ivPlayStopRecording);
                } else {
                    playMusic(ivPlayStopRecording);
                }
            } else {
                Toast.makeText(requireActivity(),
                        getString(R.string.audio_file_not_inserted_error),
                        Toast.LENGTH_SHORT).show();
            }
        });

        ivAttachAudio.setOnClickListener(view1 -> getAudioFromDevice());

        builder.setView(view)
                .setPositiveButton(R.string.save, (dialogInterface, i) -> setUpAudioUI())
                .setNegativeButton(R.string.cancel, (dialogInterface, i) -> audioFile = null);
        builder.create();
        builder.show();

        builder.setOnDismissListener(dialogInterface -> {
            stopMusic(ivPlayStopRecording);
            if (isAudioRecording) {
                mRecorder.stop();
                audioFile = Uri.parse(recordedAudioPath);
                isAudioRecording = false;
            }
        });
    }

    private void onStartRecordingClick() {
        if (hasMicPermissions()) {
            startStopRecording();
        } else {
            micPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO);
        }
    }

    @SuppressLint("DefaultLocale")
    private void startStopRecording() {
        if (isAudioRecording) {
            mRecorder.stop();
            audioFile = Uri.parse(recordedAudioPath);
            isAudioRecording = false;
            ivStartRecording.setImageResource(R.drawable.ic_record_start);
        } else {
            recordedAudioPath = String.format(getString(R.string.audio_path),
                    Environment.getExternalStorageDirectory(),
                    File.separator, Environment.DIRECTORY_DCIM,
                    File.separator, Calendar.getInstance().getTimeInMillis(),
                    getString(R.string.audio_extension));
            mRecorder = new MediaRecorder();
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

            mRecorder.setOutputFile(recordedAudioPath);
            try {
                mRecorder.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mRecorder.start();
            isAudioRecording = true;
            ivStartRecording.setImageResource(R.drawable.ic_record_off);
        }
    }

    private boolean hasMicPermissions() {
        return ContextCompat.checkSelfPermission(requireActivity(),
                Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED;
    }

    private void setUpAudioUI() {
        if (audioFile == null) {
            tvAudioHead.setText(R.string.no_audio_attached);
            audioGroup.setVisibility(View.GONE);
        } else {
            tvAudioHead.setText(R.string.audio_attached);
            audioGroup.setVisibility(View.VISIBLE);
        }
    }

    private void stopMusic(ImageView ivPlayStopRecording) {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
        isAudioPlaying = false;
        ivPlayStopRecording.setImageResource(R.drawable.ic_play);
    }

    private void playMusic(ImageView ivPlayStopRecording) {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioAttributes(
                new AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build());
        try {
            mediaPlayer.setDataSource(requireActivity(), audioFile);
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.start();
        isAudioPlaying = true;
        ivPlayStopRecording.setImageResource(R.drawable.ic_stop);

        mediaPlayer.setOnCompletionListener(mediaPlayer -> {
            if (mediaPlayer != null) {
                mediaPlayer.stop();
            }
            isAudioPlaying = false;
            ivPlayStopRecording.setImageResource(R.drawable.ic_play);
        });
    }

    private void getAudioFromDevice() {
        if (hasMediaStoragePermission()) {
            openMediaStorage();
        } else {
            readMediaPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
    }

    private boolean hasMediaStoragePermission() {
        return ContextCompat.checkSelfPermission(requireActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    private void openMediaStorage() {
        Intent audioIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
        audioFileLauncher.launch(audioIntent);
    }

    private void openSubTaskPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = (requireActivity()).getLayoutInflater();
        builder.setTitle(R.string.enter_subtask_head);
        builder.setCancelable(false);
        View view = inflater.inflate(R.layout.add_category_dialog_view, null);
        EditText etCategory = view.findViewById(R.id.et_category);
        etCategory.setHint(R.string.enter_subtask_head);
        builder.setView(view)
                .setPositiveButton(R.string.ok, (dialog, id) -> {
                    if (TextUtils.isEmpty(etCategory.getText())) {
                        Toast.makeText(requireActivity(),
                                getString(R.string.subtask_empty_error),
                                Toast.LENGTH_SHORT).show();
                    } else {
                        subtaskList.add(new SubTaskInfo(taskId,
                                etCategory.getText().toString().trim(), false));
                        refreshSubtaskList();
                        Toast.makeText(requireActivity(),
                                getString(R.string.subtask_added_success_msg),
                                Toast.LENGTH_SHORT).show();
                    }
                });
        builder.create();
        builder.show();
    }

    private void refreshSubtaskList() {
        if (subtaskList.size() > 0) {
            tvSubtaskListHead.setText(R.string.subtask_list_head);
            rvSubtasks.setVisibility(View.VISIBLE);
            subTaskListAdapter.setData(subtaskList);

        } else {
            tvSubtaskListHead.setText(R.string.no_subtasks_attached);
            rvSubtasks.setVisibility(View.GONE);
        }

        handleCompleteTaskButtonVisibility(subtaskList);
    }

    private void handleCompleteTaskButtonVisibility(ArrayList<SubTaskInfo> subtaskList) {
        int inCompleteSubtaskCount = 0;
        for (SubTaskInfo subTaskInfo : subtaskList) {
            if (!subTaskInfo.isComplete) {
                inCompleteSubtaskCount++;
            }
        }
        if (inCompleteSubtaskCount > 0) {
            btnMarkTaskComplete.setVisibility(View.GONE);
        } else {
            if (taskId != 0) {
                btnMarkTaskComplete.setVisibility(View.VISIBLE);
            } else {
                btnMarkTaskComplete.setVisibility(View.GONE);
            }
        }
    }

    private void handleOnGalleryClick() {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        galleryIntent.setType(IMAGE_TYPE);
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
                                date.set(Calendar.SECOND, 0);
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

    @RequiresApi(api = Build.VERSION_CODES.S)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.save_task) {
            handleTaskSaveClick();
            return false;
        }
        return super.onOptionsItemSelected(item);
    }

    @RequiresApi(api = Build.VERSION_CODES.S)
    @SuppressLint("UnspecifiedImmutableFlag")
    private void scheduleNotification(long id) {
        AlarmManager alarmManager = (AlarmManager) requireActivity()
                .getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(requireActivity(), TaskReminderReceiver.class);
        intent.putExtra(TASK_TITLE, Objects.requireNonNull(tietTitle.getText()).toString());
        intent.putExtra(TASK_ID, id);
        PendingIntent pendingIntent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            pendingIntent = PendingIntent.getBroadcast(requireContext()
                    .getApplicationContext(), 0, intent, PendingIntent.FLAG_MUTABLE);
        } else {
            pendingIntent = PendingIntent.getBroadcast(requireContext()
                    .getApplicationContext(), 0, intent, 0);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,
                    date.getTimeInMillis(), pendingIntent);
        } else {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, date.getTimeInMillis(),
                    pendingIntent);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.S)
    private void handleTaskSaveClick() {
        if (isInputValid()) {
            TaskInfo taskInfo =
                    new TaskInfo(
                            taskId != 0 ? taskId :
                                    Calendar.getInstance().getTimeInMillis(),
                            Objects.requireNonNull(tietTitle.getText()).toString(),
                            Objects.requireNonNull(tietDesc.getText()).toString(),
                            spCategories.getSelectedItem().toString(),
                            tvDueDate.getText().toString(),
                            DateFormat.format(DATE_TIME_FORMAT,
                                    Calendar.getInstance().getTimeInMillis()).toString(),
                            audioFile != null ? audioFile.toString() : NULL_STRING
                    );
            viewModel.saveTaskToRepo(taskInfo, imageSources, subtaskList, taskId != 0);
            scheduleNotification(taskInfo.taskID);
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
                .setPositiveButton(R.string.yes, (dialog, which) -> removeImage(position))
                .setNegativeButton(R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void removeImage(int position) {
        bitmaps.remove(position);
        imageSources.remove(position);
        refreshImagesList();
        Toast.makeText(requireActivity(), getString(R.string.image_removed_success), Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    public void onSubTaskComplete(SubTaskInfo subTaskInfo) {
        new AlertDialog.Builder(requireActivity())
                .setTitle(R.string.subtask_complete_head)
                .setMessage(R.string.subtask_complete_body)
                .setPositiveButton(R.string.yes, (dialog, which) -> removeSubtask(subTaskInfo))
                .setNegativeButton(R.string.no, (dialog, which) -> refreshSubtaskList())
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void removeSubtask(SubTaskInfo subTaskInfo) {
        viewModel.markSubtaskComplete(subTaskInfo);
        for (SubTaskInfo taskInfo : subtaskList) {
            if (taskInfo.subTaskId == subTaskInfo.subTaskId) {
                taskInfo.isComplete = true;
            }
        }
        refreshSubtaskList();
        Toast.makeText(requireActivity(), getString(R.string.subtask_remove_success),
                Toast.LENGTH_SHORT)
                .show();
    }

    private int getIndex(Spinner spinner, String myString) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)) {
                return i;
            }
        }

        return 0;
    }
}