package info.alchemistdigital.e_carrier.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gcm.GCMRegistrar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import info.alchemistdigital.e_carrier.R;
import info.alchemistdigital.e_carrier.adapter.Book_Service_Adapter;
import info.alchemistdigital.e_carrier.asynctask.SentEnquiryConformReply;
import info.alchemistdigital.e_carrier.asynctask.ServiceCompleteAsyncTask;
import info.alchemistdigital.e_carrier.asynctask.LogoutAsyncTask;
import info.alchemistdigital.e_carrier.AuthenticationScreen;
import info.alchemistdigital.e_carrier.model.Book_Service_Item;
import info.alchemistdigital.e_carrier.services.GetLatLongService;
import info.alchemistdigital.e_carrier.utilities.CommonUtilities;
import info.alchemistdigital.e_carrier.utilities.ConnectionDetector;
import info.alchemistdigital.e_carrier.utilities.DateHelper;
import info.alchemistdigital.e_carrier.utilities.Queries;
import info.alchemistdigital.e_carrier.utilities.RecyclerViewListener;
import info.alchemistdigital.e_carrier.utilities.WakeLocker;

public class DriverViewActivity extends AppCompatActivity{

    private Toolbar mToolbar;
    private RecyclerView book_service_RecyclerView;
    public RecyclerView.Adapter book_service_Adapter;
    public static List<Book_Service_Item> data;
    int bookingId = 0;
    Button btnConform;
    View driverView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_view);

        driverView = findViewById(R.id.idDriverViewActivity);

        mToolbar = (Toolbar) findViewById(R.id.toolbarOnDriverView);
        View viewById = findViewById(R.id.empty_booking_data);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // set the toolbar title
        getSupportActionBar().setTitle("");

        book_service_RecyclerView = (RecyclerView)findViewById(R.id.book_service_details_recycler);

        List<Book_Service_Item> bookingEnquiryData = getData();
        if(bookingEnquiryData.size() <= 0){
            viewById.setVisibility(View.VISIBLE);
            book_service_RecyclerView.setVisibility(View.GONE);
        }
        else {
            viewById.setVisibility(View.GONE);
            book_service_RecyclerView.setVisibility(View.VISIBLE);
            bookingEnquiryRecycler(bookingEnquiryData);
        }


        // Make sure the device has the proper dependencies.
        GCMRegistrar.checkDevice(this);

        // Make sure the manifest was properly set - comment out this line
        // while developing the app, then uncomment it when it's ready.
        GCMRegistrar.checkManifest(this);

        // Get GCM registration id
        final String regId = GCMRegistrar.getRegistrationId(this);

        registerReceiver(mHandleMessageReceiver, new IntentFilter(
                CommonUtilities.DISPLAY_MESSAGE_ACTION));

        if (regId.equals("")) {
            // Registration is not present, Register now with GCM
            GCMRegistrar.register(getApplicationContext(), CommonUtilities.SENDER_ID);
        }


    }

    private void bookingEnquiryRecycler(List<Book_Service_Item> bookingEnquiryData) {

        // Inflating view layout
        book_service_Adapter = new Book_Service_Adapter(getApplicationContext(), bookingEnquiryData );
        book_service_RecyclerView.setAdapter(book_service_Adapter);
        book_service_RecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        book_service_RecyclerView.addOnItemTouchListener(
                new RecyclerViewListener.RecyclerItemClickListener(getApplicationContext(), new RecyclerViewListener.RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, final int position) {

                        bookingId = data.get(position).getBookingId();

                        // get data from bookid in booking_service_info
                        Cursor c = Queries.db.rawQuery("SELECT * FROM booking_service_info where enquiryId='" + bookingId + "'", null);
                        c.moveToFirst();

                        String picupAdd = c.getString(c.getColumnIndex("pickupAddress"));
                        String deliveryAdd = c.getString(c.getColumnIndex("deliveryAddress"));
                        String deliveryDate = DateHelper.convertToString(Long.parseLong(c.getString(c.getColumnIndex("deliveryDateTime"))));
                        String deliveryWeightUnit = c.getDouble(c.getColumnIndex("weight")) + " " + c.getString(c.getColumnIndex("unit"));

                        // Alert Dialog
                        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(DriverViewActivity.this);
                        LayoutInflater inflater = DriverViewActivity.this.getLayoutInflater();
                        View dialogView = inflater.inflate(R.layout.driver_view_alert, null);
                        dialogBuilder.setView(dialogView);

                        TextView textView_pickup = (TextView) dialogView.findViewById(R.id.textview_pickup_address);
//                        TextView textView_delivery = (TextView) dialogView.findViewById(R.id.textview_delivery_address);
                        TextView textView_date = (TextView) dialogView.findViewById(R.id.textview_delivery_Date);
                        TextView textView_WeightUnit = (TextView) dialogView.findViewById(R.id.textview_weightUnit);
                        btnConform = (Button) dialogView.findViewById(R.id.conformOrder);
                        Button btnCancel = (Button) dialogView.findViewById(R.id.cancelOrder);

                        textView_pickup.setText(picupAdd);
//                        textView_delivery.setText(deliveryAdd);
                        textView_date.setText(deliveryDate);
                        textView_WeightUnit.setText(deliveryWeightUnit);

                        // show ok or start/stop button according to
                        // booked_enquiry table data using enquiry is
                        // if table data not found with enquiry id then
                        // button text not showing start/stop button

                        Cursor bookedEnquiryCursor= Queries.db.rawQuery("SELECT * FROM booked_enquiry WHERE enquiryId="+bookingId+";", null);
                        if(bookedEnquiryCursor!=null && bookedEnquiryCursor.getCount()>0){

                            bookedEnquiryCursor.moveToFirst();
                            do {
                                if(bookedEnquiryCursor!=null && bookedEnquiryCursor.getCount() > 0){
                                    int enquiryId = bookedEnquiryCursor.getInt(bookedEnquiryCursor.getColumnIndex("enquiryId"));

                                    Cursor serviceStatusCursor= Queries.db.rawQuery("SELECT * FROM enquiry_start_service_status WHERE enquiryId="+enquiryId+";", null);
                                    serviceStatusCursor.moveToFirst();
                                    do {

                                        if(serviceStatusCursor!=null && serviceStatusCursor.getCount() > 0){
                                            btnConform.setText("stop service");
                                        } else {
                                            btnConform.setText("start service");
                                        }

                                    } while (serviceStatusCursor.moveToNext());

                                }
                            } while (bookedEnquiryCursor.moveToNext());
                        }

                        final AlertDialog alertDialog = dialogBuilder.create();
                        alertDialog.show();

                        btnConform.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ConnectionDetector cd=new ConnectionDetector(DriverViewActivity.this);
                                String btnText = btnConform.getText().toString();
                                SharedPreferences sharedPreferenceLogin = getSharedPreferences(getResources().getString(R.string.sharedPrefrence), 0);
                                SharedPreferences.Editor editor = sharedPreferenceLogin.edit();
                                switch (btnText){
                                    case "Ok":
                                        int driverId = sharedPreferenceLogin.getInt(getResources().getString(R.string.loginId), 0);

                                        if (!cd.isConnectingToInternet()) {
                                            // Internet Connection is not present
                                            Snackbar.make(driverView, "No internet connection !", Snackbar.LENGTH_LONG)
                                                    .setAction("Action", null).show();
                                            // stop executing code by return
                                            return;
                                        }
                                        else{
                                            new SentEnquiryConformReply(DriverViewActivity.this,bookingId, driverId).execute();
                                        }
                                        alertDialog.dismiss();

                                        break;

                                    case "start service":
                                        alertDialog.dismiss();

//                                        startService(new Intent(DriverViewActivity.this, DistanceCalculator.class));

                                        Queries.db.execSQL("INSERT INTO enquiry_start_service_status" +
                                                "(enquiryId,serviceStatus)VALUES(" +
                                                bookingId + ",'start' " +
                                                ");");


                                        editor.putString(getResources().getString(R.string.distanceBtwnLatLong), "");
                                        editor.commit();

                                        book_service_Adapter.notifyItemChanged(position);

                                        break;

                                    case "stop service":
//                                        String distanceCovered = sharedPreferenceLogin.getString(getResources().getString(R.string.distanceBtwnLatLong), "");
//                                        System.out.println("Distance(Km)[DriverView Activity]: " + distanceCovered);
//                                        if(distanceCovered.length() <= 0){
//                                            distanceCovered = "0";
//                                        }

                                        alertDialog.dismiss();
                                        if (!cd.isConnectingToInternet()) {
                                            // Internet Connection is not present
                                            Snackbar.make(driverView, "No internet connection !", Snackbar.LENGTH_LONG)
                                                    .setAction("Action", null).show();
                                            // stop executing code by return
                                            return;
                                        }
                                        else{
                                            new extendServiceCompleteAsyncTask(DriverViewActivity.this,bookingId,position).execute();
                                        }

                                        break;
                                }
                            }
                        });

                        btnCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alertDialog.dismiss();
                            }
                        });
                    }
                })
        );

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.driver_view_menu, menu);
        return true;
