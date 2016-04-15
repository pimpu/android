package info.alchemistdigital.e_carrier.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gcm.GCMRegistrar;

import info.alchemistdigital.e_carrier.R;
import info.alchemistdigital.e_carrier.asynctask.GCMInsertAsyncTask;

/**
 * Created by user on 12/30/2015.
 */
public class GCMInsertToServer {

    public static void register(Context context, String regId) {
        Log.i(CommonUtilities.TAG, "registering device (regId = " + regId + ")");

        SharedPreferences sharedPreferenceLogin= context.getSharedPreferences(context.getResources().getString(R.string.sharedPrefrence), 0);
        SharedPreferences.Editor editor = sharedPreferenceLogin.edit();
        editor.putString(context.getResources().getString(R.string.gcmId), regId);
        editor.commit();

        String loginId = String.valueOf(sharedPreferenceLogin.getInt(context.getResources().getString(R.string.loginId), 0));
        new extendGCMInsertAsyncTask(context,regId,loginId).execute();

    }

    /**
     * Unregister this account/device pair within the server.
     */
    public static void unregister(Context context, String regId) {
        Log.i(CommonUtilities.TAG, "unregistering device (regId = " + regId + ")");
        GCMRegistrar.setRegisteredOnServer(context, false);
    }

    private static class extendGCMInsertAsyncTask extends GCMInsertAsyncTask {
        Context context;
        public extendGCMInsertAsyncTask(Context context, String gcmId, String loginId) {
            super(context,gcmId,loginId);
            this.context=context;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if(result.contains("Error occurred!")) {
                Toast.makeText(context, result, Toast.LENGTH_LONG).show();
                return;
            }

            GCMRegistrar.setRegisteredOnServer(context, true);
        }
    }

}
