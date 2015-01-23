package com.bloc.blocparty.Models;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Class that handles all loading/caching of images
 */
public class ImageHandler {

    private Context mContext;

    public interface ImageHandlerListener {

        public void onImageLoaded(Bitmap image);

    }

    public ImageHandler(Context context) {

        mContext = context;

    }

    public void loadImage(final String id, final String url, final ImageHandlerListener listener) {

        // check if image is in the cache
        if (isImageInCache(id)) {

            //Log.d("ImageHandler", "image is in cache");

            // load image from the cache and pass it to the listener
            listener.onImageLoaded(loadImageFromCache(id));


        } else {

            //Log.d("ImageHandler", "image not found");

            // run an async a load image from the web
            new AsyncTask<Void, Void, Bitmap>() {

                @Override
                protected Bitmap doInBackground(Void... params) {

                    //Log.d("ImageHandler", "doInBackground");

                    // load the image from the web and pass it to the onPostExecute
                    return loadImageFromWeb(url);

                }

                @Override
                protected void onPostExecute(Bitmap bitmap) {

                    //Log.d("ImageHandler", "onPostExecute");

                    // save the image to the cache
                    saveImageToCache(id,bitmap);

                    // pass the image to the listner
                    listener.onImageLoaded(bitmap);

                }
            }.execute();
        }

    }

    /**
     *  Save an image to the cache
     *
     *  @param id       The unique id of the image, will be used as the filename
     *  @param image    The bitmap image
     */
    private void saveImageToCache(String id, Bitmap image) {

        //Log.d("ImageHandler", "SaveImageToCache");

        // check that we have access to external storage
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return;
        }

        // create an output stream and turn our image into a png
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, outputStream);

        File externalCache = mContext.getExternalCacheDir();
        File cachedImageFile = new File(externalCache.getAbsolutePath() + File.separator + id);

        try {

            cachedImageFile.createNewFile();
            FileOutputStream fos = new FileOutputStream(cachedImageFile);
            fos.write(outputStream.toByteArray());
            fos.close();

        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *  Checks whether an image with the name [id] is in the cache
     *
     *  @param id      The file to check for
     */
    private boolean isImageInCache(String id) {

        //Log.d("ImageHandler", "isImageInCache");

        // check that we have access to external storage
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return false;
        }

        // create a ref to the supposed file
        String filePath = mContext.getExternalCacheDir() + File.separator + id;
        Log.d("ImageHandler", filePath);
        File file = new File(filePath);

        // return whether or not it exists
        return file.exists();

    }

    /**
     *  Loads an image from the cache with filename [id]
     *
     *  @param id     The file to load
     *  @return Bitmap
     */
    private Bitmap loadImageFromCache(String id) {

        //Log.d("ImageHandler", "loadImageFromCache");

        // check that we have access to external storage
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return null;
        }

        // create a ref to the supposed file
        String filePath = mContext.getExternalCacheDir() + File.separator + id;

        // Re-create the bitmap from the raw data saved in the cache
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;

        return BitmapFactory.decodeFile(filePath, options);

    }

    /**
     *  Loads an image from a given web address
     *
     */
    private Bitmap loadImageFromWeb(String imageAddress) {

        //Log.d("ImageHandler", "loadImageFromWeb");

        Bitmap picture = null;

        try {

            // make a URL from the address
            URL url = new URL(imageAddress);

            // try and connect to the url
            URLConnection connection = url.openConnection();

            // get the image
            InputStream inputStream = connection.getInputStream();

            picture = BitmapFactory.decodeStream(inputStream);

        }
        catch (IOException e) {
            e.printStackTrace();
        }

        // send it back to the caller
        return picture;
    }

}
