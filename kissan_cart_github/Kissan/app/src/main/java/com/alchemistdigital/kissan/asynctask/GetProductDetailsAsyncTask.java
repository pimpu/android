package com.alchemistdigital.kissan.asynctask;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.alchemistdigital.kissan.DBHelper.DatabaseHelper;
import com.alchemistdigital.kissan.model.Product_Details;
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
public class GetProductDetailsAsyncTask extends AsyncTask<String, String, String>{
    Context context;
    public GetProductDetailsAsyncTask(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(String... params) {
        String responseString = null;

        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(CommonVariables.GET_PRODUCTDETAILS_SERVER_URL);

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

        Log.d("get product details: ", result.toString());
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
                    int subcategory_id = jsonObj.getInt("subcategory_id");
                    int category_id = jsonObj.getInt("category_id");
                    int product_group_id = jsonObj.getInt("product_group_id");
                    String product_name = jsonObj.getString("product_name");
                    String prod_path = jsonObj.getString("prod_path");
                    String prod_path1 = jsonObj.getString("prod_path1");
                    String prod_path2 = jsonObj.getString("prod_path2");
                    String prod_path3 = jsonObj.getString("prod_path3");
                    String prod_path4 = jsonObj.getString("prod_path4");
                    String prod_description = jsonObj.getString("prod_description");
                    String discount_type = jsonObj.getString("discount_type");
                    int status = jsonObj.getInt("status");

                    Product_Details product_details = new Product_Details(
                            serverId,
                            subcategory_id,
                            category_id,
                            product_group_id,
                            product_name,
                            prod_path,
                            prod_path1,
                            prod_path2,
                            prod_path3,
                            prod_path4,
                            prod_description,
                            discount_type,
                            status);
                    dbHelper.insertProductDetailsGroup(product_details);

                }
                dbHelper.closeDB();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        // check for success tag

    }
}
