package com.example.wiprodemo.mainactivitymodule;


import com.example.wiprodemo.R;
import com.example.wiprodemo.Util.ErrorCode;
import com.example.wiprodemo.Util.NetworkStatus;
import com.example.wiprodemo.datasource.ImageDataSource;
import com.example.wiprodemo.datasource.ImageRepository;
import com.example.wiprodemo.model.ImageDataResponse;

public class MainPresenter implements MainContract.Presenter {

    private MainContract.View view;
    private ImageRepository repository;

    public MainPresenter(MainContract.View view, ImageRepository repository) {
        this.view = view;
        this.repository = repository;
        this.view.setPresenter(this);
    }

    @Override
    public void getImages(NetworkStatus networkStatus) {
        view.showLoadingIndicator(true);
        repository.getImages(networkStatus, new ImageDataSource.LoadCallBackListener() {
            @Override
            public void onLoaded(Object response) {
                view.showLoadingIndicator(false);
                view.showImages((ImageDataResponse) response);
            }

            @Override
            public void onError(Object error) {
                view.showLoadingIndicator(false);
                if ((int) error == ErrorCode.NETWORK_ERROR) {
                    view.showNetworkError(true);
                } else {
                    view.showError(filterError(error));
                }
            }
        });
    }

    @Override
    public String filterError(Object errorCode) {
        String errorMsg;
        switch ((int) errorCode) {
            case ErrorCode.CONNECTION_PROBLEM:
                errorMsg = view.getErrorString(R.string.connection_problem);
                break;
            case ErrorCode.NO_RESULT:
                errorMsg = view.getErrorString(R.string.no_result);
                break;
            default:
                errorMsg = view.getErrorString(R.string.connection_problem);
                break;
        }
        return errorMsg;
    }

    @Override
    public void start() {
        view.setUpUI();
    }
}
