package com.shalan.photoweather.adapter;

import android.support.annotation.NonNull;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.util.DiffUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shalan.photoweather.R;
import com.shalan.photoweather.data.realm_models.HistoryModel;
import com.shalan.photoweather.view_holder.HistoryViewHolder;
import com.squareup.picasso.Picasso;

import java.io.File;

public class HistoryAdapter extends ListAdapter<HistoryModel, HistoryViewHolder> {

    private HistoryListener listener;

    public HistoryAdapter(@NonNull DiffUtil.ItemCallback<HistoryModel> diffCallback, HistoryListener listener) {
        super(diffCallback);
        this.listener = listener;
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.history_item_view_layout, viewGroup, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder historyViewHolder, int i) {
        HistoryModel model = getItem(i);
        Picasso.get().load(new File(model.getImagePath())).into(historyViewHolder.capturedImage);
        historyViewHolder.imageName.setText(model.getName());
        historyViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onHistoryClicked(getItem(historyViewHolder.getAdapterPosition()).getImagePath());
            }
        });
    }

    public interface HistoryListener{
        void onHistoryClicked(String imagePath);
    }
}
