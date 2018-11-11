package com.shalan.photoweather.home.camera;

import android.content.Context;
import android.content.pm.PackageManager;
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
        , AskForPermission.PermissionResultListener, AppDialogs.PermissionExplanationDialogListener {

    public static final String TAG = CameraFragment.class.getSimpleName();
    @BindView(R.id.cameraPreview)
    TextureView cameraPreview;
    @BindView(R.id.cautionMessage)
    TextView cautionMessage;

    private OnFragmentInteractionListener mListener;
    private CameraPresenter<CameraViewInteractor> presenter;

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

    private void showCautionMessage(int permissionID){
        cameraPreview.setVisibility(View.GONE);
        cautionMessage.setVisibility(View.VISIBLE);
        if (permissionID == AskForPermission.CAMERA_PERMISSION)
            cautionMessage.setText(R.string.camera_permission_caution_message);
        else if (permissionID == AskForPermission.EXTERNAL_STORAGE_PERMISSION)
            cautionMessage.setText(R.string.external_storage_permission_caution_message);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        initPresenter();
        presenter.checkCameraPermission();
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

    }

    /*listener fired from permission request*/
    @Override
    public void onPermissionDenied(int permissionID) {
        showCautionMessage(permissionID);
    }

    /*listener fired from permission dialog*/
    @Override
    public void onGrantClicked(int permissionID) {

    }

    /*listener fired from permission dialog*/
    @Override
    public void onDeniedClicked(int permissionID) {
        showCautionMessage(permissionID);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case AskForPermission.CAMERA_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    onPermissionGranted(AskForPermission.CAMERA_PERMISSION);
                }else{
                    onPermissionDenied(AskForPermission.CAMERA_PERMISSION);
                }
                break;
            case AskForPermission.EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    onPermissionGranted(AskForPermission.CAMERA_PERMISSION);
                }else{
                    onPermissionDenied(AskForPermission.CAMERA_PERMISSION);
                }
                break;
        }
    }

    @Override
    public void askForCameraPermission() {
        AskForPermission.getInstance(getActivity(), this, this)
                .requestPermission(AskForPermission.CAMERA_PERMISSION);
    }

    public interface OnFragmentInteractionListener {

    }
}
