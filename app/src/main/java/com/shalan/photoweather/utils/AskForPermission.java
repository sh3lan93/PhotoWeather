package com.shalan.photoweather.utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.shalan.photoweather.R;

public class AskForPermission {

    public static final int CAMERA_PERMISSION = 1;
    public static final int EXTERNAL_STORAGE_PERMISSION = 2;

    public static final int CAMERA_PERMISSION_REQUEST_CODE = 1000;
    public static final int EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE = 2000;

    private static final String CAMERA_MANIFEST_PERMISSION = Manifest.permission.CAMERA;
    private static final String EXTERNAL_STORAGE_MANIFEST_PERMISSION = Manifest.permission.WRITE_EXTERNAL_STORAGE;

    private static AskForPermission INSTANCE;
    private static PermissionResultListener permissionListener;
    private static Activity activity;
    private static AppDialogs.PermissionExplanationDialogListener permissionDialogListener;

    private AskForPermission(Activity activity, PermissionResultListener listener
            , AppDialogs.PermissionExplanationDialogListener permissionDialogListener) {
        AskForPermission.activity = activity;
        AskForPermission.permissionListener = listener;
        AskForPermission.permissionDialogListener = permissionDialogListener;
    }

    public static AskForPermission getInstance(Activity activity, PermissionResultListener listener
            , AppDialogs.PermissionExplanationDialogListener permissionDialogListener) {
        if (INSTANCE == null)
            INSTANCE = new AskForPermission(activity, listener, permissionDialogListener);
        return INSTANCE;
    }

    public void requestPermission(int permissionID) {
        switch (permissionID) {
            case CAMERA_PERMISSION:
                checkPermission(CAMERA_MANIFEST_PERMISSION, permissionID);
                break;
            case EXTERNAL_STORAGE_PERMISSION:
                checkPermission(EXTERNAL_STORAGE_MANIFEST_PERMISSION, permissionID);
                break;
        }
    }

    private void checkPermission(String permission, int permissionID) {
        if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                switch (permissionID) {
                    case CAMERA_PERMISSION:
                        AppDialogs.showPermissionExplanationDialog(activity, R.string.camera_permission_message, permissionDialogListener);
                        break;
                    case EXTERNAL_STORAGE_PERMISSION:
                        AppDialogs.showPermissionExplanationDialog(activity, R.string.external_storage_permission_message, permissionDialogListener);
                        break;
                }
            } else {
                switch (permissionID) {
                    case CAMERA_PERMISSION:
                        ActivityCompat.requestPermissions(activity, new String[]{CAMERA_MANIFEST_PERMISSION}
                                , CAMERA_PERMISSION_REQUEST_CODE);
                        break;
                    case EXTERNAL_STORAGE_PERMISSION:
                        ActivityCompat.requestPermissions(activity, new String[]{EXTERNAL_STORAGE_MANIFEST_PERMISSION}
                                , EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE);
                        break;
                }
            }
        } else {
            permissionListener.onPermissionGranted();
        }
    }

    public interface PermissionResultListener {
        void onPermissionGranted();
        void onPermissionDenied();
    }
}
