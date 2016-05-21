package com.alchemistdigital.kissan.asynctask;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.alchemistdigital.kissan.DBHelper.DatabaseHelper;
import com.alchemistdigital.kissan.model.Item;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 3/17/2016.
 */
public class GetOrderAsyncTask extends AsyncTask<String, String, String> {
    private Context context;
    // Progress Dialog
    private ProgressDialog pDialog;
    private String strUID,userType;

    public GetOrderAsyncTask(Context context, String strUID, String userType) {
        this.context = context;
        this.strUID = strUID;
        this.userType = userType;
    }

    @Override
    protected String doInBackground(String... params) {
        String responseString = null;

        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(CommonVariables.ALL_ORDER_QUERY_SERVER_URL);

        try {
            AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                    new AndroidMultiPartEntity.ProgressListener() {

                        @Override
                        public void transferred(long num) {
//                            publishProgress((int) ((num / (float) totalSize) * 100));
                        }
                    });

            // Adding file data to http body
            entity.addPart("userId", new StringBody(strUID));
            entity.addPart("userType", new StringBody(userType));

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

        Log.d("get all order Data:: ", result.toString());
        if(result.contains("Error occurred!")){
            Toast.makeText(context, result, Toast.LENGTH_LONG).show();
            return;
        }

        JSONObject json = null;
        try {

            json = new JSONObject(result);
            int success = json.getInt(CommonVariables.TAG_SUCCESS);

            if (success == 1) {
                JSONArray jsonSociety = json.getJSONArray(CommonVariables.TAG_MESSAGE);
                DatabaseHelper dbHelper = new DatabaseHelper(context);

                List<String> referenceNo = new ArrayList<>();

                for (int i = 0; i < jsonSociety.length(); i++) {
                    JSONObject jsonObj = jsonSociety.getJSONObject(i);
                    int userID = jsonObj.getInt("userID");
                    String enqRef = jsonObj.getString("enqRef");
                    String utr = jsonObj.getString("UTR");
                    String ordItem = jsonObj.getString("ordItem");
                    int ordQty = jsonObj.getInt("ordQty");
                    String ordPrice = jsonObj.getString("ordPrice");
                    String totamnt = jsonObj.getString("totamnt");
                    String ordDate = jsonObj.getString("ordDate");
                    int ordStatus = jsonObj.getInt("ordStatus");

                    Item item = new Item( enqRef, ordItem, ordQty, ordPrice, totamnt);
                    dbHelper.insertItem(item);

                    if (dbHelper.numberOfOrderByRefNo(enqRef) <= 0 ){
                        Order order = new Order( userID, enqRef, utr, ordDate, ordStatus);
                        dbHelper.insertOrder(order);
                    }
                }
                dbHelper.closeDB();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        // check for success tag

    }
}
