package com.shalan.photoweather.data;

import android.content.Context;

import com.shalan.photoweather.data.api.APIHelper;
import com.shalan.photoweather.data.models.WeatherDataBaseModel;
import com.shalan.photoweather.data.realm.RealmHelper;
import com.shalan.photoweather.data.realm_models.HistoryModel;
import com.shalan.photoweather.network.network_listeners.BaseResponseListener;

import io.realm.RealmResults;

public class AppDataManager implements AppDataManagerInteractor{

    private Context context;
    private APIHelper apiHelper;
    private RealmHelper realmHelper;

    public static final String REALM_FILE_NAME = "PhototWeather-realm.realm";
    public static final int REALM_SCHEMA_VERSION = 1;

    public AppDataManager(Context context) {
        this.context = context;
        this.apiHelper = new APIHelper();
        this.realmHelper = new RealmHelper();
    }

    @Override
    public void getWeatherData(double lat, double lng, BaseResponseListener<WeatherDataBaseModel> listener) {
        apiHelper.getWeatherData(lat, lng, listener);
    }

    @Override
    public void insertNewHistoryRecord(String name, String imagePath) {
        realmHelper.insertNewHistoryRecord(name, imagePath);
    }

    @Override
    public RealmResults<HistoryModel> getAllHistoryRecords() {
        return realmHelper.getAllHistoryRecords();
    }
}
