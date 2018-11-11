package com.shalan.photoweather.home.camera;

import com.shalan.photoweather.base.BaseViewInteractor;

public interface CameraViewInteractor extends BaseViewInteractor {
    void askForCameraPermission();
    void requestPermission(int permission);
}
