package com.shalan.photoweather.home.camera;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shalan.photoweather.PhotoWeatherApp;
import com.shalan.photoweather.R;
import com.shalan.photoweather.base.BaseFragment;
import com.shalan.photoweather.data.AppDataManager;
import com.shalan.photoweather.utils.AppDialogs;
import com.shalan.photoweather.utils.AskForPermission;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class CameraFragment extends BaseFragment implements CameraViewInteractor
        , AskForPermission.PermissionResultListener
        , AppDialogs.PermissionExplanationDialogListener, TextureView.SurfaceTextureListener {

    public static final String TAG = CameraFragment.class.getSimpleName();
    @BindView(R.id.cameraPreview)
    TextureView cameraPreview;
    @BindView(R.id.cautionMessage)
    TextView cautionMessage;
    @BindView(R.id.capturePhoto)
    ImageView capturePhoto;
    @BindView(R.id.history)
    ImageView history;
    @BindView(R.id.cameraOptionsHolder)
    ConstraintLayout cameraOptionsHolder;

    private OnFragmentInteractionListener mListener;
    private CameraPresenter<CameraViewInteractor> presenter;
    private CameraManager mCameraManager;
    private static final int mBackCamera = 1;
    private FileOutputStream imageOutputStream;
    private File imageFile;

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
        presenter.checkPermissions();
    }

    private void showCautionMessage(int permissionID) {
        cameraPreview.setVisibility(View.GONE);
        cameraOptionsHolder.setVisibility(View.GONE);
        cautionMessage.setVisibility(View.VISIBLE);
        if (permissionID == AskForPermission.CAMERA_PERMISSION)
            cautionMessage.setText(R.string.camera_permission_caution_message);
        else if (permissionID == AskForPermission.EXTERNAL_STORAGE_PERMISSION)
            cautionMessage.setText(R.string.external_storage_permission_caution_message);
    }

    @OnClick(R.id.capturePhoto)
    public void onCapturePhotoClicked() {
        presenter.lockCaptureSession();
        this.imageFile = presenter.createTempImageFile(presenter.createAppImagesPublicDirectory(getString(R.string.app_name)));
        Log.i(TAG, "onCapturePhotoClicked: " + this.imageFile);
        try {
            this.imageOutputStream = presenter.getOutputPhoto(this.imageFile);
            if (this.imageOutputStream != null)
                cameraPreview.getBitmap().compress(Bitmap.CompressFormat.PNG, 100, this.imageOutputStream);
            else
                AppDialogs.showErrorDialog(getContext(), getString(R.string.output_null_error));
        } catch (Exception e) {
            e.printStackTrace();
            Log.i(TAG, "onCapturePhotoClicked: " + e.getLocalizedMessage());
        } finally {
            presenter.unlockCaptureSession();
            try{
                if (this.imageOutputStream != null)
                    this.imageOutputStream.close();
            }catch (IOException ex){
                ex.printStackTrace();
                Log.i(TAG, "onCapturePhotoClicked: " + ex.getLocalizedMessage());
            }
            if (mListener != null)
                mListener.onFinishCapturingImage(this.imageFile);
        }
    }

    @OnClick(R.id.history)
    public void onHistoryClicked() {

    }

    @Override
    public void onStop() {
        super.onStop();
        presenter.closeCameraDevice();
        presenter.quiteCameraHandler();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (cameraPreview.isAvailable()
                && AskForPermission.getInstance(getActivity(), this, this)
                .isPermissionGranted(AskForPermission.CAMERA_PERMISSION)) {
            if (mCameraManager != null) {
                presenter.configureCamera(mCameraManager, mBackCamera, cameraPreview.getSurfaceTexture());
                presenter.startCameraStream(mCameraManager);
            } else {
                mCameraManager = (CameraManager) getContext().getSystemService(Context.CAMERA_SERVICE);
                presenter.configureCamera(mCameraManager, mBackCamera, cameraPreview.getSurfaceTexture());
                presenter.startCameraStream(mCameraManager);
            }
        } else {
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
        /*entry point for camera and storage permission*/
        switch (permissionID) {
            case AskForPermission.CAMERA_PERMISSION:
                mCameraManager = getContext() != null
                        ? (CameraManager) getContext().getSystemService(Context.CAMERA_SERVICE) : null;
                break;
            case AskForPermission.EXTERNAL_STORAGE_PERMISSION:
                presenter.createAppImagesPublicDirectory(getString(R.string.app_name));
                break;
        }
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
    public void askForStoragePermission() {
        AskForPermission.getInstance(getActivity(), this, this)
                .requestPermission(AskForPermission.EXTERNAL_STORAGE_PERMISSION);
    }

    @Override
    public void askForPermissions() {
        AskForPermission.getInstance(getActivity(), this, this)
                .requestPermission(new int[]{AskForPermission.CAMERA_PERMISSION, AskForPermission.EXTERNAL_STORAGE_PERMISSION});
    }

    @Override
    public void requestPermission(int permission) {
        AskForPermission.getInstance(getActivity(), this, this)
                .forceRequestPermission(permission);
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        if (AskForPermission.getInstance(getActivity(), this, this)
                .isPermissionGranted(AskForPermission.CAMERA_PERMISSION)) {
            if (mCameraManager != null) {
                presenter.configureCamera(mCameraManager, mBackCamera, cameraPreview.getSurfaceTexture());
                presenter.startCameraStream(mCameraManager);
            } else {
                mCameraManager = (CameraManager) getContext().getSystemService(Context.CAMERA_SERVICE);
                presenter.configureCamera(mCameraManager, mBackCamera, cameraPreview.getSurfaceTexture());
                presenter.startCameraStream(mCameraManager);
            }
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        return true;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }

    public interface OnFragmentInteractionListener {
        void onFinishCapturingImage(File capturedImageFile);
    }
}
