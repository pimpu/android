package com.alchemistdigital.kissan.asynctask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.alchemistdigital.kissan.DBHelper.DatabaseHelper;
import com.alchemistdigital.kissan.activities.Edit_Vendor_Details;
import com.alchemistdigital.kissan.model.Vendor;
import com.alchemistdigital.kissan.utilities.AndroidMultiPartEntity;
import com.alchemistdigital.kissan.utilities.CommonVariables;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * Created by user on 5/21/2016.
 */
public class UpdateVendorAsyncTask extends AsyncTask<String, String, String>{

    private ProgressDialog pDialog;
    private Context context;
    private String str_vendor_name;
    private String str_vendor_contact;
    private String str_vendor_email;
    private String str_vendor_address;
//    private String oldVendorName;
    private int serverId;
    private int vendor_status;
    private int localId;

    public UpdateVendorAsyncTask(Context context,
                                 String str_vendor_name,
                                 String str_vendor_contact,
                                 String str_vendor_email,
                                 String str_vendor_address,
//                                 String oldVendorName,
                                 int serverId,
                                 int vendor_status,
                                 int localId ) {
        this.context = context;
        this.str_vendor_name = str_vendor_name;
        this.str_vendor_contact = str_vendor_contact;
        this.str_vendor_email = str_vendor_email;
        this.str_vendor_address = str_vendor_address;
//        this.oldVendorName = oldVendorName;
        this.serverId = serverId;
        this.vendor_status = vendor_status;
        this.localId = localId;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        ((Activity)context).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);

        pDialog = new ProgressDialog(context);
        pDialog.setMessage("updating ...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    @Override
    protected String doInBackground(String... params) {
        String responseString = null;

        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(CommonVariables.UPDATE_VENDOR_DETAIL_SERVER_URL);

        try {
            AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                    new AndroidMultiPartEntity.ProgressListener() {

                        @Override
                        public void transferred(long num) {
//                            publishProgress((int) ((num / (float) totalSize) * 100));
                        }
                    });

            // Adding file data to http body
            entity.addPart("name", new StringBody(str_vendor_name));
            entity.addPart("contact", new StringBody(str_vendor_contact));
            entity.addPart("email", new StringBody(str_vendor_email));
            entity.addPart("address", new StringBody(str_vendor_address));
            entity.addPart("id", new StringBody( String.valueOf(serverId) ));
            entity.addPart("status", new StringBody( String.valueOf(vendor_status) ));
//            entity.addPart("old_name", new StringBody(oldVendorName));

            httppost.setEntity(entity);

            // Making server call
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity r_entity = response.getEntity();

            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                // Server response
                responseString = EntityUtils.toString(r_entity);
            } else {
                responseString = "Error occurred! Http Status Code: "
                        + statusCode;
            }

        } catch (ClientProtocolException e) {
            responseString = "Error occurred! "+e.toString();
        } catch (IOException e) {
            responseString = "Error occurred! "+e.toString();
        }

        return responseString;
    }

    @Override
    protected void onPostExecute(String result) {
        pDialog.dismiss();

        ((Activity)context).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);

        Log.d("update vendor Data", result.toString());

        if(result.contains("Error occurred!")) {
            Toast.makeText(context, result, Toast.LENGTH_LONG).show();
            return;
        }

        Vendor vendor = new Vendor();
        vendor.setVendor_name(str_vendor_name);
        vendor.setVendor_contact(str_vendor_contact);
        vendor.setVendor_email(str_vendor_email);
        vendor.setVendor_Address(str_vendor_address);
        vendor.setServerId(serverId);
        vendor.setVendor_status(vendor_status);
        vendor.setId(localId);

        DatabaseHelper dbhelper = new DatabaseHelper(context);
        dbhelper.updateVendor(vendor);
        dbhelper.closeDB();

        ((Edit_Vendor_Details)context).onBackPressed();

    }
}
