package com.shalan.photoweather.home;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.shalan.photoweather.PhotoWeatherApp;
import com.shalan.photoweather.R;
import com.shalan.photoweather.base.BaseActivity;
import com.shalan.photoweather.data.AppDataManager;

import butterknife.ButterKnife;

public class HomeActivity extends BaseActivity implements HomeViewInteractor{

    private HomePresenter<HomeViewInteractor> presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        initPresenter();
    }

    @Override
    protected void initPresenter() {
        AppDataManager dataManager = ((PhotoWeatherApp)getApplicationContext()).getDataManager();
        presenter = new HomePresenter<HomeViewInteractor>(dataManager, this);
    }
}
