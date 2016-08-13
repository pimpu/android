package com.alchemistdigital.buxa.asynctask;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.alchemistdigital.buxa.utilities.AndroidMultiPartEntity;
import com.alchemistdigital.buxa.utilities.CommonVariables;

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
 * Created by user on 8/12/2016.
 */
public class InsertCompanyRegisteration extends AsyncTask<String, String, String>  {
    private ProgressDialog pDialog;
    private Context context;
    private String code;
    private String companyName;
    private String address;
    private String landmark;
    private String city;
    private String zipcode;
    private String pan;
    private String tin;
    private String contactName;
    private String mobile;
    private String emailId;
    private String password;
    private String strState;

    public InsertCompanyRegisteration(Context context,
                                      String code,
                                      String companyName,
                                      String address,
                                      String landmark,
                                      String city,
                                      String zipcode,
                                      String pan,
                                      String tin,
                                      String contactName,
                                      String mobile,
                                      String emailId,
                                      String password,
                                      String strState) {

        this.context = context;
        this.code = code;
        this.companyName = companyName;
        this.address = address;
        this.landmark = landmark;
        this.city = city;
        this.zipcode = zipcode;
        this.pan = pan;
        this.tin = tin;
        this.contactName = contactName;
        this.mobile = mobile;
        this.emailId = emailId;
        this.password = password;
        this.strState = strState;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        pDialog = new ProgressDialog(context);
        pDialog.setMessage("Registering ...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    @Override
    protected String doInBackground(String... params) {
        String responseString = null;

        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(CommonVariables.COMPANY_REGISTER_SERVER_URL);

        try {
            AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                    new AndroidMultiPartEntity.ProgressListener() {

                        @Override
                        public void transferred(long num) {
//                            publishProgress((int) ((num / (float) totalSize) * 100));
                        }
                    });

            // Adding file data to http body
            entity.addPart("code", new StringBody(code));
            entity.addPart("company", new StringBody(companyName));
            entity.addPart("address", new StringBody(address));
            entity.addPart("landmark", new StringBody(landmark));
            entity.addPart("country", new StringBody("India"));
            entity.addPart("state", new StringBody(strState));
            entity.addPart("city", new StringBody(city));
            entity.addPart("zipcode", new StringBody(zipcode));
            entity.addPart("pan", new StringBody(pan));
            entity.addPart("tin", new StringBody(tin));
            entity.addPart("uname", new StringBody(contactName));
            entity.addPart("mobile", new StringBody(mobile));
            entity.addPart("email", new StringBody(emailId));
            entity.addPart("password", new StringBody(password));
            entity.addPart("create_time", new StringBody(""+System.currentTimeMillis()));

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

        Log.d("company registeration", result.toString());
    }
}
