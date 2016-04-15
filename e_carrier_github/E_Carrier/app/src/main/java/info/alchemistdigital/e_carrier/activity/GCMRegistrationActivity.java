package info.alchemistdigital.e_carrier.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gcm.GCMRegistrar;

import info.alchemistdigital.e_carrier.R;
import info.alchemistdigital.e_carrier.utilities.CommonUtilities;

public class GCMRegistrationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gcmregistration);

        // Make sure the device has the proper dependencies.
        GCMRegistrar.checkDevice(this);

        // Make sure the manifest was properly set - comment out this line
        // while developing the app, then uncomment it when it's ready.
        GCMRegistrar.checkManifest(this);

        // Get GCM registration id
        final String regId = GCMRegistrar.getRegistrationId(this);

        // Check if regid already presents
        if (regId.equals("")) {
            // Registration is not present, Register now with GCM
            GCMRegistrar.register(this, CommonUtilities.SENDER_ID);

            Intent intent = new Intent(GCMRegistrationActivity.this,MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            finish();
            startActivity(intent);

        }

    }

    @Override
    protected void onDestroy() {
        try {
//            unregisterReceiver(mHandleMessageReceiver);
//            GCMRegistrar.onDestroy(this);
        } catch (Exception e) {
            Log.e("UnRegisterReceiverError", "> " + e.getMessage());
        }
        super.onDestroy();
    }
}
