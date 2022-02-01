package com.example.task_rapidchidori_android.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.task_rapidchidori_android.R;
import com.example.task_rapidchidori_android.data.models.Category;

import java.util.List;

public class CategoriesListAdapter extends RecyclerView.Adapter<CategoriesListAdapter.ViewHolder> {

    private final List<Category> categories;

    public CategoriesListAdapter(List<Category> categories) {
        this.categories = categories;
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
        viewHolder.tvCategory.setText(categories.get(position).category);
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public void setData(List<Category> categories) {
        this.categories.clear();
        this.categories.addAll(categories);
        notifyItemRangeChanged(0, categories.size());
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCategory;

        public ViewHolder(View view) {
            super(view);
            tvCategory = view.findViewById(R.id.tv_category);
        }
    }
}