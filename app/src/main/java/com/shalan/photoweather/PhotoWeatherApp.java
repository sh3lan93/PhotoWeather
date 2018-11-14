package com.shalan.photoweather;

import android.app.Application;
import android.content.IntentFilter;

import com.shalan.photoweather.broadcasts.ConnectivityBroadCast;
import com.shalan.photoweather.data.AppDataManager;

public class PhotoWeatherApp extends Application {

    private AppDataManager dataManager;
    private ConnectivityBroadCast connectivityBroadCast;

    @Override
    public void onCreate() {
        super.onCreate();
        dataManager = new AppDataManager(this);
        initConnectivityBroadCast();
    }

    public AppDataManager getDataManager() {
        return dataManager;
    }

    private void initConnectivityBroadCast(){

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityBroadCast.ACTION_NAME);
        this.connectivityBroadCast = new ConnectivityBroadCast();
        this.registerReceiver(connectivityBroadCast, intentFilter);

    }

    @Override
    public void onTerminate() {
        this.unregisterReceiver(connectivityBroadCast);
        super.onTerminate();
    }
}
