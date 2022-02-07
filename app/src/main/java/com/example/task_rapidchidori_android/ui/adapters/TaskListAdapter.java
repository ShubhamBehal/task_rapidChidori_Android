package com.example.task_rapidchidori_android.ui.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.task_rapidchidori_android.R;
import com.example.task_rapidchidori_android.data.models.TaskInfo;
import com.example.task_rapidchidori_android.ui.interfaces.TaskItemClickListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.ViewHolder> {

    private final List<TaskInfo> tasks;
    private final TaskItemClickListener listener;

    public TaskListAdapter(List<TaskInfo> tasks, TaskItemClickListener listener) {
        this.tasks = tasks;
        this.listener = listener;
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
        if (task.isCompleted) {
            viewHolder.clParent.setBackgroundColor(
                    ContextCompat.getColor(viewHolder.clParent.getContext(), R.color.grey_opacity));
            viewHolder.tvDueDate.setText(String.format(viewHolder.clParent.getContext()
                    .getString(R.string.completed_on), task.completedDate));
        } else {
            viewHolder.tvDueDate.setText(String.format(viewHolder.tvDueDate.getContext()
                    .getString(R.string.due_date), task.dueDate));
            viewHolder.cvRoot.setOnClickListener(view -> listener.onItemClick(task));
        }

        viewHolder.tvTitle.setText(task.taskTitle);
        viewHolder.tvDesc.setText(task.taskDescription);
        viewHolder.tvAddedDate.setText(String.format(viewHolder.tvAddedDate.getContext()
                .getString(R.string.created_on), task.dateCreated));
        viewHolder.ivDelete.setOnClickListener(view -> listener.onTaskDelete(task.taskID));
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    @SuppressLint({"NotifyDataSetChanged", "SimpleDateFormat"})
    public void setData(List<TaskInfo> tasks, String queryText, boolean sortByName) {
        this.tasks.clear();
        for (TaskInfo t : tasks) {
            if (t.taskTitle.toLowerCase().contains(queryText.toLowerCase())) {
                this.tasks.add(t);
            }
        }

        if (sortByName) {
            Collections.sort(this.tasks, (s1, s2) -> s1.taskTitle.compareToIgnoreCase(s2.taskTitle));
        } else {
            Collections.sort(this.tasks, (s1, s2) ->
            {
                try {
                    return Objects.requireNonNull(new SimpleDateFormat("dd/MM/yyyy")
                            .parse(s1.dateCreated))
                            .compareTo(new SimpleDateFormat("dd/MM/yyyy")
                                    .parse(s2.dateCreated));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return 0;
            });
        }
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvTitle;
        private final TextView tvDesc;
        private final TextView tvDueDate;
        private final TextView tvAddedDate;
        private final ImageView ivDelete;
        private final CardView cvRoot;
        private final ConstraintLayout clParent;

        public ViewHolder(View view) {
            super(view);
            cvRoot = view.findViewById(R.id.cv_root);
            tvTitle = view.findViewById(R.id.tv_title);
            tvDesc = view.findViewById(R.id.tv_desc);
            tvDueDate = view.findViewById(R.id.tv_due_date);
            tvAddedDate = view.findViewById(R.id.tv_added_date);
            ivDelete = view.findViewById(R.id.iv_delete);
            clParent = view.findViewById(R.id.cl_parent);
        }
    }
}