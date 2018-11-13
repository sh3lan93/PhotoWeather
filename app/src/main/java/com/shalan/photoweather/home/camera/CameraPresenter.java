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
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.Size;
import android.view.Surface;

import com.shalan.photoweather.base.BasePresenter;
import com.shalan.photoweather.data.AppDataManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

public class CameraPresenter<V extends CameraViewInteractor> extends BasePresenter<V> implements CameraPresenterInteractor<V> {

    public static final String TAG = CameraPresenter.class.getSimpleName();
    private static final String IMAGE_FORMAT = ".jpg";
    private Size cameraPreviewSize;
    private String mCameraID;
    private CameraDevice.StateCallback mCameraPreviewStateCallback;
    private CameraDevice cameraDevice;
    private CaptureRequest.Builder captureRequestBuilder;
    private CaptureRequest captureRequest;
    private CameraCaptureSession cameraCaptureSession;
    private HandlerThread cameraBackgroundThread;
    private Handler cameraBackgroundHandler;
    private File storageDirectory;
    private File appGalleryFolder = null;

    public CameraPresenter(AppDataManager dataManager, V baseViewInteractor) {
        super(dataManager, baseViewInteractor);
    }

    @Override
    public void checkCameraPermission() {
        getBaseViewInteractor().askForCameraPermission();
    }

    @Override
    public void checkStoragePermission() {
        getBaseViewInteractor().askForStoragePermission();
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

    @Override
    public File createAppImagesPublicDirectory(String directoryName) {
        this.storageDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        this.appGalleryFolder = new File(this.storageDirectory, directoryName);
        if (!this.appGalleryFolder.exists()){
            this.appGalleryFolder.mkdirs();
        }
        return this.appGalleryFolder;
    }

    @Override
    public File createTempImageFile(File appDirectory) {
        String timeStamp = new SimpleDateFormat("yyyyddMM_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = appDirectory.getName().concat("_").concat(timeStamp);
        File imageFile = null;
        try {
            imageFile = File.createTempFile(imageFileName, IMAGE_FORMAT, appDirectory);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageFile;
    }

    @Override
    public FileOutputStream getOutputPhoto(File imageFile) {
        FileOutputStream imageOutputStream = null;
        try {
            imageOutputStream = new FileOutputStream(imageFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return imageOutputStream;
    }

    @Override
    public void lockCaptureSession() {
        try {
            cameraCaptureSession.capture(captureRequestBuilder.build(), null, cameraBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void unlockCaptureSession() {
        try {
            cameraCaptureSession.setRepeatingRequest(captureRequestBuilder.build(), null, cameraBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void checkPermissions() {
        getBaseViewInteractor().askForPermissions();
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
