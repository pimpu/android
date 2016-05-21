package com.alchemistdigital.kissan.asynctask;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.alchemistdigital.kissan.DBHelper.DatabaseHelper;
import com.alchemistdigital.kissan.activities.View_Obp;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by user on 4/6/2016.
 */
public class DeleteOBPAsyncTask extends AsyncTask<String, String, String> {

    OBP obp;
    Context context;
    int position;

    public DeleteOBPAsyncTask(Context context, OBP obp, int position) {
        this.obp = obp;
        this.context = context;
        this.position = position;
    }

    @Override
    protected String doInBackground(String... params) {
        String responseString = null;
        String serverId = String.valueOf(obp.getUserID_serverId());

        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(CommonVariables.DELETE_OBP_SERVER_URL);

        try {
            AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                    new AndroidMultiPartEntity.ProgressListener() {

                        @Override
                        public void transferred(long num) {
//                            publishProgress((int) ((num / (float) totalSize) * 100));
                        }
                    });

            // Adding file data to http body
            entity.addPart("serverId", new StringBody(serverId));

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
        Log.d("delete obp data", result.toString());

        if(result.contains("Error occurred!")){
            Toast.makeText(context, result, Toast.LENGTH_LONG).show();
            return;
        }

        JSONObject json = null;
        try {
            json = new JSONObject(result);
            int success = json.getInt(CommonVariables.TAG_SUCCESS);
            if (success == 1) {

                DatabaseHelper dbhelper = new DatabaseHelper(context);
                dbhelper.deleteObp(obp);
                int len = dbhelper.isAnyOBPPresent();
                dbhelper.closeDB();

                if (len <= 0) {
                    ((View_Obp)context).emptyView.setVisibility(View.VISIBLE);
                    ((View_Obp)context).obp_RecyclerView.setVisibility(View.GONE);
                }

                // reomove deleted obp from adapter
                ((View_Obp)context).data.remove(position);
                ((View_Obp)context).obp_Adapter.notifyItemRemoved(position);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
