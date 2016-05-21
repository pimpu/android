package com.alchemistdigital.kissan.asynctask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.alchemistdigital.kissan.DBHelper.DatabaseHelper;
import com.alchemistdigital.kissan.activities.View_Vendor;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by user on 5/20/2016.
 */
public class InsertVendorAsyncTask extends AsyncTask<String, String, String> {
    private Context context;
    // Progress Dialog
    private ProgressDialog pDialog;
    private String str_vendor_name, str_vendor_contact, str_vendor_email,  str_vendor_address;

    public InsertVendorAsyncTask(Context context, String str_vendor_name, String str_vendor_contact, String str_vendor_email, String str_vendor_address) {
        this.context = context;
        this.str_vendor_name = str_vendor_name;
        this.str_vendor_contact = str_vendor_contact;
        this.str_vendor_email = str_vendor_email;
        this.str_vendor_address = str_vendor_address;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        ((Activity)context).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);

        pDialog = new ProgressDialog(context);
        pDialog.setMessage("inserting ...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    @Override
    protected String doInBackground(String... params) {

        String responseString = null;

        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(CommonVariables.VENDOR_INSERT_SERVER_URL);

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

        try {
            Log.d("vendor insert Data", result.toString());

            if(result.contains("Error occurred!")){
                Toast.makeText(context, result, Toast.LENGTH_LONG).show();
                return;
            }

            JSONObject json = new JSONObject(result);
            // check for success tag
            int success = json.getInt(CommonVariables.TAG_SUCCESS);

            if(success == 2) {

                int serverIdMessage = json.getInt(CommonVariables.TAG_MESSAGE);

                Vendor vendorObj = new Vendor(
                        serverIdMessage,
                        1,
                        str_vendor_name,
                        str_vendor_contact,
                        str_vendor_address,
                        str_vendor_email);

                DatabaseHelper dbHelper = new DatabaseHelper(context);
                dbHelper.insertVendorData(vendorObj);
                dbHelper.closeDB();

                context.startActivity(new Intent(context, View_Vendor.class));
                ((Activity) context).finish();
            }
            else {
                String message = json.getString(CommonVariables.TAG_MESSAGE);
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
