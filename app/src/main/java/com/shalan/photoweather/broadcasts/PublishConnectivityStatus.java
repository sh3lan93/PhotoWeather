package com.shalan.photoweather.broadcasts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class PublishConnectivityStatus extends BroadcastReceiver {

    public static final String CONNECTIVITY_STATUS_ACTION_NAME = "com.shalan.mohamed.connectivity.status";
    public static final String CONNECTIVITY_AVAILABLE_KEY = "connectivityStatus";
    private PublishResult publishResult;

    public PublishConnectivityStatus(PublishResult publishResult) {
        this.publishResult = publishResult;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(CONNECTIVITY_STATUS_ACTION_NAME) && !intent.getBooleanExtra(CONNECTIVITY_AVAILABLE_KEY, false)) {
            publishResult.onNoConnectionAvailable();
        }
    }

    public interface PublishResult {
        void onNoConnectionAvailable();
    }
}
