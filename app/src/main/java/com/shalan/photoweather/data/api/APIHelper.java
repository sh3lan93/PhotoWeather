package com.shalan.photoweather.data.api;

import com.shalan.photoweather.data.models.WeatherDataBaseModel;
import com.shalan.photoweather.network.NetworkService;
import com.shalan.photoweather.network.network_listeners.BaseResponseListener;

public class APIHelper implements APIHelperInteractor{

    public APIHelper() {
    }

    @Override
    public void getWeatherData(double lat, double lng, BaseResponseListener<WeatherDataBaseModel> listener) {
        NetworkService.getInstance().getWeatherData(lat, lng, listener);
    }
}
