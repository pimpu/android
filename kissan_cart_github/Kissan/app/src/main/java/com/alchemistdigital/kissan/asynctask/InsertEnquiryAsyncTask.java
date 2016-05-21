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
 * Created by user on 3/5/2016.
 */
public class InsertEnquiryAsyncTask extends AsyncTask<String, String, String> {
    private Context context;
    // Progress Dialog
    private ProgressDialog pDialog;
    private String str_ref_no;
    private String strUID;
    private String userType;
    private String eId;
    private int repToVal;
    private String replyTo;
    private int gId;
    private String groupId;
    private int selectedSocietyServerId;
    String societyId;
    private int selectedSubcategoryServerId;
    String subcategoryId;
    private int selectedProductServerId;
    String productId;
    String str_product_qty;

    public InsertEnquiryAsyncTask(Context context,
                                  String str_ref_no,
                                  String strUID,
                                  int gId,
                                  int repToVal,
                                  int selectedSocietyServerId,
                                  int selectedSubcategoryServerId,
                                  int selectedProductServerId,
                                  String str_product_qty,
                                  String userType,
                                  String eId) {
        this.context = context;
        this.str_ref_no = str_ref_no;
        this.strUID = strUID;
        this.userType = userType;
        this.eId = eId;
        this.selectedSocietyServerId = selectedSocietyServerId;
        this.selectedSubcategoryServerId = selectedSubcategoryServerId;
        this.selectedProductServerId = selectedProductServerId;
        this.str_product_qty = str_product_qty;
        this.repToVal = repToVal;
        this.gId = gId;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        ((Activity)context).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);

        pDialog = new ProgressDialog(context);
        pDialog.setMessage("inserting ...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    @Override
    protected String doInBackground(String... params) {
        return uploadFile();
    }

    private String uploadFile() {
        String responseString = null;

        groupId = String.valueOf(gId);
        replyTo = String.valueOf(repToVal);
        societyId = String.valueOf(selectedSocietyServerId);
        subcategoryId = String.valueOf(selectedSubcategoryServerId);
        productId = String.valueOf(selectedProductServerId);

        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(CommonVariables.ENQUIRY_INSERT_SERVER_URL);

        try {
            AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                    new AndroidMultiPartEntity.ProgressListener() {

                        @Override
                        public void transferred(long num) {
//                            publishProgress((int) ((num / (float) totalSize) * 100));
                        }
                    });

            // Adding file data to http body
            entity.addPart("refNo", new StringBody(str_ref_no));
            entity.addPart("uId", new StringBody(strUID));
            entity.addPart("userType", new StringBody(userType));
            entity.addPart("eId", new StringBody(eId));
            entity.addPart("replyTo", new StringBody(replyTo));
            entity.addPart("groupId", new StringBody(groupId));
            entity.addPart("societyId", new StringBody(societyId));
            entity.addPart("subcategoryId", new StringBody(subcategoryId));
            entity.addPart("productId", new StringBody(productId));
            entity.addPart("productQty", new StringBody(str_product_qty));

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

        Log.d("enquiry insert Data", result.toString());

        if (result.contains("Error occurred!")) {
            Toast.makeText(context, result, Toast.LENGTH_LONG).show();
            return;
        }

        try {
            JSONObject json = new JSONObject(result);

            int success = json.getInt(CommonVariables.TAG_SUCCESS);
            if(success == 1) {
                String message = json.getString(CommonVariables.TAG_MESSAGE);
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            }
            else {
                JSONArray jsonSociety = json.getJSONArray(CommonVariables.TAG_MESSAGE);
                DatabaseHelper dbHelper = new DatabaseHelper(context);

                JSONObject c = jsonSociety.getJSONObject(0);
                int id_enquiry = c.getInt("id");
                String creted_at = c.getString("timestamp");
                int eUid = Integer.valueOf(strUID);

                Enquiry enquiry = new Enquiry(id_enquiry, creted_at, str_ref_no, eUid, gId,
                        repToVal, 0, selectedSocietyServerId, selectedSubcategoryServerId, selectedProductServerId,
                        str_product_qty,1 );

                long enquiryId = dbHelper.insertEnquiry(enquiry);
                System.out.println(context.getClass().getSimpleName() + "(insert) : " + enquiryId);

                if( !eId.equals("0") ) {
                    int i = dbHelper.updateEnquiryReplied(eId, "1");
                }

                dbHelper.closeDB();
                ((Activity)context).onBackPressed();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
