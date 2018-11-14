package com.shalan.photoweather.history.history_listing;

import com.shalan.photoweather.base.BasePresenter;
import com.shalan.photoweather.data.AppDataManager;
import com.shalan.photoweather.data.realm_models.HistoryModel;
import com.shalan.photoweather.utils.Utils;

import java.util.List;

public class HistoryListFragmentPresenter<V extends HistoryListFragmentViewInteractor> extends BasePresenter<V>
        implements HistoryListFragmentPresenterInteractor<V> {

    public HistoryListFragmentPresenter(AppDataManager dataManager, V baseViewInteractor) {
        super(dataManager, baseViewInteractor);
    }

    @Override
    public List<HistoryModel> getHistories() {
        return Utils.getList(getDataManager().getAllHistoryRecords());
    }
}
