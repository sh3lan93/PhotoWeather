package com.shalan.photoweather.view_holder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.shalan.photoweather.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HistoryViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.capturedImage)
    public ImageView capturedImage;
    @BindView(R.id.imageName)
    public TextView imageName;

    public HistoryViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
