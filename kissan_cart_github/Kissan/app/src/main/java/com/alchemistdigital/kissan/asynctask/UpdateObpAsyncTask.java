package com.alchemistdigital.kissan.asynctask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.alchemistdigital.kissan.DBHelper.DatabaseHelper;
import com.alchemistdigital.kissan.R;
import com.alchemistdigital.kissan.model.OBP;
import com.alchemistdigital.kissan.sharedPrefrenceHelper.GetSharedPreferenceHelper;
import com.alchemistdigital.kissan.sharedPrefrenceHelper.SetSharedPreferenceHelper;
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
 * Created by user on 4/6/2016.
 */
public class UpdateObpAsyncTask extends AsyncTask<String, String, String> {

    private Context context;
    // Progress Dialog
    private ProgressDialog pDialog;
    private String str_obp_name, str_obp_contact, str_obp_email, str_obp_pwd, str_obp_address,
            str_obp_store_name, str_obp_pin, str_obp_city, str_obp_state,
            str_obp_country,str_serverId, str_status, gId;

    public UpdateObpAsyncTask(
            Context context,
            int uId,
            String str_obp_name,
            String str_obp_store_name,
            String str_obp_email,
            String str_obp_pwd,
            String str_obp_contact,
            String str_obp_address,
            String str_obp_pin,
            String str_obp_city,
            String str_obp_state,
            String str_obp_country,
            int status) {

        this.context = context;
        this.str_obp_name = str_obp_name;
        this.str_obp_contact = str_obp_contact;
        this.str_obp_email = str_obp_email;
        this.str_obp_pwd = str_obp_pwd;
        this.str_obp_address = str_obp_address;
        this.str_obp_store_name = str_obp_store_name;
        this.str_obp_pin = str_obp_pin;
        this.str_obp_city = str_obp_city;
        this.str_obp_state = str_obp_state;
        this.str_obp_country = str_obp_country;
        str_serverId = String.valueOf(uId);
        str_status = String.valueOf(status);

        GetSharedPreferenceHelper getPrefrence = new GetSharedPreferenceHelper(context);
        String who = getPrefrence.getUserTypePreference(context.getResources().getString(R.string.userType));
        // check user is admin or obp
        // on the bases of preference value.
        if( who.equals("obp") ){
            gId = "2" ;
        }
        else {
            gId = "1";
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
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
        HttpPost httppost = new HttpPost(CommonVariables.UPDATE_OBP_SERVER_URL);

        try {
            AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                    new AndroidMultiPartEntity.ProgressListener() {

                        @Override
                        public void transferred(long num) {
//                            publishProgress((int) ((num / (float) totalSize) * 100));
                        }
                    });

            // Adding file data to http body
            entity.addPart("name", new StringBody(str_obp_name));
            entity.addPart("contact", new StringBody(str_obp_contact));
            entity.addPart("email", new StringBody(str_obp_email));
            entity.addPart("pwd", new StringBody(str_obp_pwd));
            entity.addPart("address", new StringBody(str_obp_address));
            entity.addPart("store_name", new StringBody(str_obp_store_name));
            entity.addPart("pin", new StringBody(str_obp_pin));
            entity.addPart("city", new StringBody(str_obp_city));
            entity.addPart("state", new StringBody(str_obp_state));
            entity.addPart("country", new StringBody(str_obp_country));
            entity.addPart("serverId", new StringBody(str_serverId));
            entity.addPart("status", new StringBody(str_status));
            entity.addPart("gId",new StringBody(gId));

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

        Log.d("update society Data", result.toString());

        if(result.contains("Error occurred!")) {
            Toast.makeText(context, result, Toast.LENGTH_LONG).show();
            return;
        }

        OBP obpObj = new OBP(
                Integer.parseInt(str_serverId),
                str_obp_name,
                str_obp_store_name,
                str_obp_email,
                str_obp_pwd,
                str_obp_contact,
                str_obp_address,
                Integer.parseInt(str_obp_pin),
                str_obp_city,
                str_obp_state,
                str_obp_country,
                Integer.parseInt(str_status));

        DatabaseHelper dbHelper = new DatabaseHelper(context);
        dbHelper.updateObpData(obpObj);

        SetSharedPreferenceHelper setPreference = new SetSharedPreferenceHelper(context);
        // user register email
        setPreference.setEmailPreference(context.getResources().getString(R.string.loginEmail), str_obp_email);
        // user register name
        setPreference.setNamePreference(context.getResources().getString(R.string.loginName), str_obp_name);

        ((Activity)context).finish();

    }
}
