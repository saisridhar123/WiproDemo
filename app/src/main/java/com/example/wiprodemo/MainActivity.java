package com.example.wiprodemo;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Toast;

import com.example.wiprodemo.Util.AppNetworkStatus;
import com.example.wiprodemo.datasource.ImageRemoteDataSource;
import com.example.wiprodemo.datasource.ImageRepository;
import com.example.wiprodemo.mainactivitymodule.ImageListAdapter;
import com.example.wiprodemo.mainactivitymodule.MainContract;
import com.example.wiprodemo.mainactivitymodule.MainPresenter;
import com.example.wiprodemo.model.ImageDataResponse;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class MainActivity extends AppCompatActivity implements MainContract.View {

    private SwipeRefreshLayout swipeContainer;
    private MainPresenter presenter;
    private RecyclerView recyclerView;
    private ImageListAdapter imageListAdapter;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dialog = new ProgressDialog(this);
        presenter = new MainPresenter(this, ImageRepository.getInstance(ImageRemoteDataSource.getInstance()));
        presenter.start();
        presenter.getImages(new AppNetworkStatus(this));
    }

    @Override
    public void showLoadingIndicator(boolean active) {
        if (active) dialog.show();
        else dialog.dismiss();
    }

    @Override
    public void showError(String errorMsg) {
        Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showImages(ImageDataResponse imageDataResponse) {
        getSupportActionBar().setTitle(imageDataResponse.getTitle());
        if (imageListAdapter == null) {
            imageListAdapter = new ImageListAdapter(imageDataResponse.getRows());
            LinearLayoutManager manager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(manager);
            recyclerView.setAdapter(imageListAdapter);
        } else {
            imageListAdapter.setItemModelList(imageDataResponse.getRows());
        }

    }

    @Override
    public String getErrorString(int resourceId) {
        return getResources().getString(resourceId);
    }


    @Override
    public void showNetworkError(boolean active) {
        if (active)
            Toast.makeText(this, getResources().getString(R.string.connection_problem), Toast.LENGTH_LONG).show();
    }

    @Override
    public void setUpUI() {
        swipeContainer = findViewById(R.id.swipeContainer);
        recyclerView = findViewById(R.id.rvItems);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.getImages(new AppNetworkStatus(MainActivity.this));
                swipeContainer.setRefreshing(false);
            }
        });
    }

    @Override
    public void setPresenter(MainContract.Presenter presenter) {
        this.presenter = (MainPresenter) presenter;
    }
}
