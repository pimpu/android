package com.alchemistdigital.kissan.asynctask;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.alchemistdigital.kissan.utilities.CommonUtilities;
import com.alchemistdigital.kissan.utilities.CommonVariables;

import java.io.InputStream;

/**
 * Created by user on 4/1/2016.
 */
public class StoreServerImageAtMemory extends AsyncTask<String, String, Bitmap> {

    Context context;
    String fileName;
    public StoreServerImageAtMemory(Context context, String fileName) {
        this.context = context;
        this.fileName = fileName;
    }

    @Override
    protected Bitmap doInBackground(String... params) {

        String urldisplay = CommonVariables.File_DOWNLOAD_URL + fileName;
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return mIcon11;

    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        CommonUtilities.store_Png_InSdcard(context,bitmap,fileName);
    }
}
