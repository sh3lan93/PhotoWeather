package com.shalan.photoweather.home.camera;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
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

import butterknife.BindView;
import butterknife.ButterKnife;


public class CameraFragment extends BaseFragment implements CameraViewInteractor
        , AskForPermission.PermissionResultListener
        , AppDialogs.PermissionExplanationDialogListener, TextureView.SurfaceTextureListener {

    public static final String TAG = CameraFragment.class.getSimpleName();
    @BindView(R.id.cameraPreview)
    TextureView cameraPreview;
    @BindView(R.id.cautionMessage)
    TextView cautionMessage;

    private OnFragmentInteractionListener mListener;
    private CameraPresenter<CameraViewInteractor> presenter;
    private CameraManager mCameraManager;
    private static final int mBackCamera = 1;

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

    private void showCautionMessage(int permissionID) {
        cameraPreview.setVisibility(View.GONE);
        cautionMessage.setVisibility(View.VISIBLE);
        if (permissionID == AskForPermission.CAMERA_PERMISSION)
            cautionMessage.setText(R.string.camera_permission_caution_message);
        else if (permissionID == AskForPermission.EXTERNAL_STORAGE_PERMISSION)
            cautionMessage.setText(R.string.external_storage_permission_caution_message);
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
            }else {
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
        /*entry point for camera*/
        mCameraManager = getContext() != null
                ? (CameraManager) getContext().getSystemService(Context.CAMERA_SERVICE) : null;
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
        if (AskForPermission.getInstance(getActivity(), this, this)
                .isPermissionGranted(AskForPermission.CAMERA_PERMISSION)) {
            if (mCameraManager != null) {
                presenter.configureCamera(mCameraManager, mBackCamera, cameraPreview.getSurfaceTexture());
                presenter.startCameraStream(mCameraManager);
            }else {
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
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }

    public interface OnFragmentInteractionListener {

    }
}
