package com.shalan.photoweather.utils;

import android.content.Context;
import android.net.ConnectivityManager;

public class Utils {

    public static boolean checkConnectivity(Context context){
        ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return mConnectivityManager.getActiveNetworkInfo() != null;
    }
}
