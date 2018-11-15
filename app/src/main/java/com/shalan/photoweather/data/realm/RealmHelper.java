package com.shalan.photoweather.data.realm;

import com.shalan.photoweather.data.realm_models.HistoryModel;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import io.realm.Sort;

public class RealmHelper implements RealmHelperInteractor {


    public RealmHelper() {
    }

    @Override
    public void insertNewHistoryRecord(final String name, final String imagePath) {
        Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                HistoryModel historyModel = new HistoryModel(name, imagePath);
                realm.copyToRealmOrUpdate(historyModel);
            }
        });
    }

    @Override
    public RealmResults<HistoryModel> getAllHistoryRecords() {
        return Realm.getDefaultInstance().where(HistoryModel.class).findAll().sort("name", Sort.DESCENDING);
    }
}
