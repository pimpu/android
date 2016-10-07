package com.alchemistdigital.buxa;

import android.content.Context;
import android.content.Intent;

import com.alchemistdigital.buxa.utilities.CommonVariables;
import com.google.android.gcm.GCMBaseIntentService;

/**
 * Created by user on 10/6/2016.
 */

public class GCMIntentService extends GCMBaseIntentService {
    public GCMIntentService() {
        super(CommonVariables.SENDER_ID);
    }

    @Override
    protected void onMessage(Context context, Intent intent) {
        String message = intent.getExtras().getString(CommonVariables.EXTRA_MESSAGE);
    }

    @Override
    protected void onDeletedMessages(Context context, int total) {
        String message = getString(R.string.gcm_deleted, total);
        // notifies user
//        generateNotification(context, message);
    }

    @Override
    protected void onError(Context context, String s) {

    }

    @Override
    protected boolean onRecoverableError(Context context, String errorId) {
        // log message
        return super.onRecoverableError(context, errorId);
    }

    @Override
    protected void onRegistered(Context context, String registrationId) {
        GCMInsertToServer.register(context, registrationId);
    }

    @Override
    protected void onUnregistered(Context context, String registrationId) {
        GCMInsertToServer.unregister(context, registrationId);
    }
}
