package info.alchemistdigital.e_carrier;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.google.android.gcm.GCMBaseIntentService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import info.alchemistdigital.e_carrier.asynctask.GetEnquiryDetails;
import info.alchemistdigital.e_carrier.utilities.CommonUtilities;
import info.alchemistdigital.e_carrier.utilities.GCMInsertToServer;
import info.alchemistdigital.e_carrier.utilities.Queries;

import static info.alchemistdigital.e_carrier.utilities.CommonUtilities.apostropheEscapeChar;
import static info.alchemistdigital.e_carrier.utilities.CommonUtilities.convertToArrayList;
import static info.alchemistdigital.e_carrier.utilities.NotificationManager.generateNotification;

/**
 * Created by user on 12/30/2015.
 */
public class GCMIntentService extends GCMBaseIntentService {

    private static final String TAG = "GCMIntentService";

    public GCMIntentService() {
        super(CommonUtilities.SENDER_ID);
    }

    @Override
    protected void onMessage(Context context, Intent intent) {
        String message = intent.getExtras().getString(CommonUtilities.EXTRA_MESSAGE);

        Queries.db = openOrCreateDatabase("E_Carrier",MODE_PRIVATE,null);

        if (message.equals("requestToNearestDriver")) {
            String enquiryDetails = intent.getExtras().getString("enquiryDetails");
            insertEnquiryData(context,enquiryDetails);
        }
        else if (message.equals("enquiryFullFill")) {

            String userId = intent.getExtras().getString("userId");
            System.out.println(userId);

            // enquiryArray comes in form of json array like ["....","....","...."];
            // seperate enquiry string data using pattern.
            String enquiry = intent.getExtras().getString("enquiryArray");
            ArrayList<String> enquiryArray = convertToArrayList(enquiry);

            String acceptingDriverProfiles = intent.getExtras().getString("acceptingDriverProfile");
            ArrayList<String> acceptingDriverProfilesArray = convertToArrayList(acceptingDriverProfiles);

            SharedPreferences sharedPreferenceLogin = getSharedPreferences(getResources().getString(R.string.sharedPrefrence), 0);
            int loginID = sharedPreferenceLogin.getInt(getResources().getString(R.string.loginId), 0);
            String userType = sharedPreferenceLogin.getString(getResources().getString(R.string.userType), "");

            if (userType.equals("driver") && Integer.valueOf(acceptingDriverProfilesArray.get(0)) == loginID) {
                generateNotification(context, "Enquiry accept for " + enquiryArray.get(1));

                // for visible start button in alert box and disable ok button for responding
                // to customer enquiry request.
                Queries.db.execSQL("INSERT INTO booked_enquiry" +
                        "(enquiryId,driverData)VALUES(" +
                        Integer.parseInt(enquiryArray.get(0)) + ",' " +
                        acceptingDriverProfilesArray.get(1) + " ( Id - " + acceptingDriverProfilesArray.get(0) + " )' );" +
                        ");");

            } else if (userType.equals("customer") && Integer.valueOf(userId) == loginID) {
//                generateNotification(context, "Enquiry handle by " + acceptingDriverProfilesArray.get(1) + " driverID: " + acceptingDriverProfilesArray.get(0));

                Queries.db.execSQL("INSERT INTO booked_enquiry" +
                        "(enquiryId,driverData)VALUES(" +
                        Integer.parseInt(enquiryArray.get(0)) + ",' " +
                        acceptingDriverProfilesArray.get(1) + " ( Id - " + acceptingDriverProfilesArray.get(0) + " )' );" +
                        ");");

            } else if (userType.equals("driver")) {
                Queries.db.execSQL("DELETE FROM booking_service_info WHERE enquiryId='" + enquiryArray.get(0) + "' AND userId='" + userId + "';");
                generateNotification(context, "Enquiry from " + enquiryArray.get(1) + " fullfill.");
            }

            CommonUtilities.displayMessage(context, "success");
        }
        else if (message.equals("afterEnquiryFullFill")) {
            String enquiryId = intent.getExtras().getString("enquiryId");
            String driverIdId = intent.getExtras().getString("driverId");

            SharedPreferences sharedPreferenceLogin = getSharedPreferences(getResources().getString(R.string.sharedPrefrence), 0);
            int loginID = sharedPreferenceLogin.getInt(getResources().getString(R.string.loginId), 0);
            String userType = sharedPreferenceLogin.getString(getResources().getString(R.string.userType), "");
            if (userType.equals("driver") && Integer.valueOf(driverIdId) == loginID) {
                Queries.db.execSQL("DELETE FROM booking_service_info WHERE enquiryId='" + enquiryId + "';");
                generateNotification(context, "Enquiry fullfill.");
            }

            CommonUtilities.displayMessage(context, "success");
        }
        else if (message.equals("afterEnquiryFullFill(ownerDriverEnquiry)")) {
            generateNotification(context, "Enquiry assign to you.");

            // notifies user
            CommonUtilities.displayMessage(context, "success");
        }
        else if(message.equals("comesInNearestEnquiryArea")){
            String enquiryIds = intent.getExtras().getString("enquiryIds");
            System.out.println(enquiryIds);

            SharedPreferences sharedPreferenceLogin = getSharedPreferences(getResources().getString(R.string.sharedPrefrence), 0);
            String userType = sharedPreferenceLogin.getString(getResources().getString(R.string.userType), "");

            if( userType.equals("driver") ){
                if(enquiryIds.length() > 0){
                    new extendsGetEnquiryDetails(context,enquiryIds).execute();
                }
            }
        }

    }

