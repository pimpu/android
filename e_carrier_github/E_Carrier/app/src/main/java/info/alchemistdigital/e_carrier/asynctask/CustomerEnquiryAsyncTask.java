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
import java.text.ParseException;
import java.util.ArrayList;

import info.alchemistdigital.e_carrier.activity.MainActivity;
import info.alchemistdigital.e_carrier.utilities.AndroidMultiPartEntity;
import info.alchemistdigital.e_carrier.utilities.CommonUtilities;
import info.alchemistdigital.e_carrier.utilities.DateHelper;

/**
 * Created by user on 1/9/2016.
 */
public class CustomerEnquiryAsyncTask extends AsyncTask<String, String, String>{
    // Progress Dialog
    private ProgressDialog pDialog;
    Context context;
    int userId;
    String fromArea,toArea,date,time,weight,unit,type,vehicle,pickup,delivery,strDateTime,lats,longs,extraManPwr;
    ArrayList<String> vas;

    public CustomerEnquiryAsyncTask(MainActivity context, int userId, String fromArea,
                                    String toArea, String date, String time, String weight,
                                    String unit, String type, String vehicle, ArrayList<String> vas,
                                    String pickup, String delivery, String lats,String longs,String extraManPwr) {
        this.context     = context;
        this.userId      = userId;
        this.fromArea    = fromArea;
        this.toArea      = toArea;
        this.date        = date;
        this.time        = time;
        this.weight      = weight;
        this.unit        = unit;
        this.type        = type;
        this.vehicle     = vehicle;
        this.vas         = vas;
        this.pickup      = pickup;
        this.delivery    = delivery;
        this.lats        = lats;
        this.longs       = longs;
        this.extraManPwr = extraManPwr;

        if(this.extraManPwr.length() <= 0){
            this.extraManPwr = "0";
        }
        String str = date+" "+time; //Your String containing a date
        long milisDate = 0;
        try {
            milisDate = DateHelper.convertToMillis(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        strDateTime    = String.valueOf(milisDate);

    }

    /**
     * Before starting background thread Show Progress Dialog
     * */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        ((Activity)context).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);

        pDialog = new ProgressDialog(context);
        pDialog.setMessage("sending enquiry..");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    @Override
    protected String doInBackground(String... params) {
        String serverUrl = CommonUtilities.UPLOAD_ENQUIRY_SERVER_URL;
        String responseString = null;

        String loginId = String.valueOf(userId);
        String VAS     = String.valueOf(vas);
        VAS            = VAS.replaceAll("\\s+","");

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
            entity.addPart("fk_userId", new StringBody(loginId));
            entity.addPart("beginningArea", new StringBody(fromArea));
            entity.addPart("destinationArea", new StringBody(toArea));
            entity.addPart("deliveryDateTime", new StringBody(strDateTime));
            entity.addPart("weight", new StringBody(weight));
            entity.addPart("unit", new StringBody(unit));
            entity.addPart("vehicleType", new StringBody(type));
            entity.addPart("vehicle", new StringBody(vehicle));
            entity.addPart("valueAddedServices", new StringBody(VAS));
            entity.addPart("pickupAddress", new StringBody(pickup));
            entity.addPart("deliveryAddress", new StringBody(delivery));
            entity.addPart("lats", new StringBody(lats));
            entity.addPart("longs", new StringBody(longs));
            entity.addPart("extraManPwr", new StringBody(extraManPwr));

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
