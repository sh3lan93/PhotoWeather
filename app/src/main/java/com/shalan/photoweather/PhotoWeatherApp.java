package com.shalan.photoweather;

import android.app.Application;

import com.shalan.photoweather.data.AppDataManager;

public class PhotoWeatherApp extends Application {

    private AppDataManager dataManager;

    @Override
    public void onCreate() {
        super.onCreate();
        dataManager = new AppDataManager(this);
    }

    public AppDataManager getDataManager() {
        return dataManager;
    }
}
