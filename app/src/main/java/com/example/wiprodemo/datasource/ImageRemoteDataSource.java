package com.example.wiprodemo.datasource;


import com.example.wiprodemo.MainActivity;
import com.example.wiprodemo.Util.ConnectionStateMonitor;
import com.example.wiprodemo.Util.ErrorCode;
import com.example.wiprodemo.model.ImageDataResponse;
import com.example.wiprodemo.network.ImageServices;
import com.example.wiprodemo.network.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ImageRemoteDataSource implements ImageDataSource {

    private static ImageRemoteDataSource imageRemoteDataSource;


    private ImageRemoteDataSource() {
    }

    public synchronized static ImageRemoteDataSource getInstance() {
        if (imageRemoteDataSource == null) {
            imageRemoteDataSource = new ImageRemoteDataSource();
        }
        return imageRemoteDataSource;
    }

    @Override
    public  void getImages(ConnectionStateMonitor networkStats, final LoadCallBackListener callBackListener, MainActivity mainActivity) {
       final ImageServices apiService = RetrofitClient.getClient().create(ImageServices.class);

        Call<ImageDataResponse> call = apiService.getAllImages();
        call.enqueue(new Callback<ImageDataResponse>() {
            @Override
            public void onResponse(Call<ImageDataResponse> call, Response<ImageDataResponse> response) {
                if (response.isSuccessful()) {
                    callBackListener.onLoaded(response.body());
                } else {
                    callBackListener.onError(ErrorCode.CONNECTION_PROBLEM);

                }
            }

            @Override
            public void onFailure(Call<ImageDataResponse> call, Throwable t) {
                callBackListener.onError(ErrorCode.CONNECTION_PROBLEM);
            }
        });
    }

}
