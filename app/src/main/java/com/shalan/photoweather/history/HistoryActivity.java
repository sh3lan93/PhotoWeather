package com.shalan.photoweather.history;

import android.os.Bundle;

import com.shalan.photoweather.PhotoWeatherApp;
import com.shalan.photoweather.R;
import com.shalan.photoweather.base.BaseActivity;
import com.shalan.photoweather.data.AppDataManager;

public class HistoryActivity extends BaseActivity implements HistoryViewInteractor{


    private HistoryPresenter<HistoryViewInteractor> presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        initPresenter();
//        if (savedInstanceState == null)
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 1)
            finish();
        else
            super.onBackPressed();
    }

    @Override
    protected void initPresenter() {
        AppDataManager dataManager = ((PhotoWeatherApp)getApplicationContext()).getDataManager();
        presenter = new HistoryPresenter<>(dataManager, this);
    }
}
