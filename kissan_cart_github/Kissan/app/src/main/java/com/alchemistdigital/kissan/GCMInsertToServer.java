package com.alchemistdigital.kissan;

import android.content.Context;
import android.util.Log;

import com.alchemistdigital.kissan.asynctask.UpdateGCMRegIdAsyncTask;
import com.alchemistdigital.kissan.sharedPrefrenceHelper.GetSharedPreferenceHelper;
import com.google.android.gcm.GCMRegistrar;

/**
 * Created by user on 3/8/2016.
 */
public class GCMInsertToServer {
    public static void register(Context context, String regId) {
        Log.i(context.getClass().getSimpleName(), "registering device (regId = " + regId + ")");

        GetSharedPreferenceHelper getPreference = new GetSharedPreferenceHelper(context);
        int uId = getPreference.getUserIdPreference(context.getResources().getString(R.string.userId));
        String strUID = String.valueOf(uId);

        new UpdateGCMRegIdAsyncTask(context,regId,strUID).execute();

    }

    public static void unregister(Context context, String regId) {
        Log.i(context.getClass().getSimpleName(), "unregistering device (regId = " + regId + ")");
        GCMRegistrar.setRegisteredOnServer(context, false);
    }
}
