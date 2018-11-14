package com.shalan.photoweather.home.weather_info;

import com.shalan.photoweather.base.BaseViewInteractor;
import com.shalan.photoweather.data.models.WeatherDataBaseModel;

public interface WeatherInfoViewInteractor extends BaseViewInteractor {
    void requestLocationPermission();
    void requestPermission(int permissionID);
    void publishErrorMessage(String message);
    void publishWeatherData(WeatherDataBaseModel baseModel);
}
