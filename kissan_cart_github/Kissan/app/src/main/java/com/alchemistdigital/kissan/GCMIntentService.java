package com.alchemistdigital.kissan;

import android.content.Context;
import android.content.Intent;

import com.alchemistdigital.kissan.DBHelper.DatabaseHelper;
import com.alchemistdigital.kissan.model.Enquiry;
import com.alchemistdigital.kissan.model.Item;
import com.alchemistdigital.kissan.model.OBP;
import com.alchemistdigital.kissan.model.Order;
import com.alchemistdigital.kissan.model.Society;
import com.alchemistdigital.kissan.sharedPrefrenceHelper.GetSharedPreferenceHelper;
import com.alchemistdigital.kissan.sharedPrefrenceHelper.SetSharedPreferenceHelper;
import com.alchemistdigital.kissan.utilities.CommonUtilities;
import com.alchemistdigital.kissan.utilities.CommonVariables;
import com.google.android.gcm.GCMBaseIntentService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by user on 3/8/2016.
 */
public class GCMIntentService extends GCMBaseIntentService {
    public GCMIntentService() {
        super(CommonVariables.SENDER_ID);
    }

    @Override
    protected void onMessage(Context context, Intent intent) {
        String message = intent.getExtras().getString(CommonVariables.EXTRA_MESSAGE);
        if (message.equals("downloadSocietyAtAdmin")) {

            // comes from create_society_byAndroid.php page.
            storeSocietyDetails(context, intent.getExtras().getString("societyDetails"));

        } else if (message.equals("downloadEnquiry")) {

            storeEnquiryDetails(context, intent.getExtras().getString("enquiryDetails"));

        } else if ( message.equals("downloadOrderAtAdmin") || message.equals("downloadOfflineOrderAtAdmin") ) {

            String referenceNo = intent.getExtras().getString("referenceNo");
            String utr = intent.getExtras().getString("utr");
            String items = intent.getExtras().getString("items");
            String userId = intent.getExtras().getString("userId");
            String creationTime = intent.getExtras().getString("creationtime");

            storeOrderAtAdminSite(context, referenceNo, utr, items, userId,creationTime);

        } else if (message.equals("updateSocietyAtAdmin")){

           updateSocietyDetails(context, intent.getExtras().getString("changedSocietyDetails"));

        } else if (message.equals("downloadOfflineInsertEnquiry")){

            storeOfflineEnquiryDetails(context, intent.getExtras().getString("enquiryDetails"));

        } else if(message.equals("downloadOfflineUpdateEnquiry")) {

            updateOfflineEnquiry(context,intent.getExtras().getString("enquiryDetails"));

        } else if(message.equals("downloadOfflineSocietyAtAdmin")) {

            storeOfflineSociety(context,intent.getExtras().getString("societyDetails"));

        } else if (message.equals("updateOfflineSocietyAtAdmin")) {

            updateOfflineSociety(context, intent.getExtras().getString("changedSocietyDetails"));

        } else if (message.equals("deletedOBPByAdmin")) {

            SetSharedPreferenceHelper setPreference = new SetSharedPreferenceHelper(context);
            // it store false value of user for purpose of user is logging.
            setPreference.setLoginPreference(getResources().getString(R.string.boolean_login_sharedPref), "false");

        } else if (message.equals("updateOfflineOBP") || message.equals("updateOBPAtAdmin") ) {
            updateObpDetails(context, intent.getExtras().getString("changedOBPDetails"));
        }


        CommonUtilities.displayMessage(context, "success");
//        generateNotification(context, "success");
    }

