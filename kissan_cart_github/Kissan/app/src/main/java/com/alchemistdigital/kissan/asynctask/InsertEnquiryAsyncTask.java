package com.alchemistdigital.kissan.asynctask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
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
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

/**
 * Created by user on 3/5/2016.
 */
public class InsertEnquiryAsyncTask extends AsyncTask<String, String, String> {
    private Context context;
    // Progress Dialog
    private ProgressDialog pDialog;
    private String str_ref_no;
    private String str_name;
    private String str_contact;
    private String str_email;
    private String str_address;
    private String str_message;
    private String absolutePath;
    private String strUID;
    private String userType;
    private String eId;

    public InsertEnquiryAsyncTask(Context context, String str_ref_no, String str_name, String str_contact, String str_email, String str_address, String str_message, String absolutePath, String strUID, String userType, String eId) {
        this.context = context;
        this.str_ref_no = str_ref_no;
        this.str_name = str_name;
        this.str_contact = str_contact;
        this.str_email = str_email;
        this.str_address = str_address;
        this.str_message = str_message;
        this.absolutePath = absolutePath;
        this.strUID = strUID;
        this.userType = userType;
        this.eId = eId;
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
        return uploadFile();
    }

    private String uploadFile() {
        String responseString = null;

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

            File sourceFile = new File(absolutePath);

            // Adding file data to http body
            entity.addPart("image", new FileBody(sourceFile));
            entity.addPart("refNo", new StringBody(str_ref_no));
            entity.addPart("name", new StringBody(str_name));
            entity.addPart("contact", new StringBody(str_contact));
            entity.addPart("email", new StringBody(str_email));
            entity.addPart("address", new StringBody(str_address));
            entity.addPart("message", new StringBody(str_message));
            entity.addPart("uId", new StringBody(strUID));
            entity.addPart("userType", new StringBody(userType));
            entity.addPart("eId", new StringBody(eId));
            entity.addPart("filepath",new StringBody(CommonVariables.FILE_UPLOAD_URL));

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
        pDialog.dismiss();

        Log.d("enquiry insert Data", result.toString());

        if (result.contains("Error occurred!")) {
            Toast.makeText(context, result, Toast.LENGTH_LONG).show();
            return;
        }

        try {
            JSONObject json = new JSONObject(result);

            int success = json.getInt(CommonVariables.TAG_SUCCESS);
            if(success == 1){
                String message = json.getString(CommonVariables.TAG_MESSAGE);
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            }
            else {
                JSONArray jsonSociety = json.getJSONArray(CommonVariables.TAG_MESSAGE);
                DatabaseHelper dbHelper = new DatabaseHelper(context);

                JSONObject c = jsonSociety.getJSONObject(0);
                int id_enquiry = c.getInt("id");
                String creted_at = c.getString("timestamp"),fileName=null;
                int gId = c.getInt("groupId");
                int repToVal = c.getInt("repToVal");
                int eUid = Integer.valueOf(strUID);

                //  absolutepath = .../filename.png
                int cut = absolutePath.lastIndexOf('/');
                if (cut != -1) {
                    //  fileName = filename.png
                    fileName = absolutePath.substring(cut + 1);
                }

                Enquiry enquiry = new Enquiry(id_enquiry, creted_at, str_ref_no, eUid, gId, repToVal, 0, str_message, str_name, str_address, str_contact, str_email, fileName, 1);
                long enquiryId = dbHelper.insertEnquiry(enquiry);
                System.out.println(context.getClass().getSimpleName() + "(insert) : " + enquiryId);

                System.out.println("Before update stmt: "+eId);
                if( !eId.equals("0") ){
                    System.out.println("inside update replied.");
                    int i = dbHelper.updateEnquiryReplied(eId);
                    System.out.println(context.getClass().getSimpleName()+"(update) : "+i);
                }

                dbHelper.closeDB();
                ((Activity)context).finish();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
