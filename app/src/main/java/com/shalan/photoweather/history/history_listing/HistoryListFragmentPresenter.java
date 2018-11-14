package com.shalan.photoweather.history.history_listing;

import com.shalan.photoweather.base.BasePresenter;
import com.shalan.photoweather.data.AppDataManager;

public class HistoryListFragmentPresenter<V extends HistoryListFragmentViewInteractor> extends BasePresenter<V>
        implements HistoryListFragmentPresenterInteractor<V> {

    public HistoryListFragmentPresenter(AppDataManager dataManager, V baseViewInteractor) {
        super(dataManager, baseViewInteractor);
    }
}
