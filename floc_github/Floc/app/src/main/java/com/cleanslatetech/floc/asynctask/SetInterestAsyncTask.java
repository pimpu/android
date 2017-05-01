package com.cleanslatetech.floc.asynctask;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import com.cleanslatetech.floc.R;
import com.cleanslatetech.floc.activities.HomeActivity;
import com.cleanslatetech.floc.sharedprefrencehelper.GetSharedPreference;
import com.cleanslatetech.floc.sharedprefrencehelper.SetSharedPreference;
import com.cleanslatetech.floc.utilities.CommonUtilities;
import com.cleanslatetech.floc.utilities.CommonVariables;
import com.cleanslatetech.floc.utilities.RestClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by pimpu on 1/31/2017.
 */
public class SetInterestAsyncTask {
    private Context context;
    private ArrayList<Integer> iArraySelectedPositions;
    private static ProgressDialog prgDialog;
    private int userId;

    public SetInterestAsyncTask(
            Context context,
            List<Integer> iArraySelectedPositions) {
        this.context = context;
        this.iArraySelectedPositions = (ArrayList<Integer>) iArraySelectedPositions;

        userId = new GetSharedPreference(context).getInt(context.getResources().getString(R.string.shrdLoginId));

        // Instantiate Progress Dialog object
        prgDialog = new ProgressDialog(context);
        // Set Progress Dialog Text
        prgDialog.setMessage("posting interest ...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);
    }

    public void postData() {

        RequestParams params;
        params = new RequestParams();
        params.put("UserId", userId);
        params.put("Interest", iArraySelectedPositions);
        invokeWS(context, params);
    }

    private void invokeWS(final Context context, RequestParams params) {
        // Show Progress Dialog
        prgDialog.show();

        // Make RESTful webservice call using AsyncHttpClient object
        RestClient.get(CommonVariables.SET_USER_INTEREST_SERVER_URL, params, new JsonHttpResponseHandler() {
            // When the response returned by REST has Http response code '200'

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
                prgDialog.cancel();
                try{
                    System.out.println(json);

                    Boolean error = json.getBoolean(CommonVariables.TAG_ERROR);
                    JSONArray jsonArray = json.getJSONArray(CommonVariables.TAG_MESSAGE);

                    if (error) {
                        for( int i = 0 ; i < jsonArray.length(); i++) {
                            String msg = jsonArray.getJSONObject(i).getString(CommonVariables.TAG_MESSAGE_OBJ);
                            System.out.println(msg);
                            CommonUtilities.customToast(context, msg);
                        }
                    } else {
                        String msg = jsonArray.getJSONObject(0).getString(CommonVariables.TAG_MESSAGE_OBJ);
//                        CommonUtilities.customToast(context, msg);
                        if(msg.equals("Success")) {
                            SetSharedPreference setSharedPreference = new SetSharedPreference(context);
                            setSharedPreference.setBoolean(context.getResources().getString(R.string.shrdIsInterestSelected), true);
                            setSharedPreference.setStringSet(context.getResources().getString(R.string.shrdSelectedCategory), iArraySelectedPositions);

                            context.startActivity(new Intent(context, HomeActivity.class));
                            ((AppCompatActivity)context).finish();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                prgDialog.cancel();
                System.out.println("status code: "+statusCode);
                System.out.println("responseString: "+responseString);
                CommonUtilities.customToast(context, "Error "+statusCode+" : "+responseString);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

                // When Http response code is '404'
                if (statusCode == 404) {
                    prgDialog.cancel();
                    System.out.println("Requested resource not found");
                    CommonUtilities.customToast(context, "Requested resource not found");
                }
                // When Http response code is '500'
                else if (statusCode == 500) {
                    prgDialog.cancel();
                    System.out.println("Something went wrong at server end");
                    CommonUtilities.customToast(context, "Something went wrong at server end");
                }
                // When Http response code other than 404, 500
                else {
                    try {
                        System.out.println(errorResponse);
                        if (errorResponse == null) {
                            /*CommonUtilities.customToast(context,"Sorry for inconvenience. Please, Try again.");*/
                            postData();
                            return;
                        }

                        prgDialog.cancel();

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
