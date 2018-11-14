package com.shalan.photoweather.home.weather_info;

import android.util.Log;

import com.shalan.photoweather.base.BasePresenter;
import com.shalan.photoweather.data.AppDataManager;
import com.shalan.photoweather.data.models.WeatherDataBaseModel;
import com.shalan.photoweather.network.network_listeners.BaseResponseListener;
import com.shalan.photoweather.utils.Utils;

public class WeatherInfoPresenter<V extends WeatherInfoViewInteractor> extends BasePresenter<V> implements WeatherInfoPresenterInteractor<V>,BaseResponseListener<WeatherDataBaseModel> {

    public static final String TAG = WeatherInfoPresenter.class.getSimpleName();

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

    @Override
    public void requestWeatherData(double lat, double lng) {
        getDataManager().getWeatherData(lat, lng, this);
    }

    @Override
    public void getDataSuccess(WeatherDataBaseModel weatherDataBaseModel) {
        getBaseViewInteractor().publishWeatherData(weatherDataBaseModel);
    }

    @Override
    public void getDataFail(String message) {
        getBaseViewInteractor().publishErrorMessage(message);
    }
}
