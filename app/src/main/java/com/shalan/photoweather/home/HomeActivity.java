package com.shalan.photoweather.home;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.shalan.photoweather.PhotoWeatherApp;
import com.shalan.photoweather.R;
import com.shalan.photoweather.base.BaseActivity;
import com.shalan.photoweather.data.AppDataManager;
import com.shalan.photoweather.home.camera.CameraFragment;
import com.shalan.photoweather.home.weather_info.WeatherInfoFragment;
import com.shalan.photoweather.utils.AskForPermission;

import java.io.File;

import butterknife.ButterKnife;

public class HomeActivity extends BaseActivity implements HomeViewInteractor
        , CameraFragment.OnFragmentInteractionListener, WeatherInfoFragment.OnFragmentInteractionListener {

    public static final String TAG = HomeActivity.class.getSimpleName();
    private static final int GOOGLE_PLAY_SERVICE_REQUEST_CODE = 4000;
    private static final String IMAGE_TYPE = "image/*";

    private HomePresenter<HomeViewInteractor> presenter;
    private Fragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        initPresenter();
        if (savedInstanceState == null) {
            presenter.initialStart();
        }
    }

    @Override
    protected void initPresenter() {
        AppDataManager dataManager = ((PhotoWeatherApp) getApplicationContext()).getDataManager();
        presenter = new HomePresenter<HomeViewInteractor>(dataManager, this);
    }

    @Override
    public void openCameraFragment() {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, CameraFragment.newInstance(), CameraFragment.TAG)
                .addToBackStack(HomeActivity.class.getSimpleName()).commit();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
            finishAndRemoveTask();
        }else {
            getSupportFragmentManager().popBackStack();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        int googleApiAvailability = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);
        if (googleApiAvailability != ConnectionResult.SUCCESS
                && GoogleApiAvailability.getInstance().isUserResolvableError(googleApiAvailability)) {
            GoogleApiAvailability.getInstance()
                    .getErrorDialog(this, googleApiAvailability, GOOGLE_PLAY_SERVICE_REQUEST_CODE)
                    .show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case GOOGLE_PLAY_SERVICE_REQUEST_CODE:
                if (resultCode != RESULT_OK)
                    finish();
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        CameraFragment mCameraFragment = null;
        WeatherInfoFragment mWeatherInfoFragment = null;
        Fragment mCurrentFragment = getSupportFragmentManager().findFragmentById(R.id.container);
        if (mCurrentFragment instanceof CameraFragment)
            mCameraFragment = (CameraFragment) mCurrentFragment;
        else if (mCurrentFragment instanceof WeatherInfoFragment)
            mWeatherInfoFragment = (WeatherInfoFragment) mCurrentFragment;
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
                    if (mCameraFragment != null)
                        mCameraFragment.onPermissionGranted(AskForPermission.EXTERNAL_STORAGE_PERMISSION);
                } else {
                    if (mCameraFragment != null)
                        mCameraFragment.onPermissionDenied(AskForPermission.EXTERNAL_STORAGE_PERMISSION);
                }
                break;
            case AskForPermission.ALL_PERMISSIONS_REQUEST_CODE:
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                            if (mCameraFragment != null) {
                                if (permissions[i].equals(AskForPermission.CAMERA_MANIFEST_PERMISSION))
                                    mCameraFragment.onPermissionGranted(AskForPermission.CAMERA_PERMISSION);
                                else
                                    mCameraFragment.onPermissionGranted(AskForPermission.EXTERNAL_STORAGE_PERMISSION);
                            }
                        } else {
                            if (mCameraFragment != null) {
                                if (permissions[i].equals(AskForPermission.CAMERA_MANIFEST_PERMISSION))
                                    mCameraFragment.onPermissionDenied(AskForPermission.CAMERA_PERMISSION);
                                else
                                    mCameraFragment.onPermissionDenied(AskForPermission.EXTERNAL_STORAGE_PERMISSION);
                            }
                        }
                    }
                }
                break;
            case AskForPermission.COARSE_LOCATION_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (mWeatherInfoFragment != null)
                        mWeatherInfoFragment.onPermissionGranted(AskForPermission.COARSE_LOCATION_PERMISSION);
                } else {
                    if (mWeatherInfoFragment != null)
                        mWeatherInfoFragment.onPermissionDenied(AskForPermission.COARSE_LOCATION_PERMISSION);
                }
                break;
        }
    }

    @Override
    public void onFinishCapturingImage(File capturedImageFile) {
        //TODO: implement open edit fragment here
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, WeatherInfoFragment.newInstance(capturedImageFile.getPath())
                        , WeatherInfoFragment.TAG)
                .addToBackStack(HomeActivity.class.getSimpleName()).commit();
    }

    @Override
    public void shareImage(String imagePath) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType(IMAGE_TYPE);
        Uri imageUri = Uri.fromFile(new File(imagePath));
        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        startActivity(Intent.createChooser(shareIntent, getString(R.string.share_via_title)));
    }
}
