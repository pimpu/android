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
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import info.alchemistdigital.e_carrier.activity.DriverViewActivity;
import info.alchemistdigital.e_carrier.utilities.AndroidMultiPartEntity;
import info.alchemistdigital.e_carrier.utilities.CommonUtilities;

/**
 * Created by user on 1/14/2016.
 */
public class SentEnquiryConformReply extends AsyncTask<String,String,String>{
    private int enquiryId;
    private int driverId;
    private Context context;

    public SentEnquiryConformReply(DriverViewActivity context, int enquiryId, int driverId) {
        this.enquiryId=enquiryId;
        this.driverId=driverId;
        this.context = context;

    }

    @Override
    protected String doInBackground(String... params) {
        String serverUrl = CommonUtilities.DRIVER_ENQUIRY_RESPONCE_SERVER_URL;
        String strEnquiry = String.valueOf(enquiryId);
        String strDriver  = String.valueOf(driverId);
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
            entity.addPart("enquiryId", new StringBody(strEnquiry));
            entity.addPart("driverId", new StringBody(strDriver));

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
        try {
            Log.d("sentEnquiryConfoReply:",result.toString());

            if(result.contains("Error occurred!")) {
                Toast.makeText(context, result, Toast.LENGTH_LONG).show();
                return;
            }

            JSONObject json = new JSONObject(result);

            int success = json.getInt(CommonUtilities.TAG_SUCCESS);

            if(success == 3){
                String message = json.getString(CommonUtilities.TAG_MESSAGE);
                Toast.makeText(context,message,Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
