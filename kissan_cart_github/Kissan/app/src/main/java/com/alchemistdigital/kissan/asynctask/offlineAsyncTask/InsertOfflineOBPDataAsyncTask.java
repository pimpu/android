package com.alchemistdigital.kissan.asynctask.offlineAsyncTask;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.alchemistdigital.kissan.DBHelper.DatabaseHelper;
import com.alchemistdigital.kissan.utilities.AndroidMultiPartEntity;
import com.alchemistdigital.kissan.utilities.CommonVariables;
import com.alchemistdigital.kissan.utilities.offlineActionModeEnum;

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
 * Created by user on 4/5/2016.
 */
public class InsertOfflineOBPDataAsyncTask extends AsyncTask<String, String, String> {

    private Context context;
    private String jsonArrayObpArr, gId;

    public InsertOfflineOBPDataAsyncTask(Context context, String jsonArrayObpArr, String gId) {
        this.context = context ;
        this.jsonArrayObpArr = jsonArrayObpArr ;
        this.gId = gId ;
    }

    @Override
    protected String doInBackground(String... params) {
        String responseString = null;

        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(CommonVariables.OFFLINE_OBP_INSERT_SERVER_URL);

        try {
            AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                    new AndroidMultiPartEntity.ProgressListener() {

                        @Override
                        public void transferred(long num) {
//                            publishProgress((int) ((num / (float) totalSize) * 100));
                        }
                    });


            // Adding file data to http body
            entity.addPart("jsonArrayObpArr", new StringBody(jsonArrayObpArr));
            entity.addPart("gId", new StringBody(gId));

//            totalSize = entity.getContentLength();
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
        Log.d("offline obp insert", result.toString());

        if (result.contains("Error occurred!")) {
            Toast.makeText(context, result, Toast.LENGTH_LONG).show();
            return;
        }

        JSONObject json = null;
        try {
            json = new JSONObject(result);
            int success = json.getInt(CommonVariables.TAG_SUCCESS);
            if(success == 1) {
                JSONArray jsonSociety = json.getJSONArray(CommonVariables.TAG_MESSAGE);
                DatabaseHelper dbHelper = new DatabaseHelper(context);

                for( int i = 0 ; i < jsonSociety.length() ; i++ ) {
                    JSONObject c = jsonSociety.getJSONObject(i);
                    if ( (c.getString("action")).equals( offlineActionModeEnum.INSERT.toString() ) ) {

                        String localOBPId = c.getString("localOBPId");
                        int serverId = c.getInt("serverId");

                        dbHelper.updateServerIdOfOBP(localOBPId, serverId);

                        dbHelper.deleteOfflineTableData(localOBPId);
                    }
                    else if( (c.getString("action")).equals( offlineActionModeEnum.UPDATE.toString() ) ){
                        String localId = c.getString("localId");
                        dbHelper.deleteOfflineTableData( localId );
                    }
                }
                dbHelper.closeDB();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
