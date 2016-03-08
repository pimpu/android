package com.alchemistdigital.kissan;

/**
 * Created by Tapan on 11-Oct-15.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.alchemistdigital.kissan.activities.AdminPanel;
import com.alchemistdigital.kissan.activities.MainActivity;
import com.alchemistdigital.kissan.sharedPrefrenceHelper.GetSharedPreferenceHelper;

import static com.alchemistdigital.kissan.utilities.CommonUtilities.isConnectingToInternet;

public class SplashScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        View idSplashScreen = findViewById(R.id.id_xmlSplashScreen);

        // Check if Internet present
        if (!isConnectingToInternet(SplashScreen.this)) {
            // Internet Connection is not present
            Snackbar.make(idSplashScreen, "No internet connection !", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Retry", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            onCreate(null);
                        }
                    }).show();
            // stop executing code by return
            return;
        }
        else{
            Thread timerThread = new Thread(){
                public void run(){
                try{
                    sleep(2000);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }finally{

                    // made sharedprefrence
                    GetSharedPreferenceHelper getPrefrence = new GetSharedPreferenceHelper(SplashScreen.this);

                    // get boolean value of login sharedPreference for checking if user
                    // if already logged in or not
                    String loginSharedPref =getPrefrence.getLoginPreference(getResources().getString(R.string.boolean_login_sharedPref));

                    if( loginSharedPref.equals("true")){
                        String who = getPrefrence.getUserTypePreference(getResources().getString(R.string.userType));

                        System.out.println("Who: "+who);

                        // check user is admin or obp
                        // on the bases of preference value.
                        if( who.equals("obp") ){
                            startActivity(new Intent(SplashScreen.this, MainActivity.class));
                        }
                        else {
                            startActivity(new Intent(SplashScreen.this, AdminPanel.class));
                        }

                        finish();
                    }
                    else {

                        Intent intent = new Intent(SplashScreen.this, Login.class);
                        startActivity(intent);
                        finish();
                    }
                }
                }
            };
            timerThread.start();
        }
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        finish();
    }

}
