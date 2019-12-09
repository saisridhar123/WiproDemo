package com.example.wiprodemo.Util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

public class ConnectionStateMonitor extends LiveData<Boolean> {

    private ConnectivityManager.NetworkCallback networkCallback = null;
    private ConnectivityManager connectivityManager;

    public ConnectionStateMonitor(Context context) {
        connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        networkCallback = new NetworkCallback(this);
    }

    @Override
    protected void onActive() {
        super.onActive();
        updateConnection();
        connectivityManager.registerDefaultNetworkCallback(networkCallback);
    }

    @Override
    protected void onInactive() {
        super.onInactive();
        connectivityManager.unregisterNetworkCallback(networkCallback);
    }

    class NetworkCallback extends ConnectivityManager.NetworkCallback {

        private ConnectionStateMonitor mConnectionStateMonitor;

        NetworkCallback(ConnectionStateMonitor connectionStateMonitor) {
            mConnectionStateMonitor = connectionStateMonitor;
        }

        @Override
        public void onAvailable(@NonNull Network network) {
            mConnectionStateMonitor.postValue(true);
        }

        @Override
        public void onLost(@NonNull Network network) {
            mConnectionStateMonitor.postValue(false);
        }
    }

    private void updateConnection() {
        if (connectivityManager != null) {
            NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
            if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) postValue(true);
            else postValue(false);
        }
    }
}
