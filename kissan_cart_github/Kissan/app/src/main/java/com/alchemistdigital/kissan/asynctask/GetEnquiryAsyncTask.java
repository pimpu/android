package com.alchemistdigital.kissan.asynctask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.alchemistdigital.kissan.DBHelper.DatabaseHelper;
import com.alchemistdigital.kissan.model.Enquiry;
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
 * Created by user on 3/8/2016.
 */
public class GetEnquiryAsyncTask extends AsyncTask<String, String, String> {
    private Context context;
    // Progress Dialog
    private ProgressDialog pDialog;
    private String str_obpId;

    public GetEnquiryAsyncTask(Context context, String str_obpId) {
        this.context = context;
        this.str_obpId = str_obpId;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        ((Activity)context).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);

        pDialog = new ProgressDialog(context);
        pDialog.setMessage("Loading ...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    @Override
    protected String doInBackground(String... params) {
        String responseString = null;

        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(CommonVariables.ENQUIRY_PER_OBP_QUERY_SERVER_URL);

        try {
            AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                    new AndroidMultiPartEntity.ProgressListener() {

                        @Override
                        public void transferred(long num) {
//                            publishProgress((int) ((num / (float) totalSize) * 100));
                        }
                    });

            // Adding file data to http body
            entity.addPart("str_obpId", new StringBody(str_obpId));

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
            Log.d("Enquiry per obp Data: ", result.toString());

            if(result.contains("Error occurred!")){
                Toast.makeText(context, result, Toast.LENGTH_LONG).show();
                return;
            }

            JSONObject json = new JSONObject(result);
            // check for success tag
            int success = json.getInt(CommonVariables.TAG_SUCCESS);
            if (success == 1) {
                JSONArray jsonSociety = json.getJSONArray(CommonVariables.TAG_MESSAGE);
                DatabaseHelper dbHelper = new DatabaseHelper(context);

                // looping through All Products
                for (int i = 0; i < jsonSociety.length(); i++) {
                    JSONObject c = jsonSociety.getJSONObject(i);

                    // Storing each json item in variable
                    int id_enquiry = c.getInt("id");
                    String creted_at = c.getString("creted_at");
                    String str_ref_no = c.getString("ref");
                    int eUid = c.getInt("eUid");
                    int gId = c.getInt("gId");
                    int repToVal = c.getInt("repToVal");
                    int replied = c.getInt("replied");
                    int socID = c.getInt("SocID");
                    int scid = c.getInt("scid");
                    int prod_id = c.getInt("prod_id");
                    String qty = c.getString("qty");
                    int status = c.getInt("status");

                    Enquiry enquiry = new Enquiry(id_enquiry, creted_at, str_ref_no, eUid, gId,
                                                    repToVal, replied, socID, scid, prod_id, qty, status );

                    long enquiryId = dbHelper.insertEnquiry(enquiry);

                }

                dbHelper.closeDB();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
