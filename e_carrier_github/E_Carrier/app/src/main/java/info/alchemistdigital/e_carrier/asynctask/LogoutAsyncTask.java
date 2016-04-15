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
 * Created by user on 1/9/2016.
 */
public class LogoutAsyncTask extends AsyncTask<String, String, String> {
    // Progress Dialog
     private ProgressDialog pDialog;
    private Context context;
    private int userId;

    public LogoutAsyncTask(Context context, int userId) {
        this.context = context;
        this.userId  = userId;

    }

    /**
     * Before starting background thread Show Progress Dialog
     * */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        ((Activity)context).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);

        pDialog = new ProgressDialog(context);
        pDialog.setMessage("Logout..");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    @Override
    protected String doInBackground(String... params) {
        String serverUrl = CommonUtilities.LOGOT_SERVER_URL;
        String strLoginId = String.valueOf(userId);
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
            entity.addPart("userId", new StringBody(strLoginId));

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
    @Override
    protected void onPostExecute(String result) {
        pDialog.dismiss();

    }
}