//        return super.onCreateOptionsMenu(menu);
    }

    public void onLogoutItemClick(MenuItem item) {
        SharedPreferences sharedPreferenceLogin = getSharedPreferences(getResources().getString(R.string.sharedPrefrence), 0);
        int driverId = sharedPreferenceLogin.getInt(getResources().getString(R.string.loginId), 0);

        ConnectionDetector cd=new ConnectionDetector(DriverViewActivity.this);
        if (!cd.isConnectingToInternet()) {
            // Internet Connection is not present
            Snackbar.make(driverView, "No internet connection !", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            // stop executing code by return
            return;
        }
        else{
            new extendClassLogoutAsyncTask(DriverViewActivity.this,driverId).execute();
        }
    }

    @Override
    protected void onDestroy() {
        try {
            unregisterReceiver(mHandleMessageReceiver);
            GCMRegistrar.onDestroy(getApplicationContext());
        } catch (Exception e) {
            Log.e("UnRegisterReceiverError", "> " + e.getMessage());
        }
        super.onDestroy();
    }

    /**
     * Receiving push messages
     * */
    private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String newMessage = intent.getExtras().getString(CommonUtilities.EXTRA_MESSAGE);
            // Waking up mobile if it is sleeping
            WakeLocker.acquire(getApplicationContext());

            if(newMessage.equals("success")){
                if(getData().size() == 1){
                    finish();
                    startActivity(getIntent());
                }
                else {
                    bookingEnquiryRecycler(getData());
                }
            }
            // message come from GetLatLongService service class when gps is unavailable
            else if(newMessage.equals("gpsDisable")){
                if( bookingId != 0){
                    Queries.db.execSQL("DELETE FROM enquiry_start_service_status WHERE enquiryId=" + bookingId + ";");
                    btnConform.setText("start service");
                }
            }
            // Releasing wake lock
            WakeLocker.release();
        }
    };


    public static List<Book_Service_Item> getData() {
        data = new ArrayList<>();

        Cursor c=Queries.db.rawQuery("SELECT * FROM booking_service_info", null);
        if(c == null){
            return  data;
        }

        if (c.moveToFirst()) {

            do{
                String date = DateHelper.convertToString( Long.parseLong( c.getString(c.getColumnIndex("deliveryDateTime")) ) );
                Book_Service_Item bookItem = new Book_Service_Item();
                bookItem.setFromArea(c.getString(c.getColumnIndex("beginningArea")));
                bookItem.setToArea(c.getString(c.getColumnIndex("destinationArea")));
                bookItem.setDate(date);
                bookItem.setBookingId(c.getInt(c.getColumnIndex("enquiryId")));
                data.add(bookItem);
            }while(c.moveToNext());
        }
        return data;
    }

    @Override
    public void onBackPressed() {

        new AlertDialog.Builder(this)
                .setIcon(R.drawable.logo)
                .setTitle("Closing E-Carrier")
                .setMessage("Are you sure you want to close this app?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences sharedPreferenceLogin=getSharedPreferences(getResources().getString(R.string.sharedPrefrence), 0);
                        String loginSharedPref = sharedPreferenceLogin.getString(getResources().getString(R.string.boolean_login_sharedPref), "");
                        if(loginSharedPref.equals("true")  )
                        {
                            finish();
                        }
                        finish();
                    }

                })
                .setNegativeButton("No", null)
                .show();