    private void updateObpDetails(Context context, String changedOBPDetails) {
        JSONObject json = null;
        try {
            json = new JSONObject(changedOBPDetails);
            String gId = json.getString("fromGid");

            if(gId.equals("1")) {
                SetSharedPreferenceHelper setPreference = new SetSharedPreferenceHelper(context);
                // it store false value of user for purpose of user is logging.
                setPreference.setLoginPreference(getResources().getString(R.string.boolean_login_sharedPref), "false");
            }

            int userID_serverId = json.getInt("userID_serverId");
            String obp_name = json.getString("obp_name");
            String obp_store_name = json.getString("obp_store_name");
            String obp_email_id = json.getString("obp_email_id");
            String obp_email_passowrd = json.getString("obp_email_passowrd");
            String obp_contact_number = json.getString("obp_contact_number");
            String obp_address = json.getString("obp_address");
            int obp_pincode = json.getInt("obp_pincode");
            String obp_city = json.getString("obp_city");
            String obp_state = json.getString("obp_state");
            String obp_country = json.getString("obp_country");
            int obp_status = json.getInt("obp_status");

            OBP obp = new OBP( userID_serverId, obp_name, obp_store_name, obp_email_id, obp_email_passowrd,
                    obp_contact_number, obp_address, obp_pincode, obp_city, obp_state, obp_country, obp_status );

            DatabaseHelper dbHelper = new DatabaseHelper(context);
            long l = dbHelper.updateObpData(obp);
            dbHelper.closeDB();

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void updateOfflineSociety(Context context, String changedSocietyDetails) {
        JSONObject json = null;
        try {
            json = new JSONObject(changedSocietyDetails);

            int soc_id = json.getInt("soc_id");
            String old_name = json.getString("old_name");
            String soc_name = json.getString("soc_name");
            String soc_contact = json.getString("soc_contact");
            String soc_email = json.getString("soc_email");
            String soc_adrs = json.getString("soc_adrs");
            int soc_status = json.getInt("soc_status");

            Society society = new Society();

            society.setSoc_name(soc_name);
            society.setSoc_contact(soc_contact);
            society.setSoc_email(soc_email);
            society.setSoc_adrs(soc_adrs);
            society.setServerId(soc_id);
            society.setSoc_status(soc_status);


            DatabaseHelper dbhelper = new DatabaseHelper(context);
            dbhelper.updateSociety(society);
            dbhelper.closeDB();

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void storeOfflineSociety(Context context, String societyDetails) {
        try {
            JSONObject json = new JSONObject(societyDetails);

            int soc_id = json.getInt("soc_id");
            String soc_name = json.getString("soc_name");
            String soc_contact = json.getString("soc_contact");
            String soc_email = json.getString("soc_email");
            String soc_adrs = json.getString("soc_adrs");

            GetSharedPreferenceHelper getPreference = new GetSharedPreferenceHelper(context);
            int userId = getPreference.getUserIdPreference(context.getResources().getString(R.string.userId));

            DatabaseHelper dbHelper = new DatabaseHelper(context);
            Society society = new Society(soc_id, userId, soc_name, soc_contact, soc_email, soc_adrs,1);
            long societyId = dbHelper.insertSociety(society);
            dbHelper.closeDB();

            System.out.println("insert offline society(GCM) : " + societyId);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void updateOfflineEnquiry(Context context, String enquiryDetails) {
        try {
            JSONObject json = new JSONObject(enquiryDetails);

            String enquiryId = json.getString("enquiry_id");
            String replied = json.getString("replied");

            DatabaseHelper dbhelper = new DatabaseHelper(context);
            int i = dbhelper.updateEnquiryReplied(enquiryId, replied);
            dbhelper.closeDB();
            System.out.println("(update at GCM offline) : " + i);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void storeOfflineEnquiryDetails(Context context, String enquiryDetails) {
        try {
            JSONObject json = new JSONObject(enquiryDetails);

            // Storing each json item in variable
            int id_enquiry = json.getInt("id");
            String creted_at = json.getString("eTime");
            String str_ref_no = json.getString("eRef");
            int eUid = json.getInt("uID");
            int gId = json.getInt("gID");
            int repToVal = json.getInt("repTo");
            int replied = json.getInt("replied");
            int socID = json.getInt("SocID");
            int scid = json.getInt("scid");
            int prod_id = json.getInt("prod_id");
            String qty = json.getString("qty");

            DatabaseHelper dbHelper = new DatabaseHelper(context);
            Enquiry enquiry = new Enquiry(id_enquiry, creted_at, str_ref_no, eUid, gId, repToVal,
                    replied, socID, scid, prod_id, qty, 1);

            long enquiryId = dbHelper.insertEnquiry(enquiry);

            dbHelper.closeDB();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void updateSocietyDetails(Context context, String changedSocietyDetails) {
        JSONObject json = null;
        try {
            json = new JSONObject(changedSocietyDetails);

            int soc_id = json.getInt("soc_id");
            String old_name = json.getString("old_name");
            String soc_name = json.getString("soc_name");
            String soc_contact = json.getString("soc_contact");
            String soc_email = json.getString("soc_email");
            String soc_adrs = json.getString("soc_adrs");
            int status = json.getInt("status");

            Society society = new Society();

            society.setSoc_name(soc_name);
            society.setSoc_contact(soc_contact);
            society.setSoc_email(soc_email);
            society.setSoc_adrs(soc_adrs);
            society.setServerId(soc_id);
            society.setSoc_status(status);


            DatabaseHelper dbhelper = new DatabaseHelper(context);
            dbhelper.updateSociety(society);
            dbhelper.closeDB();

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void storeOrderAtAdminSite(Context context, String referenceNo, String utr, String items, String userId, String creationTime) {
        try {
            JSONArray jsonItem = new JSONArray(items);
            DatabaseHelper dbhelper = new DatabaseHelper(context);

            for (int i = 0; i < jsonItem.length(); i++) {
                JSONObject obj = jsonItem.getJSONObject(i);

                Item item = new Item( referenceNo,
                                        obj.getString("itemName"),
                                        obj.getInt("itemQuantity"),
                                        obj.getString("itemPrice"),
                                        obj.getString("itemTotalAmount") );

                dbhelper.insertItem(item);

            }

            Order order = new Order(Integer.parseInt(userId), referenceNo, utr, creationTime, 1);
            dbhelper.insertOrder(order);

            dbhelper.closeDB();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void storeEnquiryDetails(Context context, String enquiryDetails) {
        try {
            JSONObject json = new JSONObject(enquiryDetails);

            // Storing each json item in variable
            int id_enquiry = json.getInt("id");
            String creted_at = json.getString("eTime");
            String str_ref_no = json.getString("eRef");
            int eUid = json.getInt("uID");
            int gId = json.getInt("gID");
            int repToVal = json.getInt("repTo");
            int replied = json.getInt("replied");
            int selectedSocietyServerId = json.getInt("SocID");
            int selectedSubcategoryServerId = json.getInt("scid");
            int selectedProductServerId = json.getInt("prod_id");
            String str_product_qty = json.getString("qty");
            String oldEnquiryId = json.getString("oldEnquiryId");

            DatabaseHelper dbHelper = new DatabaseHelper(context);

            Enquiry enquiry = new Enquiry(id_enquiry, creted_at, str_ref_no, eUid, gId,
                    repToVal, replied, selectedSocietyServerId, selectedSubcategoryServerId, selectedProductServerId,
                    str_product_qty,1 );

            long enquiryId = dbHelper.insertEnquiry(enquiry);

            if( ! oldEnquiryId.equals("0") ) {
                int i = dbHelper.updateEnquiryReplied(oldEnquiryId, "1");
                System.out.println(context.getClass().getSimpleName()+"(update at GCM) : "+i);
            }

            dbHelper.closeDB();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void storeSocietyDetails(Context context, String societyDetails) {

        try {
            JSONObject json = new JSONObject(societyDetails);

            int soc_id = json.getInt("soc_id");
            String soc_name = json.getString("soc_name");
            String soc_contact = json.getString("soc_contact");
            String soc_email = json.getString("soc_email");
            String soc_adrs = json.getString("soc_adrs");

            GetSharedPreferenceHelper getPreference = new GetSharedPreferenceHelper(context);
            int userId = getPreference.getUserIdPreference(context.getResources().getString(R.string.userId));

            DatabaseHelper dbHelper = new DatabaseHelper(context);
            Society society = new Society(soc_id, userId, soc_name, soc_contact, soc_email, soc_adrs,1);
            long societyId = dbHelper.insertSociety(society);
            dbHelper.closeDB();

            System.out.println("insert society(GCM) : " + societyId);

        } catch (JSONException e) {
            e.printStackTrace();
        }
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
