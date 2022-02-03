package com.example.task_rapidchidori_android.ui.adapters;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.task_rapidchidori_android.R;
import com.example.task_rapidchidori_android.ui.interfaces.ImagesClickListener;

import java.util.List;

public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.ViewHolder> {

    private final ImagesClickListener listener;
    private List<Bitmap> images;

    public ImagesAdapter(List<Bitmap> images, ImagesClickListener listener) {
        this.images = images;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ImagesAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.task_images_child_layout, viewGroup, false);

        return new ImagesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImagesAdapter.ViewHolder viewHolder, final int position) {
        Bitmap image = images.get(position);
        viewHolder.ivImage.setImageBitmap(image);

        viewHolder.ivDelete.setOnClickListener(view -> listener.onImageDeleteClick(position));
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<Bitmap> images) {
        this.images = images;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView ivImage;
        private final ImageView ivDelete;

        public ViewHolder(View view) {
            super(view);

            ivImage = view.findViewById(R.id.iv_img);
            ivDelete = view.findViewById(R.id.iv_delete);
        }
    }
}