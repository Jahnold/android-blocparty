package com.bloc.blocparty.Fragments;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bloc.blocparty.R;
import com.bloc.blocparty.Views.TouchImageView;

/**
 * Fragment for displaying full screen image which can be pinch/zoomed
 */
public class PhotoViewFragment extends Fragment {

    private Bitmap mImage;

    public PhotoViewFragment() {}


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        LinearLayout v = (LinearLayout) inflater.inflate(R.layout.fragment_photo_view, container, false);

        // get a ref to the TouchImageView
        TouchImageView imageView = (TouchImageView) v.findViewById(R.id.touch_image);

        if (mImage != null) {

            imageView.setImageBitmap(mImage);

        }

        return v;
    }

    public void setImage(Bitmap image) {

        mImage = image;

    }
}