    private void insertEnquiryData(Context context, String enquiryDetails) {
        HashMap<String ,String> keyValue=CommonUtilities.convertToKeyValuePair(enquiryDetails);

        Queries.db.execSQL("INSERT INTO booking_service_info" +
                "(enquiryId,userId,creationTime," +
                "beginningArea,destinationArea,deliveryDateTime," +
                "weight,unit," +
                "pickupAddress,deliveryAddress" +
                ")VALUES" +
                "(" + Integer.valueOf("" + keyValue.get("enquiryId")) + ", " +
                Integer.valueOf("" + keyValue.get("userId")) + ", " +
                System.currentTimeMillis() + ", " +
                "'" + apostropheEscapeChar(keyValue.get("beginningArea")) + "', " +
                "'" + apostropheEscapeChar(keyValue.get("destinationArea")) + "', " +
                "'" + keyValue.get("deliveryDateTime") + "', " +
                Double.valueOf("" + keyValue.get("weight")) + ", " +
                "'" + keyValue.get("unit") + "', " +
                "'" + apostropheEscapeChar(keyValue.get("pickupAddress")) + "', " +
                "'" + apostropheEscapeChar(keyValue.get("deliveryAddress")) + "'" +
                ");");

        generateNotification(context, "New enquiry from " + keyValue.get("beginningArea"));
        CommonUtilities.displayMessage(context, "success");
    }

    @Override
    protected void onDeletedMessages(Context context, int total) {
        String message = getString(R.string.gcm_deleted, total);
        // notifies user
//        generateNotification(context, message);
    }

    @Override
    protected void onError(Context context, String errorId) {
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
//        GCMInsertToServer.unregister(context, registrationId);
    }

    private class extendsGetEnquiryDetails extends GetEnquiryDetails {
        Context context;
        public extendsGetEnquiryDetails(Context context, String enquiryIds) {
            super(context,enquiryIds);
            this.context = context;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            // check log cat fro response

            try {
                System.out.println("enquiry Response data when come in enquiry area: " + result);

                if(result.contains("Error occurred!")){
                    Toast.makeText(context, result, Toast.LENGTH_LONG).show();
                    return;
                }

                JSONObject json = new JSONObject(result);
                int success = json.getInt(CommonUtilities.TAG_SUCCESS);
                if(success == 0){
                    JSONArray jsonArray = json.getJSONArray(CommonUtilities.TAG_MESSAGE);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject c = jsonArray.getJSONObject(i);

                        if(c.getString("enquiryId").length() > 0){

                            Queries.db.execSQL("INSERT INTO booking_service_info" +
                                    "(enquiryId,userId,creationTime," +
                                    "beginningArea,destinationArea,deliveryDateTime," +
                                    "weight,unit," +
                                    "pickupAddress,deliveryAddress" +
                                    ")VALUES" +
                                    "(" + Integer.valueOf( c.getString("enquiryId") ) + ", " +
                                    Integer.valueOf( c.getString("userId")) + ", " +
                                    System.currentTimeMillis() + ", " +
                                    "'" + apostropheEscapeChar(c.getString("beginningArea")) + "', " +
                                    "'" + apostropheEscapeChar(c.getString("destinationArea")) + "', " +
                                    "'" + apostropheEscapeChar(c.getString("deliveryDateTime")) + "', " +
                                    Double.valueOf( c.getString("weight") ) + ", " +
                                    "'" + c.getString("unit") + "', " +
                                    "'" + apostropheEscapeChar(c.getString("pickupAddress")) + "', " +
                                    "'" + apostropheEscapeChar(c.getString("deliveryAddress")) + "'" +
                                    ");");
                        }

                    }

                    if( jsonArray.length() > 1 ){
                        generateNotification(context, "New enquiry from " + jsonArray.length()+ " areas." );
                    }
                    else {
                        generateNotification(context, "New enquiry from " + jsonArray.getJSONObject(0).getString("beginningArea"));
                    }

                    CommonUtilities.displayMessage(context, "success");
                }
                else {
                    String message = json.getString(CommonUtilities.TAG_MESSAGE);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
