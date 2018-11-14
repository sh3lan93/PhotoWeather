package com.shalan.photoweather.adapter;

import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;

import com.shalan.photoweather.data.realm_models.HistoryModel;

public class HistoryDiffCallback extends DiffUtil.ItemCallback<HistoryModel> {

    @Override
    public boolean areItemsTheSame(@NonNull HistoryModel historyModel, @NonNull HistoryModel t1) {
        return historyModel.getName().equals(t1.getName());
    }

    @Override
    public boolean areContentsTheSame(@NonNull HistoryModel historyModel, @NonNull HistoryModel t1) {
        return historyModel == t1;
    }
}
