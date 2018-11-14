package com.shalan.photoweather.history;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.shalan.photoweather.PhotoWeatherApp;
import com.shalan.photoweather.R;
import com.shalan.photoweather.base.BaseActivity;
import com.shalan.photoweather.data.AppDataManager;
import com.shalan.photoweather.history.history_listing.HistoryListFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HistoryActivity extends BaseActivity implements HistoryViewInteractor
        , HistoryListFragment.OnFragmentInteractionListener {


    private HistoryPresenter<HistoryViewInteractor> presenter;

    @BindView(R.id.main_toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        ButterKnife.bind(this);
        initPresenter();
        if (savedInstanceState == null)
            getSupportFragmentManager().beginTransaction().replace(R.id.container
                    , HistoryListFragment.newInstance(), HistoryListFragment.TAG)
                    .addToBackStack(HistoryActivity.class.getSimpleName()).commit();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 1)
            finish();
        else
            super.onBackPressed();
    }

    @Override
    protected void initPresenter() {
        AppDataManager dataManager = ((PhotoWeatherApp)getApplicationContext()).getDataManager();
        presenter = new HistoryPresenter<>(dataManager, this);
    }
}
