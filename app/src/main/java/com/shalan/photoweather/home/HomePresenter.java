package com.shalan.photoweather.home;

import com.shalan.photoweather.base.BasePresenter;
import com.shalan.photoweather.data.AppDataManager;

public class HomePresenter<V extends HomeViewInteractor> extends BasePresenter<V> implements HomePresenterInteractor<V>{

    public HomePresenter(AppDataManager dataManager, V baseViewInteractor) {
        super(dataManager, baseViewInteractor);
    }

    @Override
    public void initialStart() {
        getBaseViewInteractor().openCameraFragment();
    }
}
