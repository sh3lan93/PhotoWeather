package com.shalan.photoweather.home.camera;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shalan.photoweather.PhotoWeatherApp;
import com.shalan.photoweather.R;
import com.shalan.photoweather.base.BaseFragment;
import com.shalan.photoweather.data.AppDataManager;
import com.shalan.photoweather.utils.AppDialogs;
import com.shalan.photoweather.utils.AskForPermission;

import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;


public class CameraFragment extends BaseFragment implements CameraViewInteractor
        , AskForPermission.PermissionResultListener
        , AppDialogs.PermissionExplanationDialogListener, TextureView.SurfaceTextureListener{

    public static final String TAG = CameraFragment.class.getSimpleName();
    @BindView(R.id.cameraPreview)
    TextureView cameraPreview;
    @BindView(R.id.cautionMessage)
    TextView cautionMessage;

    private OnFragmentInteractionListener mListener;
    private CameraPresenter<CameraViewInteractor> presenter;
    private CameraManager mCameraManager;
    private int mBackCamera;
    private Size cameraPreviewSize;
    private String mCameraID;
    private CameraDevice.StateCallback mCameraPreviewStateCallback;
    private HandlerThread cameraBackgroundThread;
    private Handler cameraBackgroundHandler;
    private CameraDevice cameraDevice;
    private CaptureRequest.Builder captureRequestBuilder;
    private CaptureRequest captureRequest;
    private CameraCaptureSession cameraCaptureSession;

    public CameraFragment() {
        // Required empty public constructor
    }

    public static CameraFragment newInstance() {
        CameraFragment fragment = new CameraFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_camera, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        initPresenter();
        presenter.checkCameraPermission();
    }

    private void showCautionMessage(int permissionID){
        cameraPreview.setVisibility(View.GONE);
        cautionMessage.setVisibility(View.VISIBLE);
        if (permissionID == AskForPermission.CAMERA_PERMISSION)
            cautionMessage.setText(R.string.camera_permission_caution_message);
        else if (permissionID == AskForPermission.EXTERNAL_STORAGE_PERMISSION)
            cautionMessage.setText(R.string.external_storage_permission_caution_message);
    }

    private void configCamera(){
        try {
            for (String id : mCameraManager.getCameraIdList()){
                CameraCharacteristics camCharacteristics = mCameraManager.getCameraCharacteristics(id);
                if (camCharacteristics.get(CameraCharacteristics.LENS_FACING) != null
                        && camCharacteristics.get(CameraCharacteristics.LENS_FACING) == this.mBackCamera){
                    StreamConfigurationMap streamConfigurationMap = camCharacteristics
                            .get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
                    this.cameraPreviewSize = streamConfigurationMap.getOutputSizes(SurfaceTexture.class)[0];
                    this.mCameraID = id;
                }
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
            Log.i(TAG, "configCamera: " + e.getLocalizedMessage());
        }
    }

    private void configBackgroundThreadForCamera(){
        this.cameraBackgroundThread = new HandlerThread(TAG);
        this.cameraBackgroundThread.start();
        this.cameraBackgroundHandler = new Handler(this.cameraBackgroundThread.getLooper());
    }
    @SuppressLint("MissingPermission")
    private void startCamera(){
        try {
            mCameraManager.openCamera(this.mCameraID, this.mCameraPreviewStateCallback, this.cameraBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
            Log.i(TAG, "startCamera: " + e.getLocalizedMessage());
        }
    }

    private void startPreviewSession() {
        try {
            SurfaceTexture surfaceTexture = cameraPreview.getSurfaceTexture();
            surfaceTexture.setDefaultBufferSize(cameraPreviewSize.getWidth(), cameraPreviewSize.getHeight());
            Surface previewSurface = new Surface(surfaceTexture);
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
                                CameraFragment.this.cameraCaptureSession = cameraCaptureSession;
                                CameraFragment.this.cameraCaptureSession.setRepeatingRequest(captureRequest,
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

    @Override
    public void onStop() {
        super.onStop();
        if (cameraDevice != null){
            cameraDevice.close();
            cameraDevice = null;
        }

        if (cameraBackgroundHandler != null){
            cameraBackgroundThread.quitSafely();
            cameraBackgroundThread = null;
            cameraBackgroundHandler = null;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        configBackgroundThreadForCamera();
        if (cameraPreview.isAvailable()){
            configCamera();
            startCamera();
        }else {
            cameraPreview.setSurfaceTextureListener(this);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    protected void initPresenter() {
        AppDataManager dataManager = ((PhotoWeatherApp) getContext().getApplicationContext()).getDataManager();
        presenter = new CameraPresenter<CameraViewInteractor>(dataManager, this);
    }

    /*listener fired from permission request*/
    @Override
    public void onPermissionGranted(int permissionID) {
        /*entry point for camera*/
        this.mCameraManager = (CameraManager) getContext().getSystemService(Context.CAMERA_SERVICE);
        this.mBackCamera = CameraCharacteristics.LENS_FACING_BACK;
        this.mCameraPreviewStateCallback = new CameraDevice.StateCallback(){

            @Override
            public void onOpened(@NonNull CameraDevice camera) {
                cameraDevice = camera;
                startPreviewSession();
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

    /*listener fired from permission request*/
    @Override
    public void onPermissionDenied(int permissionID) {
        showCautionMessage(permissionID);
    }

    /*listener fired from permission dialog*/
    @Override
    public void onGrantClicked(int permissionID) {
        presenter.forceRequestPermission(permissionID);
    }

    /*listener fired from permission dialog*/
    @Override
    public void onDeniedClicked(int permissionID) {
        showCautionMessage(permissionID);
    }


    @Override
    public void askForCameraPermission() {
        AskForPermission.getInstance(getActivity(), this, this)
                .requestPermission(AskForPermission.CAMERA_PERMISSION);
    }

    @Override
    public void requestPermission(int permission) {
        AskForPermission.getInstance(getActivity(), this, this)
                .forceRequestPermission(permission);
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        configCamera();
        startCamera();
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }

    public interface OnFragmentInteractionListener {

    }
}
