package com.shalan.photoweather.network;

import com.shalan.photoweather.data.models.WeatherDataBaseModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIEndPoints {

    @GET("data/2.5/weather")
    Call<WeatherDataBaseModel> getWeatherData(@Query("lat") double lat, @Query("lon") double lng, @Query("APPID") String clientID);
}
