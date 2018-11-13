package com.shalan.photoweather.data;

import android.content.Context;

import com.shalan.photoweather.data.api.APIHelper;
import com.shalan.photoweather.data.models.WeatherDataBaseModel;
import com.shalan.photoweather.network.network_listeners.BaseResponseListener;

public class AppDataManager implements AppDataManagerInteractor{

    private Context context;
    private APIHelper apiHelper;

    public AppDataManager(Context context) {
        this.context = context;
        this.apiHelper = new APIHelper();
    }

    @Override
    public void getWeatherData(double lat, double lng, BaseResponseListener<WeatherDataBaseModel> listener) {
        apiHelper.getWeatherData(lat, lng, listener);
    }
}
