package com.example.task_rapidchidori_android.ui.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.task_rapidchidori_android.R;
import com.example.task_rapidchidori_android.data.models.CategoryInfo;
import com.example.task_rapidchidori_android.ui.interfaces.OnCategoriesEditDeleteListener;

import java.util.List;

public class EditCategoriesListAdapter extends RecyclerView.Adapter<EditCategoriesListAdapter.ViewHolder> {

    private final List<CategoryInfo> categories;
    private final OnCategoriesEditDeleteListener listener;

    public EditCategoriesListAdapter(List<CategoryInfo> categories, OnCategoriesEditDeleteListener listener) {
        this.categories = categories;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.edit_category_view, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        CategoryInfo category = categories.get(position);
        if (category.isDefaultCategory) {
            holder.ivDelete.setVisibility(View.GONE);
            holder.ivEdit.setVisibility(View.GONE);
        } else {
            holder.ivDelete.setVisibility(View.VISIBLE);
            holder.ivEdit.setVisibility(View.VISIBLE);
        }

        holder.tvEditCategory.setText(category.category);

        holder.ivDelete.setOnClickListener(v -> listener.onCategoryDelete(category.category));

        holder.ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo handle on edit click
            }
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<CategoryInfo> categories) {
        this.categories.clear();
        this.categories.addAll(categories);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvEditCategory;
        private final ImageView ivDelete;
        private final ImageView ivEdit;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvEditCategory = itemView.findViewById(R.id.tv_edit_category);
            ivDelete = itemView.findViewById(R.id.iv_delete_category);
            ivEdit = itemView.findViewById(R.id.iv_edit_category);
        }
    }
}
