package com.example.wiprodemo.mainactivitymodule;

import android.content.Context;

import com.example.wiprodemo.MainActivity;
import com.example.wiprodemo.R;
import com.example.wiprodemo.Util.ConnectionStateMonitor;
import com.example.wiprodemo.datasource.ImageRepository;
import com.example.wiprodemo.model.ImageDataResponse;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MainPresenterTest {
    private MainPresenter mainPresenter;

    @Mock
    ImageDataResponse imageDataResponse;

    @Mock
    ConnectionStateMonitor connectionStateMonitor;
    @Mock
    private ImageRepository imageRepository;

    @Mock
    private MainContract.View mainView;

    @Mock
    MainActivity mainActivity;

    @Mock
    private Context context;

    @Captor
    private ArgumentCaptor<ImageRepository.LoadCallBackListener> loadPostsCallbackCaptor;


    @Before
    public void setupCardsPresenter() {
        MockitoAnnotations.initMocks(this);
        // Get a reference to the class under test
        mainPresenter = new MainPresenter(mainView, imageRepository);

    }


    @Test
    public void createPresenter_setsThePresenterToView() {
        // Get a reference to the class under test
        mainPresenter = new MainPresenter(mainView, imageRepository);
        // Then the presenter is set to the view
        verify(mainView).setPresenter(mainPresenter);
    }


    @Test
    public void shouldSetUpUiCalled_WhenStartCalledInPresenter() {
       mainPresenter.start();

       verify(mainView).setUpUI();
    }

    @Test
    public void showImageList_whenApiGotSuccess() {
        // make call
        mainPresenter.getImages(connectionStateMonitor,mainActivity,true);

        // Callback is captured and invoked with stubbed cards
        verify(imageRepository).getImages(eq(connectionStateMonitor),loadPostsCallbackCaptor.capture(),eq(mainActivity) );
        loadPostsCallbackCaptor.getValue().onLoaded(imageDataResponse);

        // Then progress indicator is hidden posts are shown in UI
        InOrder inOrder = Mockito.inOrder(mainView);
        inOrder.verify(mainView).showLoadingIndicator(true);
        inOrder.verify(mainView).showLoadingIndicator(false);
        verify(mainView).showImages(imageDataResponse);
    }

    @Test
    public void showNetworkError_whenApiGotFailed() {

        // make call
        mainPresenter.getImages(connectionStateMonitor,mainActivity,true);

        // Callback is captured and invoked with stubbed cards
        verify(imageRepository).getImages(eq(connectionStateMonitor),loadPostsCallbackCaptor.capture(),eq(mainActivity) );
        loadPostsCallbackCaptor.getValue().onError(100);

        // Then progress indicator is hidden posts are shown in UI
        InOrder inOrder = Mockito.inOrder(mainView);
        inOrder.verify(mainView).showLoadingIndicator(true);
        inOrder.verify(mainView).showLoadingIndicator(false);
        verify(mainView).showNetworkError(true);
    }


    @Test
    public void showConnectionError_whenNetworkGotFailed() {

        when(mainView.getErrorString(R.string.connection_problem)).thenReturn("connection problem");
        // make call
        mainPresenter.getImages(connectionStateMonitor,mainActivity,true);

        // Callback is captured and invoked with stubbed cards
        verify(imageRepository).getImages(eq(connectionStateMonitor),loadPostsCallbackCaptor.capture(),eq(mainActivity) );
        loadPostsCallbackCaptor.getValue().onError(200);

        // Then progress indicator is hidden posts are shown in UI
        InOrder inOrder = Mockito.inOrder(mainView);
        inOrder.verify(mainView).showLoadingIndicator(true);
        inOrder.verify(mainView).showLoadingIndicator(false);
        verify(mainView).showError("connection problem");
    }
    @Test
    public void showNoResultError_whenDataNotGotFetched() {

        when(mainView.getErrorString(R.string.no_result)).thenReturn("no result");
        // make call
        mainPresenter.getImages(connectionStateMonitor,mainActivity,true);

        // Callback is captured and invoked with stubbed cards
        verify(imageRepository).getImages(eq(connectionStateMonitor),loadPostsCallbackCaptor.capture(),eq(mainActivity) );
        loadPostsCallbackCaptor.getValue().onError(300);

        // Then progress indicator is hidden posts are shown in UI
        InOrder inOrder = Mockito.inOrder(mainView);
        inOrder.verify(mainView).showLoadingIndicator(true);
        inOrder.verify(mainView).showLoadingIndicator(false);
        verify(mainView).showError("no result");
    }


}