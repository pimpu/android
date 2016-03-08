package com.alchemistdigital.kissan.asynctask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.alchemistdigital.kissan.DBHelper.DatabaseHelper;
import com.alchemistdigital.kissan.R;
import com.alchemistdigital.kissan.model.Society;
import com.alchemistdigital.kissan.sharedPrefrenceHelper.GetSharedPreferenceHelper;
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
 * Created by user on 2/29/2016.
 */
public class InsertSocietyAsyncTask extends AsyncTask<String,String,String>{
    private Context context;
    // Progress Dialog
    private ProgressDialog pDialog;
    private String str_society_name,str_society_contact,str_society_email,str_society_address,str_userId;

    public InsertSocietyAsyncTask(Context context, String str_society_name, String str_society_contact, String str_society_email, String str_society_address, int userId) {
        this.context = context;
        this.str_society_name       = str_society_name;
        this.str_society_contact    = str_society_contact;
        this.str_society_email      = str_society_email;
        this.str_society_address    = str_society_address;
        str_userId                  = String.valueOf(userId);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
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
        HttpPost httppost = new HttpPost(CommonVariables.SOCIETY_INSERT_SERVER_URL);

        try {
            AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                    new AndroidMultiPartEntity.ProgressListener() {

                        @Override
                        public void transferred(long num) {
//                            publishProgress((int) ((num / (float) totalSize) * 100));
                        }
                    });

            // Adding file data to http body
            entity.addPart("society_name", new StringBody(str_society_name));
            entity.addPart("society_contact", new StringBody(str_society_contact));
            entity.addPart("society_email", new StringBody(str_society_email));
            entity.addPart("society_address", new StringBody(str_society_address));
            entity.addPart("userId", new StringBody(str_userId));

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
            responseString = e.toString();
        } catch (IOException e) {
            responseString = e.toString();
        }

        return responseString;
    }

    @Override
    protected void onPostExecute(String result) {
        pDialog.dismiss();

        try {
            Log.d("Society insert Data", result.toString());
            if(result.contains("Error occurred!")){
                Toast.makeText(context, result, Toast.LENGTH_LONG).show();
                return;
            }

            JSONObject json = new JSONObject(result);
            // check for success tag
            int success = json.getInt(CommonVariables.TAG_SUCCESS);
            if (success == 0 || success == 1 || success == 3) {
                String message = json.getString(CommonVariables.TAG_MESSAGE);
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
            else {
                int id_society = json.getInt(CommonVariables.TAG_MESSAGE);
                DatabaseHelper dbHelper = new DatabaseHelper(context);

                // get userId from shared preference.
                GetSharedPreferenceHelper getPreference = new GetSharedPreferenceHelper(context);
                int uId = getPreference.getUserIdPreference(context.getResources().getString(R.string.userId));

                Society society = new Society(id_society,uId,str_society_name,str_society_contact,str_society_email,str_society_address);
                long societyId = dbHelper.insertSociety(society);
                System.out.println("Society id: "+societyId);
                System.out.println("Society count: "+dbHelper.numberOfSocietyRows());
                dbHelper.closeDB();

                ((Activity)context).finish();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
