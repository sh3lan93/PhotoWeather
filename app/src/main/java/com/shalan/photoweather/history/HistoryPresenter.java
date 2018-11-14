package com.shalan.photoweather.history;

import com.shalan.photoweather.base.BasePresenter;
import com.shalan.photoweather.data.AppDataManager;

public class HistoryPresenter<V extends HistoryViewInteractor> extends BasePresenter<V> implements HistoryPresenterInteractor<V> {

    public HistoryPresenter(AppDataManager dataManager, V baseViewInteractor) {
        super(dataManager, baseViewInteractor);
    }

}