//        super.onBackPressed();
    }

    // this did because finish() not found in LogoutAsyncTask.
    private class extendClassLogoutAsyncTask extends LogoutAsyncTask {

        public extendClassLogoutAsyncTask(Context applicationContext, int userId) {
            super(applicationContext, userId);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            // check log cat fro response

            try {
                Log.d("Logout Response", result.toString());

                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);

                if(result.contains("Error occurred!")){
                    Toast.makeText(DriverViewActivity.this, result, Toast.LENGTH_LONG).show();
                    return;
                }

                JSONObject json = new JSONObject(result);

                // check for success tag
                String message = json.getString(CommonUtilities.TAG_MESSAGE);
                int success = json.getInt(CommonUtilities.TAG_SUCCESS);
                if(success == 0){

                    SharedPreferences sharedPreferenceLogin=getSharedPreferences(getResources().getString(R.string.sharedPrefrence), 0);
                    SharedPreferences.Editor editor = sharedPreferenceLogin.edit();
                    editor.putString(getResources().getString(R.string.boolean_login_sharedPref), "false");
                    editor.commit();

                    // start location service
                    stopService(new Intent(DriverViewActivity.this, GetLatLongService.class));

                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    Intent gotoLogin = new Intent(getApplicationContext(),AuthenticationScreen.class);
                    gotoLogin.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    finish();
                    startActivity(gotoLogin);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class extendServiceCompleteAsyncTask extends ServiceCompleteAsyncTask {
        int position;
        public extendServiceCompleteAsyncTask(DriverViewActivity driverViewActivity, int bookingId, int position) {
            super(driverViewActivity,bookingId);
            this.position=position;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            // check log cat fro response

            try {
                System.out.println("Service Complete Response: " + result.toString());

                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);

                if(result.contains("Error occurred!")) {
                    Toast.makeText(DriverViewActivity.this, result, Toast.LENGTH_LONG).show();
                    return;
                }

                JSONObject json = new JSONObject(result);

                // check for success tag
                String message = json.getString(CommonUtilities.TAG_MESSAGE);
                int success = json.getInt(CommonUtilities.TAG_SUCCESS);
                if(success == 0){
                    Queries.db.execSQL("DELETE FROM enquiry_start_service_status WHERE enquiryId=" + bookingId + ";");

                    SharedPreferences sharedPreferenceLogin = getSharedPreferences(getResources().getString(R.string.sharedPrefrence), 0);
                    SharedPreferences.Editor editor = sharedPreferenceLogin.edit();
                    editor.putString(getResources().getString(R.string.distanceBtwnLatLong), "");
                    editor.commit();

//                    stopService(new Intent(DriverViewActivity.this,DistanceCalculator.class));

                    book_service_Adapter.notifyItemChanged(position);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
}