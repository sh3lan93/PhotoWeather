package com.shalan.photoweather.network.network_listeners;

public interface BaseResponseListener<T> {
    void getDataSuccess(T t);
    void getDataFail(String message);
}
