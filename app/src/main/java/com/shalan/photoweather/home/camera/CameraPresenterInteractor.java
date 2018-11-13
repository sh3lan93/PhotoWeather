package com.shalan.photoweather.home.camera;

import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraManager;

import com.shalan.photoweather.base.BasePresenterInteractor;

import java.io.File;

public interface CameraPresenterInteractor<V extends CameraViewInteractor> extends BasePresenterInteractor<V> {
    void checkCameraPermission();
    void checkStoragePermission();
    void forceRequestPermission(int permissionID);
    void configureCamera(CameraManager manager, int backCameraID, SurfaceTexture cameraPreview);
    void startCameraStream(CameraManager manager);
    void closeCameraDevice();
    void quiteCameraHandler();
    File createAppImagesPublicDirectory(String directoryName);
    File createTempImageFile(File appDirectory);

}
