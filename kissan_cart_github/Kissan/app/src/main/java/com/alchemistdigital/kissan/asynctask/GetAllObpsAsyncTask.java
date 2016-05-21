package com.alchemistdigital.kissan.asynctask;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.alchemistdigital.kissan.DBHelper.DatabaseHelper;
import com.alchemistdigital.kissan.model.OBP;
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
 * Created by user on 4/8/2016.
 */
public class GetAllObpsAsyncTask extends AsyncTask<String, String, String> {
    private Context context;
    // Progress Dialog
    private ProgressDialog pDialog;
    String userId;

    public GetAllObpsAsyncTask(Context context, int uId) {
        this.context = context;
        userId = String.valueOf(uId);
    }

    @Override
    protected String doInBackground(String... params) {
        String responseString = null;

        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(CommonVariables.ALL_OBP_QUERY_SERVER_URL);

        try {
            AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                    new AndroidMultiPartEntity.ProgressListener() {

                        @Override
                        public void transferred(long num) {
//                            publishProgress((int) ((num / (float) totalSize) * 100));
                        }
                    });

            // Adding file data to http body
            entity.addPart("userId", new StringBody(userId));

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

        Log.d("get all OBP Data:: ", result.toString());
        if(result.contains("Error occurred!")){
            Toast.makeText(context, result, Toast.LENGTH_LONG).show();
            return;
        }

        JSONObject json = null;
        try {

            json = new JSONObject(result);
            int success = json.getInt(CommonVariables.TAG_SUCCESS);

            if (success == 1) {
                JSONArray jsonObp = json.getJSONArray(CommonVariables.TAG_MESSAGE);
                DatabaseHelper dbHelper = new DatabaseHelper(context);

                for (int i = 0; i < jsonObp.length(); i++) {

                    JSONObject jsonObj = jsonObp.getJSONObject(i);

                    int userID_serverId = jsonObj.getInt("userID_serverId");
                    String obp_name = jsonObj.getString("obp_name");
                    String obp_store_name = jsonObj.getString("obp_store_name");
                    String obp_email_id = jsonObj.getString("obp_email_id");
                    String obp_email_passowrd = jsonObj.getString("obp_email_passowrd");
                    String obp_contact_number = jsonObj.getString("obp_contact_number");
                    String obp_address = jsonObj.getString("obp_address");
                    int obp_pincode = jsonObj.getInt("obp_pincode");
                    String obp_city = jsonObj.getString("obp_city");
                    String obp_state = jsonObj.getString("obp_state");
                    String obp_country = jsonObj.getString("obp_country");
                    int obp_status = jsonObj.getInt("obp_status");

                    OBP obp = new OBP( userID_serverId, obp_name, obp_store_name, obp_email_id, obp_email_passowrd,
                            obp_contact_number, obp_address, obp_pincode, obp_city, obp_state, obp_country, obp_status );

                    dbHelper.insertOBPData(obp);

                }
                dbHelper.closeDB();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
