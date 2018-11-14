package com.shalan.photoweather.history.history_listing;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shalan.photoweather.PhotoWeatherApp;
import com.shalan.photoweather.R;
import com.shalan.photoweather.base.BaseFragment;
import com.shalan.photoweather.data.AppDataManager;
import com.shalan.photoweather.data.realm_models.HistoryModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class HistoryListFragment extends BaseFragment implements HistoryListFragmentViewInteractor{

    public static final String TAG = HistoryListFragment.class.getSimpleName();

    private OnFragmentInteractionListener mListener;

    private HistoryListFragmentPresenter<HistoryListFragmentViewInteractor> presenter;

    @BindView(R.id.historyRecycler)
    RecyclerView historyRecycler;
    private List<HistoryModel> historyList;

    public HistoryListFragment() {
        // Required empty public constructor
    }


    public static HistoryListFragment newInstance() {
        HistoryListFragment fragment = new HistoryListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_history_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        historyRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        historyRecycler.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        this.historyList = presenter.getHistories();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initPresenter();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    protected void initPresenter() {
        AppDataManager dataManager = ((PhotoWeatherApp)getContext().getApplicationContext()).getDataManager();
        presenter = new HistoryListFragmentPresenter<>(dataManager, this);
    }

    @Override
    protected void noConnectionAvailable() {

    }

    @Override
    protected void connectionAvailable() {

    }


    public interface OnFragmentInteractionListener {

    }
}
