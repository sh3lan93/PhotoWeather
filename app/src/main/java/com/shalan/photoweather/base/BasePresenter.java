package com.shalan.photoweather.base;

import com.shalan.photoweather.data.AppDataManager;

public class BasePresenter<V extends BaseViewInteractor> implements BasePresenterInteractor<V> {
    private V baseViewInteractor;
    private AppDataManager dataManager;

    public BasePresenter(V baseViewInteractor, AppDataManager dataManager) {
        this.baseViewInteractor = baseViewInteractor;
        this.dataManager = dataManager;
    }

    public V getBaseViewInteractor() {
        return baseViewInteractor;
    }

    public AppDataManager getDataManager() {
        return dataManager;
    }
}
