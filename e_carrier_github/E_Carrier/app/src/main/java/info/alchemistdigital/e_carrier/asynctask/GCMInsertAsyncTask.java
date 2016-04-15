package info.alchemistdigital.e_carrier.asynctask;

import android.content.Context;
import android.os.AsyncTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;

import info.alchemistdigital.e_carrier.utilities.AndroidMultiPartEntity;
import info.alchemistdigital.e_carrier.utilities.CommonUtilities;

/**
 * Created by user on 1/2/2016.
 */
public class GCMInsertAsyncTask extends AsyncTask<String, String, String> {
    private Context context;
    private  String gcmId;
    private String loginId;

    public GCMInsertAsyncTask(Context context, String gcmId, String loginId) {
        this.context =context;
        this.gcmId=gcmId;
        this.loginId=loginId;

    }


    @Override
    protected String doInBackground(String... params) {
        String serverUrl = CommonUtilities.UPDATE_GCMID_SERVER_URL;
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
            entity.addPart("loginId", new StringBody(loginId));
            entity.addPart("gcmId", new StringBody(gcmId));

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
    protected void onPostExecute(JSONObject result) {
        // dismiss the dialog once done
    }
}
