package com.shalan.photoweather.network.response_callbacks;

import com.shalan.photoweather.data.models.WeatherDataBaseModel;
import com.shalan.photoweather.network.network_listeners.BaseResponseListener;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherDataCallback implements Callback<WeatherDataBaseModel> {
    private BaseResponseListener<WeatherDataBaseModel> listener;

    public WeatherDataCallback(BaseResponseListener<WeatherDataBaseModel> listener) {
        this.listener = listener;
    }

    @Override
    public void onResponse(Call<WeatherDataBaseModel> call, Response<WeatherDataBaseModel> response) {
        if (response.code() == 200){
            if (response.body() != null)
                listener.getDataSuccess(response.body());
        }else {
            listener.getDataFail(response.message());
        }
    }

    @Override
    public void onFailure(Call<WeatherDataBaseModel> call, Throwable t) {
        listener.getDataFail(t.getLocalizedMessage());
    }
}
