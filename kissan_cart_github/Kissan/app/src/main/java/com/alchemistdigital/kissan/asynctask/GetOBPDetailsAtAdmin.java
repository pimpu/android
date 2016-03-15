package com.alchemistdigital.kissan.asynctask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alchemistdigital.kissan.R;
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
 * Created by user on 3/15/2016.
 */
public class GetOBPDetailsAtAdmin extends AsyncTask<String, String, String> {
    Context context;
    String userId;
    private ProgressDialog pDialog;

    public GetOBPDetailsAtAdmin(Context context, String userId) {
        this.context = context;
        this.userId = userId;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
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
        HttpPost httppost = new HttpPost(CommonVariables.QUERY_OBP_DETAIL_SERVER_URL);

        try {
            AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                    new AndroidMultiPartEntity.ProgressListener() {

                        @Override
                        public void transferred(long num) {
//                            publishProgress((int) ((num / (float) totalSize) * 100));
                        }
                    });

            // Adding file data to http body
            entity.addPart("str_obpId", new StringBody(userId));

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
            Log.d("get obp Data at admin: ", result.toString());

            if(result.contains("Error occurred!")){
                Toast.makeText(context, result, Toast.LENGTH_LONG).show();
                return;
            }

            JSONObject json = new JSONObject(result);
            // check for success tag
            int success = json.getInt(CommonVariables.TAG_SUCCESS);
            if (success == 1) {
                JSONArray jsonSociety = json.getJSONArray(CommonVariables.TAG_MESSAGE);

                JSONObject obpData = jsonSociety.getJSONObject(0);

                createObpDialog(context,obpData);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void createObpDialog(Context context, JSONObject obpData) {
        // custom dialog
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.custom_alert_obp_detail, null);
        dialogBuilder.setView(dialogView);


        // set the custom dialog components - text, image and button
        TextView tv_obp_name = (TextView) dialogView.findViewById(R.id.tv_id_inOrderDetails_obp_name);
        TextView tv_store_name = (TextView) dialogView.findViewById(R.id.tv_id_inOrderDetails_store_name);
        TextView tv_obp_contact = (TextView) dialogView.findViewById(R.id.tv_id_inOrderDetails_obp_contact);
        TextView tv_obp_email = (TextView) dialogView.findViewById(R.id.tv_id_inOrderDetails_obp_email);
        TextView tv_obp_address = (TextView) dialogView.findViewById(R.id.tv_id_inOrderDetails_obp_address);
        ImageView closeDialog = (ImageView) dialogView.findViewById(R.id.closeOBPDetailsAlert);

        try {
            tv_obp_name.setText( obpData.getString("name") );
            tv_store_name.setText( obpData.getString("storeName") );
            tv_obp_contact.setText( obpData.getString("contact") );
            tv_obp_email.setText( obpData.getString("email") );
            tv_obp_address.setText( obpData.getString("address") );
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final AlertDialog b = dialogBuilder.create();

        // if button is clicked, close the custom dialog
        closeDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b.dismiss();
            }
        });

        b.show();
    }

}
