package com.shalan.photoweather.home.weather_info;

import com.shalan.photoweather.base.BasePresenterInteractor;

public interface WeatherInfoPresenterInteractor<V extends WeatherInfoViewInteractor> extends BasePresenterInteractor<V> {
    void askForLocationPermission();
    void forceRequestPermission(int permissionID);
}
