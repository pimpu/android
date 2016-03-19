package com.alchemistdigital.kissan.asynctask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.alchemistdigital.kissan.R;
import com.alchemistdigital.kissan.activities.AdminPanel;
import com.alchemistdigital.kissan.activities.MainActivity;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by user on 3/2/2016.
 */
public class LoginAsyncTask extends AsyncTask<String, String, String> {
    private Context context;
    // Progress Dialog
    private ProgressDialog pDialog;
    private String email, pwd;

    public LoginAsyncTask(Context context, String email, String pwd) {
        this.context = context;
        this.email = email;
        this.pwd = pwd;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pDialog = new ProgressDialog(context);
        pDialog.setMessage("logging ...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    @Override
    protected String doInBackground(String... params) {
        String responseString = null;

        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(CommonVariables.LOGGING_SERVER_URL);

        try {
            AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                    new AndroidMultiPartEntity.ProgressListener() {

                        @Override
                        public void transferred(long num) {
//                            publishProgress((int) ((num / (float) totalSize) * 100));
                        }
                    });

            // Adding file data to http body
            entity.addPart("emailId", new StringBody(email));
            entity.addPart("password", new StringBody(pwd));

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

        Log.d("Create Login Response", result.toString());
        if(result.contains("Error occurred!")){
            Toast.makeText(context, result, Toast.LENGTH_LONG).show();
            return;
        }

        try {
            JSONObject json = new JSONObject(result);
            int success = json.getInt(CommonVariables.TAG_SUCCESS);
            if (success == 1) {

                JSONArray userDataArray = json.getJSONArray(CommonVariables.TAG_MESSAGE);
                // get first product object from JSON Array
                JSONObject userDataObject = userDataArray.getJSONObject(0);

                SetSharedPreferenceHelper setPreference = new SetSharedPreferenceHelper(context);

                // it store true value of user for purpose of user is logging.
                setPreference.setLoginPreference(context.getResources().getString(R.string.boolean_login_sharedPref), "true");
                // user register email
                setPreference.setEmailPreference(context.getResources().getString(R.string.loginEmail), email);
                // user type (admin / obp)
                setPreference.setUserTypePreference(context.getResources().getString(R.string.userType), userDataObject.getString("who"));
                // user id which are taken from server sql data
                setPreference.setUserIdPreference(context.getResources().getString(R.string.userId), userDataObject.getInt("loggedId"));
                // user register name
                setPreference.setNamePreference(context.getResources().getString(R.string.loginName), userDataObject.getString("name"));

                Intent intent;
                if (userDataObject.getString("who").equals("obp")) {
                    intent = new Intent(context, MainActivity.class);
                } else {
                    intent = new Intent(context, AdminPanel.class);
                }

//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK  | Intent.FLAG_ACTIVITY_NO_HISTORY);

                ((Activity) context).finish();
                context.startActivity(intent);

            } // if (success == 1)
            else if (success == 0 || success == 2 || success == 3) {
                String message = json.getString(CommonVariables.TAG_MESSAGE);
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}
