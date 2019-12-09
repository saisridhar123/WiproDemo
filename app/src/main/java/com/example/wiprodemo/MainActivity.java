package com.example.wiprodemo;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.wiprodemo.Util.ConnectionStateMonitor;
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
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        presenter = new MainPresenter(this, ImageRepository.getInstance(ImageRemoteDataSource.getInstance()));
        presenter.start();
        presenter.getImages(new ConnectionStateMonitor(this),this,true);
    }

    @Override
    public void showLoadingIndicator(boolean active) {
        if (active) progressBar.setVisibility(View.VISIBLE);
        else progressBar.setVisibility(View.GONE);
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
        progressBar = findViewById(R.id.progress_bar);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.getImages(new ConnectionStateMonitor(MainActivity.this),MainActivity.this,false);
                swipeContainer.setRefreshing(false);
            }
        });
    }

    @Override
    public void setPresenter(MainContract.Presenter presenter) {
        this.presenter = (MainPresenter) presenter;
    }
}
