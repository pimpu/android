package info.alchemistdigital.e_carrier.asynctask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

import info.alchemistdigital.e_carrier.utilities.AndroidMultiPartEntity;
import info.alchemistdigital.e_carrier.utilities.CommonUtilities;

/**
 * Created by user on 1/2/2016.
 */
public class UserInfoInsertAsyncTask extends AsyncTask<String, String, String> {
    private Context context;
    private String name;
    private String email;
    private String contactNo;
    private String pwd;

    // Progress Dialog
    private ProgressDialog pDialog;


    public UserInfoInsertAsyncTask(Context context, String name, String email, String contactNo, String pwd) {
        this.context    = context;
        this.name       = name;
        this.email      = email;
        this.contactNo  = contactNo;
        this.pwd        = pwd;

    }

    /**
     * Before starting background thread Show Progress Dialog
     * */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        ((Activity)context).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);

        pDialog = new ProgressDialog(context);
        pDialog.setMessage("Creating Product..");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }


    @Override
    protected String doInBackground(String... params) {
        String serverUrl = CommonUtilities.USER_LOGIN_SERVER_URL;
        String responseString = null;

        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(serverUrl);

        try {
            AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                    new AndroidMultiPartEntity.ProgressListener() {

                        @Override
                        public void transferred(long num) {
//                            publishProgress((int) ((num / (float) totalSize) * 100));
                        }
                    });

            // Adding file data to http body
            entity.addPart("fullName", new StringBody(name));
            entity.addPart("emailId", new StringBody(email));
            entity.addPart("phoneNo", new StringBody(contactNo));
            entity.addPart("password", new StringBody(pwd));
            entity.addPart("userType", new StringBody("customer"));

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



    /**
     * After completing background task Dismiss the progress dialog
     * **/
    protected void onPostExecute(String result) {
        // dismiss the dialog once done
        pDialog.dismiss();

    }
}
