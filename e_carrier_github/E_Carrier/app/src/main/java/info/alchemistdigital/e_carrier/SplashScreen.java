package info.alchemistdigital.e_carrier;

/**
 * Created by Tapan on 11-Oct-15.
 */

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;

import info.alchemistdigital.e_carrier.activity.DriverViewActivity;
import info.alchemistdigital.e_carrier.activity.MainActivity;
import info.alchemistdigital.e_carrier.services.GetLatLongService;
import info.alchemistdigital.e_carrier.utilities.ConnectionDetector;
import info.alchemistdigital.e_carrier.utilities.Queries;

public class SplashScreen extends Activity {
    // Connection detector
    ConnectionDetector cd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        View idSplashScreen = findViewById(R.id.splashScreen);

        cd = new ConnectionDetector(SplashScreen.this);

        Queries.db = openOrCreateDatabase("E_Carrier",MODE_PRIVATE,null);

//        Queries.db.execSQL("DROP TABLE booking_service_info;");
        Queries.db.execSQL("CREATE TABLE IF NOT EXISTS booking_service_info(" +
                "enquiryId INTEGER PRIMARY KEY," +
                "userId INTEGER," +
                "creationTime INTEGER," +
                "beginningArea VARCHAR," +
                "destinationArea VARCHAR,"+
                "deliveryDateTime VARCHAR,"+
                "weight DECIMAL(10,2)," +
                "unit VARCHAR,"+
                "pickupAddress VARCHAR," +
                "deliveryAddress VARCHAR" +
                " );");

//        Queries.db.execSQL("DROP TABLE booked_enquiry;");
        Queries.db.execSQL("CREATE TABLE IF NOT EXISTS booked_enquiry(" +
                "enquiryId INTEGER PRIMARY KEY," +
                "driverData VARCHAR" +
                " );");

//        Queries.db.execSQL("DROP TABLE enquiry_start_service_status;");
        Queries.db.execSQL("CREATE TABLE IF NOT EXISTS enquiry_start_service_status(" +
                "enquiryId INTEGER PRIMARY KEY," +
                "serviceStatus VARCHAR," +
                "distance_km VARCHAR);");


        // Check if Internet present
        if (!cd.isConnectingToInternet()) {
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
                    SharedPreferences sharedPreferenceLogin = getSharedPreferences(getResources().getString(R.string.sharedPrefrence), 0);

                    final String loginSharedPref = sharedPreferenceLogin.getString(getResources().getString(R.string.boolean_login_sharedPref),"");
                    if( loginSharedPref.equals("true")){
                        String who = sharedPreferenceLogin.getString(getResources().getString(R.string.userType),"");

                        System.out.println("Who: "+who);

                        if( who.equals("customer") ){
                            startActivity(new Intent(SplashScreen.this, MainActivity.class));
                        }
                        else {
                            startActivity(new Intent(SplashScreen.this, DriverViewActivity.class));

                            // start location service
                            startService(new Intent(SplashScreen.this, GetLatLongService.class));
                        }


                        finish();
                    }
                    else {

                        Intent intent = new Intent(SplashScreen.this,AuthenticationScreen.class);
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
