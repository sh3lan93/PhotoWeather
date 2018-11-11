package com.shalan.photoweather.home;

import android.os.Bundle;

import com.shalan.photoweather.PhotoWeatherApp;
import com.shalan.photoweather.R;
import com.shalan.photoweather.base.BaseActivity;
import com.shalan.photoweather.data.AppDataManager;
import com.shalan.photoweather.home.camera.CameraFragment;

import butterknife.ButterKnife;

public class HomeActivity extends BaseActivity implements HomeViewInteractor {

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
}
