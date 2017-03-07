package com.cleanslatetech.floc.asynctask;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.cleanslatetech.floc.activities.CreateFlocActivity;
import com.cleanslatetech.floc.activities.HomeActivity;
import com.cleanslatetech.floc.utilities.CommonUtilities;
import com.cleanslatetech.floc.utilities.CommonVariables;
import com.cleanslatetech.floc.utilities.RestClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by pimpu on 3/5/2017.
 */
public class GetAllRecentEventAsyncTask {
    Context context;
    private JSONArray jsonArrayGetEvents;
    private ProgressDialog prgDig;

    public GetAllRecentEventAsyncTask(Context context, JSONArray jsonArrayGetEvents, ProgressDialog prgDig) {
        this.context = context;
        this.jsonArrayGetEvents = jsonArrayGetEvents;
        this.prgDig = prgDig;
    }

    public void getData() {
        // Make RESTful webservice call using AsyncHttpClient object
        RestClient.get(CommonVariables.GET_ALL_RECENT_SERVER_URL, null, new JsonHttpResponseHandler() {
            // When the response returned by REST has Http response code '200'

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
                try{
                    System.out.println("GetAllRecent: "+json);
                    // when Get all events comes from home activity, then progress dialog is null
                    // and come from Create floc, progress dialog of creating floc will continue upto here.
                    if(prgDig != null) {
                        prgDig.dismiss();
                        // finish fisrt page of create floc activity.
                        CreateFlocActivity.getInstance().finish();
                        // finish second/latest page of create floc activity.
                        ((AppCompatActivity)context).finish();
                    }

                    HomeActivity.interfaceAllRecentAndCurrentEvent.getAllRecent(json.getJSONArray("Event"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }



            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                System.out.println("status code: "+statusCode);
                System.out.println("responseString: "+responseString);
                CommonUtilities.customToast(context, "Error "+statusCode+" : "+responseString);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                // When Http response code is '404'
                if (statusCode == 404) {
                    System.out.println("Requested resource not found");
                    CommonUtilities.customToast(context, "Requested resource not found");
                }
                // When Http response code is '500'
                else if (statusCode == 500) {
                    System.out.println("Something went wrong at server end");
                    CommonUtilities.customToast(context, "Something went wrong at server end");
                }
                // When Http response code other than 404, 500
                else {
                    try {
                        System.out.println(errorResponse);

                        if (errorResponse == null) {
                            CommonUtilities.customToast(context,"Sorry for inconvenience. Please, Try again.");
                            return;
                        }

                        if( errorResponse.getBoolean("error") ) {
                            System.out.println(errorResponse.getString("message"));
                            CommonUtilities.customToast(context, errorResponse.getString("message"));
                        }
                        else {
                            System.out.println("Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]");
                            CommonUtilities.customToast(context, "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
