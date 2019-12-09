package com.example.wiprodemo.mainactivitymodule;


import com.example.wiprodemo.BasePresenter;
import com.example.wiprodemo.BaseView;
import com.example.wiprodemo.MainActivity;
import com.example.wiprodemo.Util.ConnectionStateMonitor;
import com.example.wiprodemo.model.ImageDataResponse;

public interface MainContract {

    interface View extends BaseView<Presenter> {

        /**
         * show/hide loading indicatore
         *
         * @param active status
         */
        void showLoadingIndicator(boolean active);

        /**
         * show error on snackbar
         *
         * @param errorMsg msg to display
         */
        void showError(String errorMsg);

        /**
         * display image
         *
         * @param imageDataResponse image data
         */
        void showImages(ImageDataResponse imageDataResponse);

        /**
         * gets the error string
         *
         * @param resourceId requested string
         * @return
         */
        String getErrorString(int resourceId);

        /**
         * show/hide network error layout
         *
         * @param active status
         */
        void showNetworkError(boolean active);

        /**
         * setup and init UI element
         */
        void setUpUI();

    }

    interface Presenter extends BasePresenter {

        /**
         * gets the image list
         *
         * @param networkStatus network interface
         */
        void getImages(ConnectionStateMonitor networkStatus, MainActivity mainActivity, boolean isPullToRefresh);

        /**
         * gets filtered error
         *
         * @param errorCode error Id
         * @return
         */
        String filterError(Object errorCode);

    }

}
