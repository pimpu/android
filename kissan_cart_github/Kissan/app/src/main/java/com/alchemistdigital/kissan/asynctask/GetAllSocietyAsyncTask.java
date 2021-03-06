package com.alchemistdigital.kissan.asynctask;

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
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by user on 3/9/2016.
 */
public class GetAllSocietyAsyncTask extends AsyncTask<String, String, String> {
    private Context context;
    // Progress Dialog
    private ProgressDialog pDialog;

    public GetAllSocietyAsyncTask(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(String... params) {
        String responseString = null;

        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(CommonVariables.ALL_SOCIETY_BY_ADMIN_QUERY_SERVER_URL);

        try {
            AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                    new AndroidMultiPartEntity.ProgressListener() {

                        @Override
                        public void transferred(long num) {
//                            publishProgress((int) ((num / (float) totalSize) * 100));
                        }
                    });

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
        try {
            Log.d("All Societies at admin", result.toString());

            if(result.contains("Error occurred!")){
                Toast.makeText(context, result, Toast.LENGTH_LONG).show();
                return;
            }

            JSONObject json = new JSONObject(result);
            // check for success tag
            int success = json.getInt(CommonVariables.TAG_SUCCESS);
            if (success == 2) {
                String message = json.getString(CommonVariables.TAG_MESSAGE);
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
            else if (success == 1) {
                JSONArray jsonSociety = json.getJSONArray(CommonVariables.TAG_MESSAGE);
                DatabaseHelper dbHelper = new DatabaseHelper(context);

                // get userId from shared preference.
                GetSharedPreferenceHelper getPreference = new GetSharedPreferenceHelper(context);
                int uId = getPreference.getUserIdPreference(context.getResources().getString(R.string.userId));

                // looping through All Products
                for (int i = 0; i < jsonSociety.length(); i++) {
                    JSONObject c = jsonSociety.getJSONObject(i);

                    // Storing each json item in variable
                    int id_society = c.getInt("soc_id");
                    String str_society_name = c.getString("soc_name");
                    String str_society_contact = c.getString("soc_contact");
                    String str_society_email = c.getString("soc_email");
                    String str_society_address = c.getString("soc_adrs");
                    int str_society_status = c.getInt("soc_status");

                    Society society = new Society(id_society,uId,str_society_name,
                                                    str_society_contact,str_society_email,
                                                    str_society_address,str_society_status );
                    long societyId = dbHelper.insertSociety(society);
                    System.out.println(context.getClass().getSimpleName()+" : "+societyId);
//                    System.out.println("Society count: " + dbHelper.numberOfSocietyRows());
                }

                dbHelper.closeDB();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
