package com.alchemistdigital.kissan.asynctask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.alchemistdigital.kissan.DBHelper.DatabaseHelper;
import com.alchemistdigital.kissan.activities.View_Orders;
import com.alchemistdigital.kissan.model.Order;
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
 * Created by user on 3/11/2016.
 */
public class InsertOrderAsyncTask extends AsyncTask<String,String,String>{
    Context context;
    private ProgressDialog pDialog;
    private String str_enquiry_refno,str_utr,jsonItemArray,userId;

    public InsertOrderAsyncTask(Context context, String str_enquiry_refno, String str_utr, String jsonItemArray, String userId) {
        this.context = context;
        this.str_enquiry_refno = str_enquiry_refno;
        this.str_utr = str_utr;
        this.jsonItemArray = jsonItemArray;
        this.userId = userId;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pDialog = new ProgressDialog(context);
        pDialog.setMessage("inserting ...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    @Override
    protected String doInBackground(String... params) {
        String responseString = null;

        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(CommonVariables.ORDER_INSERT_SERVER_URL);

        try {
            AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                    new AndroidMultiPartEntity.ProgressListener() {

                        @Override
                        public void transferred(long num) {
//                            publishProgress((int) ((num / (float) totalSize) * 100));
                        }
                    });

            // Adding file data to http body
            entity.addPart("referenceNo", new StringBody(str_enquiry_refno));
            entity.addPart("UTR", new StringBody(str_utr));
            entity.addPart("itemsJSONArray", new StringBody(jsonItemArray));
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
            responseString = e.toString();
        } catch (IOException e) {
            responseString = e.toString();
        }

        return responseString;
    }

    @Override
    protected void onPostExecute(String result) {
        pDialog.dismiss();

        try {
            Log.d("Order insert Data", result.toString());
            if(result.contains("Error occurred!")) {
                Toast.makeText(context, result, Toast.LENGTH_LONG).show();
                return;
            }

            JSONObject json = new JSONObject(result);
            int success = json.getInt(CommonVariables.TAG_SUCCESS);
            String message = json.getString(CommonVariables.TAG_MESSAGE);
            if (success == 1) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                DatabaseHelper dbhelper = new DatabaseHelper(context);

                String creationtime = json.getString("creationtime");
                Order order = new Order(Integer.parseInt(userId), str_enquiry_refno,
                                        str_utr, creationtime, 1);
                dbhelper.insertOrder(order);
                dbhelper.closeDB();

                context.startActivity(new Intent(context, View_Orders.class));
                ((Activity)context).finish();
            }
            else {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
