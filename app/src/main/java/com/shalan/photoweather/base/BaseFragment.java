package com.shalan.photoweather.base;

import android.content.IntentFilter;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;

import com.shalan.photoweather.broadcasts.PublishConnectivityStatus;

public abstract class BaseFragment extends Fragment implements PublishConnectivityStatus.PublishResult {
    private PublishConnectivityStatus mConnectivityStatus;

    protected abstract void initPresenter();
    protected abstract void noConnectionAvailable();

    @Override
    public void onResume() {
        super.onResume();
        mConnectivityStatus = new PublishConnectivityStatus(this);
        IntentFilter intentFilter = new IntentFilter(PublishConnectivityStatus.CONNECTIVITY_STATUS_ACTION_NAME);
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mConnectivityStatus, intentFilter);
    }

    @Override
    public void onStop() {
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mConnectivityStatus);
        super.onStop();
    }

    @Override
    public void onNoConnectionAvailable() {
        noConnectionAvailable();
    }
}
