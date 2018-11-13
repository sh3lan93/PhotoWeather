package com.shalan.photoweather.home.camera;

import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.support.annotation.NonNull;
import android.util.Log;

public class CaptureCallbacks extends CameraCaptureSession.CaptureCallback {

    public static final String TAG = CaptureCallbacks.class.getSimpleName();

    @Override
    public void onCaptureCompleted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request
            , @NonNull TotalCaptureResult result) {
        super.onCaptureCompleted(session, request, result);
        Log.i(TAG, "onCaptureCompleted: ");
    }
}
