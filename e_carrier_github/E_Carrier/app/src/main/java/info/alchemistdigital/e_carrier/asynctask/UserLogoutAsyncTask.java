package info.alchemistdigital.e_carrier.asynctask;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import info.alchemistdigital.e_carrier.utilities.CommonUtilities;
import info.alchemistdigital.e_carrier.utilities.JSONParser;

/**
 * Created by user on 1/9/2016.
 */
public class UserLogoutAsyncTask extends AsyncTask<String, String, JSONObject> {
    // Progress Dialog
     private ProgressDialog pDialog;

    private static JSONParser jsonParser = new JSONParser();
    private Context context;
    private int userId;

    public UserLogoutAsyncTask(Context context, int userId) {
        this.context = context;
        this.userId  = userId;

    }

    /**
     * Before starting background thread Show Progress Dialog
     * */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pDialog = new ProgressDialog(context);
        pDialog.setMessage("Logout..");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    @Override
    protected JSONObject doInBackground(String... params) {
        String serverUrl = CommonUtilities.LOGOT_SERVER_URL;

        // Building Parameters
        List<NameValuePair> param = new ArrayList<NameValuePair>();
        String strLoginId = String.valueOf(userId);
        param.add(new BasicNameValuePair("userId",strLoginId));

        JSONObject json = jsonParser.makeHttpRequest(serverUrl,"POST", param);
        return json;
    }

    /**
     * After completing background task Dismiss the progress dialog
     * **/
    @Override
    protected void onPostExecute(JSONObject result) {
        pDialog.dismiss();

    }
}
