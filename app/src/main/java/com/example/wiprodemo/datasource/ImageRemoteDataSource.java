package com.example.wiprodemo.datasource;


import com.example.wiprodemo.Util.ErrorCode;
import com.example.wiprodemo.Util.NetworkStatus;
import com.example.wiprodemo.model.ImageDataResponse;
import com.example.wiprodemo.network.ImageServices;
import com.example.wiprodemo.network.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ImageRemoteDataSource implements ImageDataSource {

    private static ImageRemoteDataSource ourInstance;
    private ImageServices apiService;

    private ImageRemoteDataSource() {
    }

    public static ImageRemoteDataSource getInstance() {
        if (ourInstance == null) {
            ourInstance = new ImageRemoteDataSource();
        }
        return ourInstance;
    }

    @Override
    public void getImages(NetworkStatus networkStats, final LoadCallBackListener callBackListener) {
        apiService = RetrofitClient.getClient().create(ImageServices.class);

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
