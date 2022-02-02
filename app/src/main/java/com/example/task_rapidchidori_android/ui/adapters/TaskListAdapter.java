package com.example.task_rapidchidori_android.ui.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.task_rapidchidori_android.R;
import com.example.task_rapidchidori_android.data.models.TaskInfo;

import java.util.List;

public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.ViewHolder> {

    private final List<TaskInfo> tasks;
//    private final OnCategorySelect listener;

    public TaskListAdapter(List<TaskInfo> tasks/*, OnCategorySelect listener*/) {
        this.tasks = tasks;
//        this.listener = listener;
    }

    @NonNull
    @Override
    public TaskListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.tasks_child_view, viewGroup, false);

        return new TaskListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        TaskInfo task = tasks.get(position);

        viewHolder.tvTitle.setText(task.taskTitle);
        viewHolder.tvDesc.setText(task.taskDescription);
        viewHolder.tvDueDate.setText(task.dueDate);
        viewHolder.tvAddedDate.setText(task.dateCreated);

        viewHolder.cvRoot.setOnClickListener(view -> {
            //todo handle on task click
        });

        viewHolder.ivDelete.setOnClickListener(view -> {
            //todo handle on delete task click
        });
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<TaskInfo> tasks) {
        this.tasks.clear();
        this.tasks.addAll(tasks);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvTitle;
        private final TextView tvDesc;
        private final TextView tvDueDate;
        private final TextView tvAddedDate;
        private final ImageView ivDelete;
        private final CardView cvRoot;

        public ViewHolder(View view) {
            super(view);
            cvRoot = view.findViewById(R.id.cv_root);
            tvTitle = view.findViewById(R.id.tv_title);
            tvDesc = view.findViewById(R.id.tv_desc);
            tvDueDate = view.findViewById(R.id.tv_due_date);
            tvAddedDate = view.findViewById(R.id.tv_added_date);
            ivDelete = view.findViewById(R.id.iv_delete);
        }
    }
}