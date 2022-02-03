package com.example.task_rapidchidori_android.ui.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.task_rapidchidori_android.R;
import com.example.task_rapidchidori_android.ui.interfaces.SubTaskCompleteListener;

import java.util.List;

public class SubTaskListAdapter extends RecyclerView.Adapter<SubTaskListAdapter.ViewHolder> {

    private final SubTaskCompleteListener listener;
    private List<String> subTasks;

    public SubTaskListAdapter(List<String> subTasks, SubTaskCompleteListener listener) {
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
        viewHolder.tvSubTaskTitle.setText(subTasks.get(position));
        viewHolder.rbDone.setOnClickListener(view -> listener.onSubTaskComplete(position));
    }

    @Override
    public int getItemCount() {
        return subTasks.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<String> subTasks) {
        this.subTasks = subTasks;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvSubTaskTitle;
        private final RadioButton rbDone;

        public ViewHolder(View view) {
            super(view);
            tvSubTaskTitle = view.findViewById(R.id.tv_subtask_title);
            rbDone = view.findViewById(R.id.rb_done);
        }
    }
}