package com.alchemistdigital.buxa.asynctask;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

import com.alchemistdigital.buxa.DBHelper.DatabaseClass;
import com.alchemistdigital.buxa.R;
import com.alchemistdigital.buxa.activities.EnquiriesActivity;
import com.alchemistdigital.buxa.sharedprefrencehelper.GetSharedPreference;
import com.alchemistdigital.buxa.utilities.CommonVariables;
import com.alchemistdigital.buxa.utilities.RestClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Pimpu on 11/17/2016.
 */
public class EnquiryAcceptAsyncTask {
    private static ProgressDialog prgDialog;
    private static GetSharedPreference getSharedPreference;
    private static DatabaseClass dbHelper ;

    public static void acceptAsyncTask(Context context, String bookingId) {
        dbHelper = new DatabaseClass(context);

        // Instantiate Progress Dialog object
        prgDialog = new ProgressDialog(context);
        // Set Progress Dialog Text
        prgDialog.setMessage("Updating ...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);

        getSharedPreference = new GetSharedPreference(context);

        RequestParams params;
        params = new RequestParams();
        params.put("bookingId", bookingId);
        params.put("status", 4);

        invokeWS(context, params, bookingId);
    }

    private static void invokeWS(final Context context, RequestParams params, final String bookingId) {
        // Show Progress Dialog
        prgDialog.show();

        String apiKeyHeader = getSharedPreference.getApiKey(context.getResources().getString(R.string.apikey));

        // Make RESTful webservice call using AsyncHttpClient object
        RestClient.postWithHeader(CommonVariables.ACCPET_ENQUIRY_SERVER_URL, apiKeyHeader, params, new JsonHttpResponseHandler() {
            // When the response returned by REST has Http response code '200'

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
                prgDialog.cancel();

                try {
                    System.out.println(json);
                    Boolean error = json.getBoolean(CommonVariables.TAG_ERROR);

                    if (error) {
                        Toast.makeText(context, json.getString(CommonVariables.TAG_MESSAGE), Toast.LENGTH_LONG).show();
                        System.out.println(json.getString(CommonVariables.TAG_MESSAGE));
                    } else {

                        boolean i = dbHelper.updateShipmentConformTable(bookingId, 4);
                        System.out.println("updateShipmentConformTable( accept ): " + i);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                prgDialog.cancel();
                System.out.println("status code: " + statusCode);
                System.out.println("responseString: " + responseString);
                Toast.makeText(context, "Error " + statusCode + " : " + responseString, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                prgDialog.cancel();
                // When Http response code is '404'
                if (statusCode == 404) {
                    System.out.println("Requested resource not found");
                    Toast.makeText(context, "Requested resource not found", Toast.LENGTH_LONG).show();
                }
                // When Http response code is '500'
                else if (statusCode == 500) {
                    System.out.println("Something went wrong at server end");
                    Toast.makeText(context, "Something went wrong at server end", Toast.LENGTH_LONG).show();
                }
                // When Http response code other than 404, 500
                else {
                    try {
                        if (errorResponse.getBoolean("error")) {
                            System.out.println(errorResponse.getString("message"));
                            Toast.makeText(context, errorResponse.getString("message"), Toast.LENGTH_LONG).show();
                        } else {
                            System.out.println("Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]");
                            Toast.makeText(context, "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]", Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

        });
    }
}
