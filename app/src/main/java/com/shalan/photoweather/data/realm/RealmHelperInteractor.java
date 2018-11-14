package com.shalan.photoweather.data.realm;

import com.shalan.photoweather.data.realm_models.HistoryModel;

import io.realm.RealmResults;

public interface RealmHelperInteractor {
    void insertNewHistoryRecord(String name, String imagePath);
    RealmResults<HistoryModel> getAllHistoryRecords();
}
