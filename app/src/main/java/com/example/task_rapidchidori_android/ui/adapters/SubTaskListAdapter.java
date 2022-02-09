package com.example.task_rapidchidori_android.ui.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.task_rapidchidori_android.R;
import com.example.task_rapidchidori_android.data.models.SubTaskInfo;
import com.example.task_rapidchidori_android.ui.interfaces.SubTaskCompleteListener;

import java.util.Collections;
import java.util.List;

public class SubTaskListAdapter extends RecyclerView.Adapter<SubTaskListAdapter.ViewHolder> {

    private final SubTaskCompleteListener listener;
    private List<SubTaskInfo> subTasks;

    public SubTaskListAdapter(List<SubTaskInfo> subTasks, SubTaskCompleteListener listener) {
        this.subTasks = subTasks;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.subtasks_child_view, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        SubTaskInfo subTaskInfo = subTasks.get(position);
        viewHolder.tvSubTaskTitle.setText(subTaskInfo.subTaskTitle);
        if (subTaskInfo.isComplete) {
            viewHolder.cbDone.setChecked(true);
            viewHolder.cbDone.setOnClickListener(null);
            viewHolder.cbDone.setClickable(false);
        } else {
            viewHolder.cbDone.setChecked(false);
            viewHolder.cbDone.setClickable(true);
            viewHolder.cbDone.setOnClickListener(view -> listener.onSubTaskComplete(subTaskInfo));
        }
    }

    @Override
    public int getItemCount() {
        return subTasks.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<SubTaskInfo> subTasks) {
        this.subTasks = subTasks;
        Collections.sort(this.subTasks, (sub1, sub2) -> Boolean.compare(sub1.isComplete,
                sub2.isComplete));
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvSubTaskTitle;
        private final CheckBox cbDone;

        public ViewHolder(View view) {
            super(view);
            tvSubTaskTitle = view.findViewById(R.id.tv_subtask_title);
            cbDone = view.findViewById(R.id.cb_done);
        }
    }
}