package com.example.wiprodemo.datasource;


import com.example.wiprodemo.Util.NetworkStatus;

public interface ImageDataSource {

    void getImages(NetworkStatus networkStats, LoadCallBackListener callBackListener);


    interface LoadCallBackListener {
        void onLoaded(Object response);

        void onError(Object error);
    }
}
