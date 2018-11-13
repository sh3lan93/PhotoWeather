package com.shalan.photoweather.network;


import com.shalan.photoweather.BuildConfig;
import com.shalan.photoweather.data.models.WeatherDataBaseModel;
import com.shalan.photoweather.network.network_listeners.BaseResponseListener;
import com.shalan.photoweather.network.response_callbacks.WeatherDataCallback;

public class NetworkService {

    private static NetworkService INSTANCE;
    private RestClientConfig restClient;
    private static final String CLIENT_ID = BuildConfig.CLIENT_ID;

    private NetworkService(){
        restClient = RestClientConfig.getInstance();
    }

    public static NetworkService getInstance(){
        if (INSTANCE == null)
            INSTANCE = new NetworkService();
        return INSTANCE;
    }

    public void getWeatherData(double lat, double lng, BaseResponseListener<WeatherDataBaseModel> listener){
        APIEndPoints endPoints = restClient.createService(APIEndPoints.class);
        endPoints.getWeatherData(lat, lng, CLIENT_ID).enqueue(new WeatherDataCallback(listener));
    }
}
