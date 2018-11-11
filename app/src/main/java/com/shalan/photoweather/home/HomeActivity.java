package com.shalan.photoweather.home;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.shalan.photoweather.PhotoWeatherApp;
import com.shalan.photoweather.R;
import com.shalan.photoweather.base.BaseActivity;
import com.shalan.photoweather.data.AppDataManager;
import com.shalan.photoweather.home.camera.CameraFragment;
import com.shalan.photoweather.utils.AskForPermission;

import butterknife.ButterKnife;

public class HomeActivity extends BaseActivity implements HomeViewInteractor
        , CameraFragment.OnFragmentInteractionListener {

    private HomePresenter<HomeViewInteractor> presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        initPresenter();
        if (savedInstanceState == null)
            presenter.initialStart();
    }

    @Override
    protected void initPresenter() {
        AppDataManager dataManager = ((PhotoWeatherApp) getApplicationContext()).getDataManager();
        presenter = new HomePresenter<HomeViewInteractor>(dataManager, this);
    }

    @Override
    public void openCameraFragment() {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, new CameraFragment(), CameraFragment.TAG)
                .addToBackStack(HomeActivity.class.getSimpleName()).commit();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        CameraFragment mCameraFragment = null;
        Fragment mCurrentFragment = getSupportFragmentManager().findFragmentById(R.id.container);
        if (mCurrentFragment instanceof CameraFragment)
            mCameraFragment = (CameraFragment) mCurrentFragment;
        switch (requestCode) {
            case AskForPermission.CAMERA_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (mCameraFragment != null)
                        mCameraFragment.onPermissionGranted(AskForPermission.CAMERA_PERMISSION);
                } else {
                    if (mCameraFragment != null)
                        mCameraFragment.onPermissionDenied(AskForPermission.CAMERA_PERMISSION);
                }
                break;
            case AskForPermission.EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {

                }
                break;
        }
    }
}
