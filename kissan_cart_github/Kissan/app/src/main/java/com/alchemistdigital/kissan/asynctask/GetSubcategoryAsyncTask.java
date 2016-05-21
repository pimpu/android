package com.alchemistdigital.kissan.asynctask;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.alchemistdigital.kissan.DBHelper.DatabaseHelper;
import com.alchemistdigital.kissan.model.Subcategory;
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
 * Created by user on 5/17/2016.
 */
public class GetSubcategoryAsyncTask extends AsyncTask<String, String, String> {
    Context context;

    public GetSubcategoryAsyncTask(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(String... params) {
        String responseString = null;

        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(CommonVariables.GET_SUBCATEGORY_SERVER_URL);

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

        Log.d("get subcategory Data:: ", result.toString());
        if(result.contains("Error occurred!")) {
            Toast.makeText(context, result, Toast.LENGTH_LONG).show();
            return;
        }

        JSONObject json = null;
        try {

            json = new JSONObject(result);
            int success = json.getInt(CommonVariables.TAG_SUCCESS);

            if (success == 1) {
                JSONArray jsonProductGroup = json.getJSONArray(CommonVariables.TAG_MESSAGE);
                DatabaseHelper dbHelper = new DatabaseHelper(context);

                for (int i = 0; i < jsonProductGroup.length(); i++) {
                    JSONObject jsonObj = jsonProductGroup.getJSONObject(i);

                    int serverId = jsonObj.getInt("serverId");
                    int category_id = jsonObj.getInt("category_id");
                    int product_group_id = jsonObj.getInt("product_group_id");
                    String subcategory_name = jsonObj.getString("subcategory_name");
                    String subcategory_image = jsonObj.getString("subcategory_image");
                    int status = jsonObj.getInt("status");

                    Subcategory subcategoryItem = new Subcategory(serverId, category_id, product_group_id, subcategory_name, subcategory_image, status);
                    dbHelper.insertSubcategoryGroup(subcategoryItem);

                }
                dbHelper.closeDB();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        // check for success tag

    }
}
