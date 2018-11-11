package com.shalan.photoweather.home.camera;

import com.shalan.photoweather.base.BasePresenter;
import com.shalan.photoweather.data.AppDataManager;

public class CameraPresenter<V extends CameraViewInteractor> extends BasePresenter<V> implements CameraPresenterInteractor<V> {

    public CameraPresenter(AppDataManager dataManager, V baseViewInteractor) {
        super(dataManager, baseViewInteractor);
    }

    @Override
    public void checkCameraPermission() {
        getBaseViewInteractor().askForCameraPermission();
    }
}
