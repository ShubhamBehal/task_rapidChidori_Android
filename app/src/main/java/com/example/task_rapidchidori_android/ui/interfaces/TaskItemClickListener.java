package com.example.task_rapidchidori_android.ui.interfaces;

import com.example.task_rapidchidori_android.data.models.TaskInfo;

public interface TaskItemClickListener {
    void onItemClick(TaskInfo taskInfo);

    void onTaskDelete(long taskId);
}
