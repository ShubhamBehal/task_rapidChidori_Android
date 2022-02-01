package com.example.task_rapidchidori_android.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.task_rapidchidori_android.R;
import com.example.task_rapidchidori_android.data.models.CategoryInfo;
import com.example.task_rapidchidori_android.ui.interfaces.OnCategorySelect;

import java.util.List;

public class CategoriesListAdapter extends RecyclerView.Adapter<CategoriesListAdapter.ViewHolder> {

    private final List<CategoryInfo> categories;
    private final OnCategorySelect listener;

    public CategoriesListAdapter(List<CategoryInfo> categories, OnCategorySelect listener) {
        this.categories = categories;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.categories_child_view, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        String category = categories.get(position).category;
        boolean isSelected = categories.get(position).isSelected;
        if (isSelected) {
            viewHolder.tvCategory
                    .setTextColor(ContextCompat.getColor(viewHolder.tvCategory.getContext(),
                            R.color.white));
        } else {
            viewHolder.tvCategory
                    .setTextColor(ContextCompat.getColor(viewHolder.tvCategory.getContext(),
                            R.color.black));
        }
        viewHolder.clRoot.setSelected(isSelected);
        viewHolder.tvCategory.setText(category);
        viewHolder.clRoot.setOnClickListener(view -> listener.onCategorySelect(category));
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public void setData(List<CategoryInfo> categories) {
        this.categories.clear();
        this.categories.addAll(categories);
        notifyItemRangeChanged(0, categories.size());
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCategory;
        ConstraintLayout clRoot;

        public ViewHolder(View view) {
            super(view);
            tvCategory = view.findViewById(R.id.tv_category);
            clRoot = view.findViewById(R.id.cl_root);
        }
    }
}