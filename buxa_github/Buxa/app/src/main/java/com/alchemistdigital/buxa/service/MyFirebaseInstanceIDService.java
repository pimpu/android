package com.alchemistdigital.buxa.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.alchemistdigital.buxa.R;
import com.alchemistdigital.buxa.asynctask.UpdateGCMID;
import com.alchemistdigital.buxa.sharedprefrencehelper.GetSharedPreference;
import com.alchemistdigital.buxa.sharedprefrencehelper.SetSharedPreference;
import com.alchemistdigital.buxa.utilities.CommonVariables;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by Pimpu on 11/18/2016.
 */
public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = MyFirebaseInstanceIDService.class.getSimpleName();

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        // Saving reg id to shared preferences
        storeRegIdInPref(refreshedToken);

        GetSharedPreference getPreference = new GetSharedPreference(getApplicationContext());
        int uId = getPreference.getLoginId(getApplicationContext().getResources().getString(R.string.loginId));

        if( uId != 0 ) {

            // Notify UI that registration has completed, so the progress indicator can be hidden.
            Intent registrationComplete = new Intent(CommonVariables.REGISTRATION_COMPLETE);
            registrationComplete.putExtra("token", refreshedToken);
            LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);

            // sending reg id to your server
            sendRegistrationToServer(refreshedToken, uId);
        }
    }

    private void sendRegistrationToServer(String token, int uId) {
        // sending gcm token to server
        new UpdateGCMID(getApplicationContext(), token, uId);
    }

    private void storeRegIdInPref(String token) {
        Log.e(TAG, "storeRegIdInPref: " + token);

        SetSharedPreference setSharedPreference = new SetSharedPreference(getApplicationContext());
        setSharedPreference.setFCMRegId(getResources().getString(R.string.FCM_RegId), token);
    }
}
