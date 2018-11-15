package com.shalan.photoweather.history.full_preview;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.shalan.photoweather.R;
import com.squareup.picasso.Picasso;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FullPreviewFragment extends Fragment {


    public static final String TAG = FullPreviewFragment.class.getSimpleName();

    private static final String IMAGE_PATH = "imagePath";
    private OnFragmentInteractionListener mListener;
    private String imagePath;

    @BindView(R.id.capturedImage)
    ImageView capturedImage;

    public FullPreviewFragment() {
        // Required empty public constructor
    }

    public static FullPreviewFragment newInstance(String imagePath) {
        FullPreviewFragment fragment = new FullPreviewFragment();
        Bundle bundle = new Bundle();
        bundle.putString(IMAGE_PATH, imagePath);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
            this.imagePath = getArguments().getString(IMAGE_PATH);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_full_preview, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (this.imagePath != null)
            Picasso.get().load(new File(this.imagePath)).into(capturedImage);
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

    public interface OnFragmentInteractionListener {

    }
}
