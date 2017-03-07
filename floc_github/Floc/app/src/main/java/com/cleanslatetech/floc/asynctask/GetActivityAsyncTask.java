package com.cleanslatetech.floc.asynctask;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.cleanslatetech.floc.R;
import com.cleanslatetech.floc.utilities.CommonUtilities;
import com.cleanslatetech.floc.utilities.CommonVariables;
import com.cleanslatetech.floc.utilities.PopulateFloDescData;
import com.cleanslatetech.floc.utilities.RestClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by pimpu on 3/2/2017.
 * this api call when floc description page is open
 * api return result of latest data on this event
 */
public class GetActivityAsyncTask {
//    private final RelativeLayout rlProgress;
    private Context context;
    private int eventId, iUSerId;
//    private AppCompatTextView btntv_Retry;
//    private ProgressBar prgDlg;
//    private AppCompatTextView tvAsyncText;

    public GetActivityAsyncTask(Context context, int eventId,
                                AppCompatTextView tvAsyncText,
                                int iUSerId) {
        this.context = context;
        this.eventId = eventId;
        this.iUSerId = iUSerId;
//        this.tvAsyncText = tvAsyncText;

        /*prgDlg = (ProgressBar) ((AppCompatActivity)context).findViewById(R.id.prgdlgGetAcivity);
        btntv_Retry = (AppCompatTextView) ((AppCompatActivity)context).findViewById(R.id.onClickRetryGetActivity);
        rlProgress = (RelativeLayout) ((AppCompatActivity)context).findViewById(R.id.id_getactivity_progress_layout);
        rlProgress.setVisibility(View.VISIBLE);

        btntv_Retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btntv_Retry.setVisibility(View.GONE);
                getData();
            }
        });*/
    }

    public void getData() {
//        prgDlg.setVisibility(View.VISIBLE);
//        tvAsyncText.setText("Getting latest update on this event.");

        RequestParams params;
        params = new RequestParams();
        params.put("EventId", eventId);

        invokeWS(context, params);
    }

    private void invokeWS(final Context context, RequestParams params) {
        // Make RESTful webservice call using AsyncHttpClient object
        RestClient.get(CommonVariables.GET_EVENT_DETAILS_SERVER_URL, params, new JsonHttpResponseHandler() {

            // When the response returned by REST has Http response code '200'
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
//                prgDlg.setVisibility(View.GONE);
//                rlProgress.setVisibility(View.GONE);

//                System.out.println("GetActivity: "+json);

                new PopulateFloDescData(context, json, iUSerId, CommonVariables.ACTIVITY).populatesData();

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                prgDlg.setVisibility(View.GONE);
//                rlProgress.setVisibility(View.GONE);

                System.out.println("status code: "+statusCode);
                System.out.println("responseString: "+responseString);
                CommonUtilities.customToast(context, "Error "+statusCode+" : "+responseString);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
//                prgDlg.setVisibility(View.GONE);

                // When Http response code is '404'
                if (statusCode == 404) {
                    System.out.println("Requested resource not found");
                    CommonUtilities.customToast(context, "Requested resource not found");

//                    rlProgress.setVisibility(View.GONE);
                }
                // When Http response code is '500'
                else if (statusCode == 500) {
                    System.out.println("Something went wrong at server end");
                    CommonUtilities.customToast(context, "Something went wrong at server end");

//                    rlProgress.setVisibility(View.GONE);
                }
                // When Http response code other than 404, 500
                else {
                    try {
                        System.out.println(errorResponse);

                        if (errorResponse == null) {
                            CommonUtilities.customToast(context,"Sorry for inconvenience. Please, Try again.");

//                            btntv_Retry.setVisibility(View.VISIBLE);

                            return;
                        }

//                        rlProgress.setVisibility(View.GONE);

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
