package com.alchemistdigital.kissan;

import android.content.Context;
import android.content.Intent;

import com.alchemistdigital.kissan.DBHelper.DatabaseHelper;
import com.alchemistdigital.kissan.model.Enquiry;
import com.alchemistdigital.kissan.model.Item;
import com.alchemistdigital.kissan.model.Order;
import com.alchemistdigital.kissan.model.Society;
import com.alchemistdigital.kissan.sharedPrefrenceHelper.GetSharedPreferenceHelper;
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
            storeSocietyDetails(context, intent.getExtras().getString("societyDetails"));
        } else if (message.equals("downloadEnquiry")) {
            storeEnquiryDetails(context, intent.getExtras().getString("enquiryDetails"));
        } else if (message.equals("downloadOrderAtAdmin")) {
            String referenceNo = intent.getExtras().getString("referenceNo");
            String utr = intent.getExtras().getString("utr");
            String items = intent.getExtras().getString("items");
            String userId = intent.getExtras().getString("userId");

            storeOrderAtAdminSite(context, referenceNo, utr, items, userId);
        }

    }

    private void storeOrderAtAdminSite(Context context, String referenceNo, String utr, String items, String userId) {
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

            Order order = new Order(Integer.parseInt(userId), referenceNo, utr);
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
            String str_message = json.getString("eMsg");
            String str_name = json.getString("eSociety");
            String str_address = json.getString("eSocAdrs");
            String str_contact = json.getString("eSocCont");
            String str_email = json.getString("eSocEmail");
            String fileName = json.getString("eDoc");
            String oldEnquiryId = json.getString("oldEnquiryId");

            DatabaseHelper dbHelper = new DatabaseHelper(context);
            Enquiry enquiry = new Enquiry(id_enquiry, creted_at, str_ref_no, eUid, gId, repToVal, replied, str_message, str_name, str_address, str_contact, str_email, fileName);
            long enquiryId = dbHelper.insertEnquiry(enquiry);

            System.out.println("Before update stmt(GCM): "+oldEnquiryId);
            if( !oldEnquiryId.equals("0") ){
                System.out.println("inside update replied.");
                int i = dbHelper.updateEnquiryReplied(oldEnquiryId);
                System.out.println(context.getClass().getSimpleName()+"(update at GCM) : "+i);
            }

            dbHelper.closeDB();

            System.out.println("insert enquiry(GCM): " + enquiryId);


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
            Society society = new Society(soc_id, userId, soc_name, soc_contact, soc_email, soc_adrs);
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
