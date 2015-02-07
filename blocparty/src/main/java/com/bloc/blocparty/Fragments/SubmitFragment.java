package com.bloc.blocparty.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bloc.blocparty.CameraActivity;
import com.bloc.blocparty.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *  Fragment for submitting image to FB/Twitter
 */
public class SubmitFragment extends Fragment {

    private ImageView mImageView;

    public SubmitFragment() {}


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_submit, container,false);

        // get a ref to the image view then set the image
        mImageView = (ImageView) v.findViewById(R.id.image_to_submit);

        // get a ref to the edit text
        EditText comment = (EditText) v.findViewById(R.id.et_comment);

        // get a ref to the submit button
        Button submit = (Button) v.findViewById(R.id.btn_submit);

        // set up the click listener on the button
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // post to FB

                // post to Twitter

                // return to Feed

            }
        });

        // get a photo
        takePhoto();

        return v;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        // check if this is the result we're looking for
        if (requestCode == 1111 && resultCode == Activity.RESULT_OK) {

            addPhotoToGallery();

            CameraActivity cameraActivity = (CameraActivity) getActivity();
            setFullImageFromFilePath(cameraActivity.getCurrentPhotoPath(), mImageView);
        }

    }

    protected void addPhotoToGallery() {

        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        CameraActivity activity = (CameraActivity)getActivity();
        File f = new File(activity.getCurrentPhotoPath());
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.getActivity().sendBroadcast(mediaScanIntent);

    }

    private void takePhoto() {

        // check that there is a camera
        PackageManager pm = getActivity().getPackageManager();

        if (!pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {

            Toast.makeText(getActivity(), "No Camera Found", Toast.LENGTH_SHORT).show();
            return;
        }

        // create a new image capture intent
        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // tell the intent we want the camera activity to deal with the results
        CameraActivity cameraActivity = ((CameraActivity) getActivity());
        takePhotoIntent.resolveActivity(cameraActivity.getPackageManager());

        // create an empty file for our photo to go into
        File photoFile = null;
        try {
            photoFile = createImageFile();
        }
        catch (IOException e) { e.printStackTrace(); }

        // if that worked continue
        if (photoFile != null) {

            Uri fileUri = Uri.fromFile(photoFile);
            cameraActivity.setCapturedImageURI(fileUri);
            cameraActivity.setCurrentPhotoPath(fileUri.getPath());
            takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, cameraActivity.getCapturedImageURI());
            startActivityForResult(takePhotoIntent, 1111);

        }
    }



    private File createImageFile() throws IOException {

        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        CameraActivity activity = (CameraActivity) getActivity();
        activity.setCurrentPhotoPath("file:" + image.getAbsolutePath());
        return image;
    }

    private void setFullImageFromFilePath(String imagePath, ImageView imageView) {

        // Get the dimensions of the View
        int targetW = imageView.getWidth();
        int targetH = imageView.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(imagePath, bmOptions);
        imageView.setImageBitmap(bitmap);

    }
}
