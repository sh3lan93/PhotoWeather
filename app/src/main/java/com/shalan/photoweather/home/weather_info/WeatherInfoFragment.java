package com.shalan.photoweather.home.weather_info;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
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
import com.shalan.photoweather.data.models.WeatherDataBaseModel;
import com.shalan.photoweather.utils.AppDialogs;
import com.shalan.photoweather.utils.AskForPermission;
import com.shalan.photoweather.utils.Utils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;


public class WeatherInfoFragment extends BaseFragment implements WeatherInfoViewInteractor, Callback
        , AppDialogs.PermissionExplanationDialogListener, AskForPermission.PermissionResultListener {

    public static final String TAG = WeatherInfoFragment.class.getSimpleName();

    private static final String IMAGE_FILE_PATH = "capturedImagePath";
    private static final int SHOW = 1;
    private static final int HIDE = 2;
    private static final float DRAWING_TEXT_SIZE = 12f;
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
    private double userLat = -1;
    private double userLng = -1;
    private WeatherDataBaseModel weatherData;

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

    private void showCautionMessage(int message) {
        cautionMessage.setText(message);
        cautionMessage.setVisibility(View.VISIBLE);
    }

    private void showCautionMessage(String message) {
        cautionMessage.setText(message);
        cautionMessage.setVisibility(View.VISIBLE);
    }

    private void hideCautionMessage() {
        cautionMessage.setVisibility(View.GONE);
    }

    private void showProgress(int state) {
        if (state == SHOW) {
            if (loadingProgressBar.getVisibility() != View.VISIBLE)
                loadingProgressBar.setVisibility(View.VISIBLE);
        } else if (state == HIDE) {
            if (loadingProgressBar.getVisibility() == View.VISIBLE)
                loadingProgressBar.setVisibility(View.GONE);
        }

    }

    private void drawWeatherData(Bitmap capturedImageBitmap) {
        Bitmap newBitmap = capturedImageBitmap.copy(Bitmap.Config.ARGB_8888, true);
        FileOutputStream outputStream = null;
        Canvas canvas = new Canvas(newBitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.WHITE);
        paint.setTextSize(DRAWING_TEXT_SIZE);
//        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.OVERLAY));

//        canvas.drawBitmap(newBitmap, 0, 0, paint);
        canvas.drawText(weatherData.getName(), 10, 10, paint);

        File newModifiedCapturedImage = new File(this.capturedImagePath);
        if (!newModifiedCapturedImage.exists()) {
            try {
                newModifiedCapturedImage.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            outputStream = new FileOutputStream(newModifiedCapturedImage);
            newBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.i(TAG, "drawWeatherData: " + e.getLocalizedMessage());
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.flush();
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                loadImageFile();
            }
        }
    }

    private void loadImageFile() {
        Picasso.get().load(new File(this.capturedImagePath)).memoryPolicy(MemoryPolicy.NO_CACHE).into(this.capturedImage, new Callback() {
            @Override
            public void onSuccess() {
                Log.i(TAG, "onSuccess: ");
            }

            @Override
            public void onError(Exception e) {
                Log.i(TAG, "onError: ");
            }
        });
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
    protected void noConnectionAvailable() {
        showProgress(HIDE);
        showCautionMessage(R.string.no_internet_connection_available_message);
    }

    @Override
    protected void connectionAvailable() {
        if (this.userLat != -1 && this.userLng != -1
                && this.cautionMessage.getVisibility() == View.VISIBLE
                && this.cautionMessage.getText().toString()
                .equals(getString(R.string.no_internet_connection_available_message))
                && loadingProgressBar.getVisibility() != View.VISIBLE && this.weatherData == null) {
            hideCautionMessage();
            showProgress(SHOW);
            presenter.requestWeatherData(this.userLat, this.userLng);
        }
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
    public void publishErrorMessage(String message) {
        showProgress(HIDE);
        showCautionMessage(message);
    }

    @Override
    public void publishWeatherData(WeatherDataBaseModel baseModel) {
        this.weatherData = baseModel;
        //TODO: try to extract data and draw it on the image
        presenter.deleteExistsFile(this.capturedImagePath);
        Bitmap imageBitmap = ((BitmapDrawable) this.capturedImage.getDrawable()).getBitmap();
        drawWeatherData(imageBitmap);
    }

    @Override
    public void onGrantClicked(int permissionID) {
        presenter.forceRequestPermission(permissionID);
    }

    @Override
    public void onDeniedClicked(int permissionID) {
        showCautionMessage(R.string.location_permission_caution_message);
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
                    if (Utils.checkConnectivity(getContext())) {
                        presenter.requestWeatherData(userLat, userLng);
                    } else {
                        showProgress(HIDE);
                        showCautionMessage(R.string.no_internet_connection_available_message);
                    }
                } else {
                    Log.i(TAG, "onSuccess: location is null");
                }
            }
        });
    }

    @Override
    public void onPermissionDenied(int permissionID) {
        showCautionMessage(R.string.location_permission_caution_message);
    }

    public interface OnFragmentInteractionListener {

    }
}
