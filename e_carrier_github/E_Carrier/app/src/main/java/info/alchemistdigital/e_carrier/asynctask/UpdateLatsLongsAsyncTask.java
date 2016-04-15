package info.alchemistdigital.e_carrier.asynctask;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

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
 * Created by user on 1/19/2016.
 */
public class UpdateLatsLongsAsyncTask extends AsyncTask<String,String,String>{
    private String latitude,longitude,loginID,strPostalCode;
    private Context context;

    public UpdateLatsLongsAsyncTask(Context context, double latitude, double longitude, int loginID, String strPostalCode) {
        this.context       = context;
        this.latitude      = String.valueOf(latitude);
        this.longitude     = String.valueOf(longitude);
        this.loginID       = String.valueOf(loginID);
        this.strPostalCode = strPostalCode;
    }

    @Override
    protected String doInBackground(String... params) {
        String serverUrl = CommonUtilities.UPDATE_LATS_LONGS_SERVER_URL;
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
            entity.addPart("lat", new StringBody(latitude));
            entity.addPart("longs", new StringBody(longitude));
            entity.addPart("driverId", new StringBody(loginID));
            entity.addPart("postal_code", new StringBody(strPostalCode));

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
        if(result != null){
            try {
                Log.d("updateLatsLongs:", result.toString());

                if(result.contains("Error occurred!")){
                    Toast.makeText(context, result, Toast.LENGTH_LONG).show();
                    return;
                }

                JSONObject json = new JSONObject(result);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
