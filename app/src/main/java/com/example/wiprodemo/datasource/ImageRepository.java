package com.example.wiprodemo.datasource;


import com.example.wiprodemo.Util.ErrorCode;
import com.example.wiprodemo.Util.NetworkStatus;
import com.example.wiprodemo.model.ImageDataResponse;
import com.example.wiprodemo.model.Row;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ImageRepository implements ImageDataSource {

    private static ImageRepository ourInstance;
    private List<Row> imageList;
    private HashMap<String, List<Row>> searchImages;
    private ImageRemoteDataSource remoteDataSource;
    private HashMap<String, Boolean> isServiceLoading;

    private ImageRepository(ImageRemoteDataSource remoteDataSource) {
        imageList = new ArrayList<>();
        this.remoteDataSource = remoteDataSource;
        this.isServiceLoading = new HashMap<>();
        this.isServiceLoading.put("getImages", false);
        this.isServiceLoading.put("getImageSearch", false);


    }

    public static ImageRepository getInstance(ImageRemoteDataSource remoteDataSource) {
        if (ourInstance == null) {
            ourInstance = new ImageRepository(remoteDataSource);
        }
        return ourInstance;
    }


    @Override
    public void getImages(NetworkStatus networkStats, final LoadCallBackListener callBackListener) {

        if (networkStats.isOnline()) {
            if (!isServiceLoading.get("getImages")) {
                remoteDataSource.getImages(networkStats, new LoadCallBackListener() {
                    @Override
                    public void onLoaded(Object response) {
                        ImageDataResponse imResponse = (ImageDataResponse) response;
                        imageList.clear();
                        if (imResponse.getRows() != null && imResponse.getRows().size() > 0) {
                            imageList.addAll(imResponse.getRows());
                        }
                        callBackListener.onLoaded(imResponse);
                    }

                    @Override
                    public void onError(Object error) {
                        callBackListener.onError(error);

                    }
                });
            }
        } else {
            callBackListener.onError(ErrorCode.NETWORK_ERROR);
        }
    }

}
