package com.example.wiprodemo.network;

import com.example.wiprodemo.model.ImageDataResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ImageServices {

    @GET("s/2iodh4vg0eortkl/facts.json")
    Call<ImageDataResponse> getAllImages();


}
