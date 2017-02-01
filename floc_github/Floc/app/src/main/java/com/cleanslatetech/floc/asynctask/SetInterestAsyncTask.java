package com.cleanslatetech.floc.asynctask;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

import com.cleanslatetech.floc.R;
import com.cleanslatetech.floc.activities.SelectInterestsActivity;
import com.cleanslatetech.floc.sharedprefrencehelper.GetSharedPreference;
import com.cleanslatetech.floc.utilities.CommonVariables;
import com.cleanslatetech.floc.utilities.RestClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by pimpu on 1/31/2017.
 */
public class SetInterestAsyncTask {
    private Context context;
    private ArrayList<Integer> iArraySelectedPositions;
    private static ProgressDialog prgDialog;

    public SetInterestAsyncTask(Context context, List<Integer> iArraySelectedPositions) {
        this.context = context;
        this.iArraySelectedPositions = (ArrayList<Integer>) iArraySelectedPositions;
    }

    public void postData() {
        // Instantiate Progress Dialog object
        prgDialog = new ProgressDialog(context);
        // Set Progress Dialog Text
        prgDialog.setMessage("posting interest ...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);

        RequestParams params;
        params = new RequestParams();
        params.put("UserId", new GetSharedPreference(context).getInt(context.getResources().getString(R.string.shrdLoginId)));
        params.put("Interest", iArraySelectedPositions );
        System.out.println(new GetSharedPreference(context).getInt(context.getResources().getString(R.string.shrdLoginId)));
        System.out.println(iArraySelectedPositions);
        invokeWS(context, params);
    }

    private void invokeWS(final Context context, RequestParams params) {
        // Show Progress Dialog
        prgDialog.show();

        // Make RESTful webservice call using AsyncHttpClient object
        RestClient.post(CommonVariables.SET_USER_INTEREST_SERVER_URL, params, new JsonHttpResponseHandler() {
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
                            Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
                        }
                    } else {

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
                Toast.makeText(context, "Error "+statusCode+" : "+responseString, Toast.LENGTH_LONG).show();
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
                        System.out.println(errorResponse);
                        if (errorResponse == null) {
                            Toast.makeText(context,"Sorry for inconvenience. Please, Try again.",Toast.LENGTH_LONG).show();
                            return;
                        }

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
