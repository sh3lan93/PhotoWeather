package com.shalan.photoweather.history.history_listing;

import com.shalan.photoweather.base.BasePresenterInteractor;
import com.shalan.photoweather.data.realm_models.HistoryModel;

import java.util.List;

public interface HistoryListFragmentPresenterInteractor<V extends HistoryListFragmentViewInteractor>
        extends BasePresenterInteractor<V> {

    List<HistoryModel> getHistories();
}
