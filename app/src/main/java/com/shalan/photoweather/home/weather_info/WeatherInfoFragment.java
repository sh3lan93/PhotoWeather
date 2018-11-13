package com.shalan.photoweather.home.weather_info;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.shalan.photoweather.PhotoWeatherApp;
import com.shalan.photoweather.R;
import com.shalan.photoweather.base.BaseFragment;
import com.shalan.photoweather.data.AppDataManager;
import com.shalan.photoweather.utils.AppDialogs;
import com.shalan.photoweather.utils.AskForPermission;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;


public class WeatherInfoFragment extends BaseFragment implements WeatherInfoViewInteractor, Callback
        , AppDialogs.PermissionExplanationDialogListener, AskForPermission.PermissionResultListener {

    public static final String TAG = WeatherInfoFragment.class.getSimpleName();

    private static final String IMAGE_FILE_PATH = "capturedImagePath";
    private static final int SHOW = 1;
    private static final int HIDE = 2;
    @BindView(R.id.capturedImage)
    ImageView capturedImage;
    @BindView(R.id.cautionMessage)
    TextView cautionMessage;
    @BindView(R.id.loadingProgressBar)
    ProgressBar loadingProgressBar;
    private OnFragmentInteractionListener mListener;
    private String capturedImagePath;
    private WeatherInfoPresenter<WeatherInfoViewInteractor> presenter;
    private FusedLocationProviderClient mFusedLocationClient;
    private double userLat;
    private double userLng;

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
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initPresenter();
        Picasso.get().load(new File(this.capturedImagePath)).into(this.capturedImage, this);
    }

    private void showCautionMessage() {
        cautionMessage.setText(R.string.location_permission_caution_message);
        cautionMessage.setVisibility(View.VISIBLE);
    }

    private void showProgress(int state) {
        if (state == SHOW)
            if (loadingProgressBar.getVisibility() != View.VISIBLE)
                loadingProgressBar.setVisibility(View.VISIBLE);
        else if (state == HIDE)
            if (loadingProgressBar.getVisibility() == View.VISIBLE)
                loadingProgressBar.setVisibility(View.GONE);

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

    @Override
    public void onSuccess() {
        presenter.askForLocationPermission();
    }

    @Override
    public void onError(Exception e) {
        e.printStackTrace();
        Log.i(TAG, "onError: " + e.getLocalizedMessage());
    }

    @Override
    public void requestLocationPermission() {
        AskForPermission.getInstance(getActivity(), this, this)
                .requestPermission(AskForPermission.COARSE_LOCATION_PERMISSION);
    }

    @Override
    public void requestPermission(int permissionID) {
        AskForPermission.getInstance(getActivity(), this, this)
                .forceRequestPermission(permissionID);
    }

    @Override
    public void onGrantClicked(int permissionID) {
        presenter.forceRequestPermission(permissionID);
    }

    @Override
    public void onDeniedClicked(int permissionID) {
        showCautionMessage();
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onPermissionGranted(int permissionID) {
        //this is the entry point for getting user location
        //TODO: Getting user last known location == current location
        //TODO: Show waiting dialog and call weather data api
        showProgress(SHOW);
        this.mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());
        this.mFusedLocationClient.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    WeatherInfoFragment.this.userLat = location.getLatitude();
                    WeatherInfoFragment.this.userLng = location.getLongitude();
                    Log.i(TAG, "onSuccess: " + userLat + "\t" + userLng);
                } else {
                    Log.i(TAG, "onSuccess: location is null");
                }
            }
        });
    }

    @Override
    public void onPermissionDenied(int permissionID) {
        showCautionMessage();
    }

    public interface OnFragmentInteractionListener {

    }
}
