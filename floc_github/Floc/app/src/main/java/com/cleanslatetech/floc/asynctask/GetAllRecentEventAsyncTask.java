package com.cleanslatetech.floc.asynctask;

import android.app.ProgressDialog;
import android.content.Context;

import com.cleanslatetech.floc.R;
import com.cleanslatetech.floc.activities.HomePageActivity;
import com.cleanslatetech.floc.sharedprefrencehelper.GetSharedPreference;
import com.cleanslatetech.floc.utilities.CommonUtilities;
import com.cleanslatetech.floc.utilities.CommonVariables;
import com.cleanslatetech.floc.utilities.RestClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by pimpu on 4/10/2017.
 */

class GetAllRecentEventAsyncTask {
    private Context context;
    private ProgressDialog prgDig;

    public GetAllRecentEventAsyncTask(Context context, ProgressDialog prgDig) {
        this.context = context;
        this.prgDig = prgDig;
    }

    public void getData() {

        RequestParams requestParams = new RequestParams();
        requestParams.put("UserId", new GetSharedPreference(context).getInt(context.getResources().getString(R.string.shrdLoginId)));

        // Make RESTful webservice call using AsyncHttpClient object
        RestClient.get(CommonVariables.GET_ALL_RECENT_SERVER_URL, requestParams, new JsonHttpResponseHandler() {
            // When the response returned by REST has Http response code '200'

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
                try{
                    System.out.println("GetAllRecent: "+json);

                    HomePageActivity.interfaceAllRecentAndCurrentEvent.getAllRecent(json.getJSONArray("Event"));

                    new GetChannelAsyncTask(context, prgDig).getData();

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
//                            CommonUtilities.customToast(context,"Sorry for inconvenience. Please, Try again.");
                            getData();
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
