package com.shalan.photoweather.home.weather_info;

import com.shalan.photoweather.base.BasePresenter;
import com.shalan.photoweather.data.AppDataManager;

public class WeatherInfoPresenter<V extends WeatherInfoViewInteractor> extends BasePresenter<V> implements WeatherInfoPresenterInteractor<V> {

    public WeatherInfoPresenter(AppDataManager dataManager, V baseViewInteractor) {
        super(dataManager, baseViewInteractor);
    }

    @Override
    public void askForLocationPermission() {
        getBaseViewInteractor().requestLocationPermission();
    }

    @Override
    public void forceRequestPermission(int permissionID) {
        getBaseViewInteractor().requestPermission(permissionID);
    }
}
