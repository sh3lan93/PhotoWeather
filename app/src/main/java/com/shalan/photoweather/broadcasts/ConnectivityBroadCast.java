package com.shalan.photoweather.broadcasts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.v4.content.LocalBroadcastManager;

public class ConnectivityBroadCast extends BroadcastReceiver {

    public static final String ACTION_NAME = ConnectivityManager.CONNECTIVITY_ACTION;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ACTION_NAME)){
            if (intent.hasExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY)){
                //no connection
                sendConnectivityStatusBroadcast(context, false);
            }else {
                //connection
                sendConnectivityStatusBroadcast(context, true);
            }
        }
    }

    private void sendConnectivityStatusBroadcast(Context context, boolean isConnectivityAvailable){
        Intent intent = new Intent(PublishConnectivityStatus.CONNECTIVITY_STATUS_ACTION_NAME);
        intent.putExtra(PublishConnectivityStatus.CONNECTIVITY_AVAILABLE_KEY, isConnectivityAvailable);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }
}
