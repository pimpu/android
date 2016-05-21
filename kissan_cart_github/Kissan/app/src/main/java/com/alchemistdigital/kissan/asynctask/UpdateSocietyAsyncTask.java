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
import com.alchemistdigital.kissan.activities.View_Society;
import com.alchemistdigital.kissan.model.Society;
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
 * Created by user on 3/18/2016.
 */
public class UpdateSocietyAsyncTask extends AsyncTask<String, String, String>  {
    private ProgressDialog pDialog;
    private Context context;
    private String str_society_name;
    private String str_society_contact;
    private String str_society_email;
    private String str_society_address;
    private String oldSocietyName;
    private int societyServerId;
    private int status;

    public UpdateSocietyAsyncTask(Context context,
                                  String str_society_name,
                                  String str_society_contact,
                                  String str_society_email,
                                  String str_society_address,
                                  String oldSocietyName,
                                  int societyServerId,
                                  int status) {
        this.context = context;
        this.str_society_name = str_society_name;
        this.str_society_contact = str_society_contact;
        this.str_society_email = str_society_email;
        this.str_society_address = str_society_address;
        this.oldSocietyName = oldSocietyName;
        this.societyServerId = societyServerId;
        this.status = status;
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
        HttpPost httppost = new HttpPost(CommonVariables.UPDATE_SOCIETY_DETAIL_SERVER_URL);

        try {
            AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                    new AndroidMultiPartEntity.ProgressListener() {

                        @Override
                        public void transferred(long num) {
//                            publishProgress((int) ((num / (float) totalSize) * 100));
                        }
                    });

            // Adding file data to http body
            entity.addPart("name", new StringBody(str_society_name));
            entity.addPart("contact", new StringBody(str_society_contact));
            entity.addPart("email", new StringBody(str_society_email));
            entity.addPart("address", new StringBody(str_society_address));
            entity.addPart("id", new StringBody( String.valueOf(societyServerId) ));
            entity.addPart("status", new StringBody( String.valueOf(status) ));
            entity.addPart("old_name", new StringBody(oldSocietyName));

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

        Log.d("update society Data", result.toString());

        if(result.contains("Error occurred!")) {
            Toast.makeText(context, result, Toast.LENGTH_LONG).show();
            return;
        }

        Society society = new Society();
        society.setSoc_name(str_society_name);
        society.setSoc_contact(str_society_contact);
        society.setSoc_email(str_society_email);
        society.setSoc_adrs(str_society_address);
        society.setServerId(societyServerId);
        society.setSoc_status(status);

        DatabaseHelper dbhelper = new DatabaseHelper(context);
        dbhelper.updateSociety(society);
        dbhelper.closeDB();

        Intent intent = new Intent(context,View_Society.class);
        context.startActivity(intent);
        ((Activity)context).finish();

    }

}
