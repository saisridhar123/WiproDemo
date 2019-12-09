package com.example.wiprodemo.datasource;


import com.example.wiprodemo.MainActivity;
import com.example.wiprodemo.Util.ConnectionStateMonitor;

public interface ImageDataSource {

    void getImages(ConnectionStateMonitor networkStats, LoadCallBackListener callBackListener, MainActivity mainActivity);


    interface LoadCallBackListener {
        void onLoaded(Object response);

        void onError(Object error);
    }
}
