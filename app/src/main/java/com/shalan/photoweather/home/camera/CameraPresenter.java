package com.shalan.photoweather.home.camera;

import android.annotation.SuppressLint;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.Size;
import android.view.Surface;

import com.shalan.photoweather.base.BasePresenter;
import com.shalan.photoweather.data.AppDataManager;

import java.util.Collections;

public class CameraPresenter<V extends CameraViewInteractor> extends BasePresenter<V> implements CameraPresenterInteractor<V> {

    public static final String TAG = CameraPresenter.class.getSimpleName();
    private Size cameraPreviewSize;
    private String mCameraID;
    private CameraDevice.StateCallback mCameraPreviewStateCallback;
    private CameraDevice cameraDevice;
    private CaptureRequest.Builder captureRequestBuilder;
    private CaptureRequest captureRequest;
    private CameraCaptureSession cameraCaptureSession;
    private HandlerThread cameraBackgroundThread;
    private Handler cameraBackgroundHandler;

    public CameraPresenter(AppDataManager dataManager, V baseViewInteractor) {
        super(dataManager, baseViewInteractor);
    }

    @Override
    public void checkCameraPermission() {
        getBaseViewInteractor().askForCameraPermission();
    }

    @Override
    public void forceRequestPermission(int permissionID) {
        getBaseViewInteractor().requestPermission(permissionID);
    }

    @Override
    public void configureCamera(CameraManager manager, int backCameraID, SurfaceTexture cameraPreview) {
        getCameraCharacteristics(manager, backCameraID);
        defineCameraBackgroundThreading();
        defineCallbackForCamera(cameraPreview);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void startCameraStream(CameraManager manager) {
        try {
            manager.openCamera(this.mCameraID, this.mCameraPreviewStateCallback, this.cameraBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
            Log.i(TAG, "startCamera: " + e.getLocalizedMessage());
        }
    }

    @Override
    public void closeCameraDevice() {
        if (cameraCaptureSession != null) {
            cameraCaptureSession.close();
            cameraCaptureSession = null;
        }

        if (cameraDevice != null) {
            cameraDevice.close();
            cameraDevice = null;
        }
    }

    @Override
    public void quiteCameraHandler() {
        if (cameraBackgroundHandler != null) {
            cameraBackgroundThread.quitSafely();
            cameraBackgroundThread = null;
            cameraBackgroundHandler = null;
        }
    }

    private void defineCameraBackgroundThreading() {
        this.cameraBackgroundThread = new HandlerThread(TAG);
        this.cameraBackgroundThread.start();
        this.cameraBackgroundHandler = new Handler(this.cameraBackgroundThread.getLooper());
    }

    private void defineCallbackForCamera(final SurfaceTexture cameraPreview) {
        this.mCameraPreviewStateCallback = new CameraDevice.StateCallback() {

            @Override
            public void onOpened(@NonNull CameraDevice camera) {
                CameraPresenter.this.cameraDevice = camera;
                startPreviewSession(cameraPreview);
            }

            @Override
            public void onDisconnected(@NonNull CameraDevice camera) {
                camera.close();
                cameraDevice = null;
            }

            @Override
            public void onError(@NonNull CameraDevice camera, int error) {
                camera.close();
                cameraDevice = null;
            }
        };
    }

    private void startPreviewSession(SurfaceTexture cameraPreview) {
        cameraPreview.setDefaultBufferSize(cameraPreviewSize.getWidth(), cameraPreviewSize.getHeight());
        Surface previewSurface = new Surface(cameraPreview);
        try {
            captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            captureRequestBuilder.addTarget(previewSurface);
            cameraDevice.createCaptureSession(Collections.singletonList(previewSurface),
                    new CameraCaptureSession.StateCallback() {

                        @Override
                        public void onConfigured(CameraCaptureSession cameraCaptureSession) {
                            if (cameraDevice == null) {
                                return;
                            }

                            try {
                                captureRequest = captureRequestBuilder.build();
                                CameraPresenter.this.cameraCaptureSession = cameraCaptureSession;
                                CameraPresenter.this.cameraCaptureSession.setRepeatingRequest(captureRequest,
                                        null, cameraBackgroundHandler);
                            } catch (CameraAccessException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onConfigureFailed(CameraCaptureSession cameraCaptureSession) {

                        }
                    }, cameraBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
            Log.i(TAG, "startPreviewSession: " + e.getLocalizedMessage());
        }
    }

    private void getCameraCharacteristics(CameraManager manager, int backCameraID) {
        try {
            for (String cameraID : manager.getCameraIdList()) {
                CameraCharacteristics camCharacteristics = manager.getCameraCharacteristics(cameraID);
                if (camCharacteristics.get(CameraCharacteristics.LENS_FACING) != null
                        && camCharacteristics.get(CameraCharacteristics.LENS_FACING) == backCameraID) {
                    StreamConfigurationMap streamConfigurationMap = camCharacteristics
                            .get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
                    this.cameraPreviewSize = streamConfigurationMap.getOutputSizes(SurfaceTexture.class)[0];
                    this.mCameraID = cameraID;
                    break;
                }
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
            Log.i(TAG, "configureCamera: " + e.getLocalizedMessage());
        }
    }
}
