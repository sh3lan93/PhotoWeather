package com.shalan.photoweather.data.api;

import com.shalan.photoweather.data.models.WeatherDataBaseModel;
import com.shalan.photoweather.network.network_listeners.BaseResponseListener;

public interface APIHelperInteractor {
    void getWeatherData(double lat, double lng, BaseResponseListener<WeatherDataBaseModel> listener);
}
