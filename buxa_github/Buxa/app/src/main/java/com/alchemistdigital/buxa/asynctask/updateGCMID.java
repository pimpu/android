package com.alchemistdigital.buxa.asynctask;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import com.alchemistdigital.buxa.R;
import com.alchemistdigital.buxa.sharedprefrencehelper.SetSharedPreference;
import com.alchemistdigital.buxa.utilities.CommonUtilities;
import com.alchemistdigital.buxa.utilities.CommonVariables;
import com.alchemistdigital.buxa.utilities.RestClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by user on 10/8/2016.
 */
public class UpdateGCMID {
    public UpdateGCMID(Context context, String regId, int uId) {
        RequestParams params;
        params = new RequestParams();

        params.put("registerId", regId);
        params.put("userId", uId);

        invokeWS(context, params);
    }

    private void invokeWS(final Context context, RequestParams params) {

        // Make RESTful webservice call using AsyncHttpClient object
        RestClient.put(CommonVariables.UPDATE_GCM_REGISTERED_ID_SERVER_URL, params, new JsonHttpResponseHandler() {
            // When the response returned by REST has Http response code '200'

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
                try{
                    Boolean error = json.getBoolean(CommonVariables.TAG_ERROR);

                    if (error) {
                        Toast.makeText(context, json.getString(CommonVariables.TAG_MESSAGE), Toast.LENGTH_LONG).show();
                        System.out.println(json.getString(CommonVariables.TAG_MESSAGE));
                    }
                    else {
                        System.out.println(json.getString(CommonVariables.TAG_MESSAGE));

                        // get All shipment type
                        GetAllShipmentType.getShipmentType(context, CommonVariables.QUERY_SHIPMENT_TYPE_SERVER_URL);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                System.out.println("status code: "+statusCode);
                System.out.println("responseString: "+responseString);
                Toast.makeText(context, "Error "+statusCode+" : "+responseString, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
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
                        if( errorResponse.getBoolean("error") ) {
                            System.out.println(errorResponse.getString("message"));
                            Toast.makeText(context, errorResponse.getString("message"),Toast.LENGTH_LONG).show();
                        }
                        else {
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
