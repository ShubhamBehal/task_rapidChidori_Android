package com.example.task_rapidchidori_android.ui.adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.task_rapidchidori_android.R;
import com.example.task_rapidchidori_android.data.models.CategoryInfo;
import com.example.task_rapidchidori_android.data.models.TaskInfo;
import com.example.task_rapidchidori_android.data.repository.CategoryRepo;
import com.example.task_rapidchidori_android.ui.fragments.EditCategories;
import com.example.task_rapidchidori_android.ui.interfaces.OnCategorySelect;
import com.example.task_rapidchidori_android.ui.interfaces.TaskItemClickListener;

import java.util.List;
import java.util.Locale;

public class EditCategoriesListAdapter extends RecyclerView.Adapter<EditCategoriesListAdapter.Viewholder> {

    private final List<CategoryInfo> categories;
    private final OnCategorySelect listener;


    public EditCategoriesListAdapter(List<CategoryInfo> categories, OnCategorySelect listener) {
        this.categories = categories;
        this.listener = listener;
    }


    @NonNull
    @Override
    public EditCategoriesListAdapter.Viewholder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.edit_category_view, parent, false);

        return new EditCategoriesListAdapter.Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, final int position) {

      //  CategoryInfo cat = categories.get(position);

        String category = categories.get(position).category;
        boolean isSelected = categories.get(position).isSelected;
        if (isSelected) {
            holder.tvEditCategory
                    .setTextColor(ContextCompat.getColor(holder.tvEditCategory.getContext(),
                            R.color.purple_700));
        } else {
            holder.tvEditCategory
                    .setTextColor(ContextCompat.getColor(holder.tvEditCategory.getContext(),
                            R.color.black));
        }
       // holder.clEdit.setSelected(isSelected);
        holder.tvEditCategory.setText(category);
    //   holder.clEdit.setOnClickListener(view -> listener.onCategorySelect(category));

        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onCategoryDelete(category);
            }
        });
      //  holder.ivDelete.setOnClickListener(view -> listener.onCategoryDelete(category));

        holder.ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

    public static class Viewholder extends RecyclerView.ViewHolder{
        private final TextView tvEditCategory;
        private final ImageView ivDelete,ivEdit;
       // ConstraintLayout clEdit;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            tvEditCategory = itemView.findViewById(R.id.tv_edit_category);
            ivDelete = itemView.findViewById(R.id.iv_delete_category);
            ivEdit = itemView.findViewById(R.id.iv_edit_category);
           // clEdit = itemView.findViewById(R.id.cv_edit_root);
        }
    }


}