package com.shalan.photoweather.utils;

import android.content.Context;
import android.net.ConnectivityManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.realm.RealmResults;

public class Utils {

    public static boolean checkConnectivity(Context context){
        ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return mConnectivityManager.getActiveNetworkInfo() != null;
    }

    public static boolean isMorning(){
        Calendar calendar = Calendar.getInstance();
        if (calendar.get(Calendar.HOUR_OF_DAY) >= 6 && calendar.get(Calendar.HOUR_OF_DAY) <= 18)
            return true;
        else
            return false;
    }

    public static <S> List<S> getList(RealmResults<S> realmResults){
        List<S> list = new ArrayList<>();
        list.addAll(realmResults);
        return list;
    }
}
