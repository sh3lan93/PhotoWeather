package com.shalan.photoweather.home.weather_info;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.shalan.photoweather.PhotoWeatherApp;
import com.shalan.photoweather.R;
import com.shalan.photoweather.base.BaseFragment;
import com.shalan.photoweather.data.AppDataManager;

import butterknife.BindView;
import butterknife.ButterKnife;


public class WeatherInfoFragment extends BaseFragment implements WeatherInfoViewInteractor {

    public static final String TAG = WeatherInfoFragment.class.getSimpleName();

    private static final String IMAGE_FILE_PATH = "capturedImagePath";
    private OnFragmentInteractionListener mListener;
    private String capturedImagePath;
    private WeatherInfoPresenter<WeatherInfoViewInteractor> presenter;

    @BindView(R.id.capturedImage)
    ImageView capturedImage;

    public WeatherInfoFragment() {
        // Required empty public constructor
    }

    public static WeatherInfoFragment newInstance(String imageFilePath) {
        WeatherInfoFragment fragment = new WeatherInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putString(IMAGE_FILE_PATH, imageFilePath);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.capturedImagePath = getArguments().getString(IMAGE_FILE_PATH);
            Log.i(TAG, "onCreate: " + this.capturedImagePath);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_weather_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
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
        AppDataManager dataManager = ((PhotoWeatherApp) (getContext().getApplicationContext())).getDataManager();
        presenter = new WeatherInfoPresenter<WeatherInfoViewInteractor>(dataManager, this);
    }

    public interface OnFragmentInteractionListener {

    }
}
