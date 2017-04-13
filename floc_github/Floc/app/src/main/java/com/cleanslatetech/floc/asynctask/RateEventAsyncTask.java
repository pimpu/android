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
 * Created by pimpu on 3/4/2017.
 */
public class RateEventAsyncTask {
    private final ProgressBar prgDlg;
    private final RelativeLayout rlProgressLayout;
    private final AppCompatTextView btntv_Retry;
    private Context context;
    private AppCompatTextView tvAsyncText;
    private int iActivityIdRating, iUSerId, iEventId, iCreaterId, iRate;

    public RateEventAsyncTask(Context context, AppCompatTextView tvAsyncText,
                              int iActivityIdRating, int iUSerId, int iEventId, int iCreaterId, int iRate) {

        this.context = context;
        this.tvAsyncText = tvAsyncText;
        this.iActivityIdRating = iActivityIdRating;
        this.iUSerId = iUSerId;
        this.iEventId = iEventId;
        this.iCreaterId = iCreaterId;
        this.iRate = iRate;

        prgDlg = (ProgressBar) ((AppCompatActivity)context).findViewById(R.id.prgdlgGetAcivity);
        rlProgressLayout = (RelativeLayout) ((AppCompatActivity) context).findViewById(R.id.id_getactivity_progress_layout);
        btntv_Retry = (AppCompatTextView) ((AppCompatActivity)context).findViewById(R.id.onClickRetryGetActivity);

        btntv_Retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btntv_Retry.setVisibility(View.GONE);
                postData();
            }
        });

        rlProgressLayout.setVisibility(View.VISIBLE);
        tvAsyncText.setText("Event loading ...");
        prgDlg.setVisibility(View.VISIBLE);
    }

    public void postData() {

        RequestParams params;
        params = new RequestParams();
        params.put("ActivityId", iActivityIdRating);
        params.put("UserId", iUSerId);
        params.put("EventId", iEventId);
        params.put("CreatorId", iCreaterId);
        params.put("Rate", iRate);

        invokeWS(context, params);
    }

    private void invokeWS(final Context context, RequestParams params) {
        // Make RESTful webservice call using AsyncHttpClient object
        RestClient.post(CommonVariables.EVENT_RATING_SERVER_URL, params, new JsonHttpResponseHandler() {

            // When the response returned by REST has Http response code '200'
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
                prgDlg.setVisibility(View.GONE);
                rlProgressLayout.setVisibility(View.GONE);

                System.out.println("rate event: "+json);

                new PopulateFloDescData(context, json, iUSerId, CommonVariables.RATE).populatesData();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                prgDlg.setVisibility(View.GONE);
                rlProgressLayout.setVisibility(View.GONE);

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

                    prgDlg.setVisibility(View.GONE);
                    rlProgressLayout.setVisibility(View.GONE);
                }
                // When Http response code is '500'
                else if (statusCode == 500) {
                    System.out.println("Something went wrong at server end");
                    CommonUtilities.customToast(context, "Something went wrong at server end");

                    prgDlg.setVisibility(View.GONE);
                    rlProgressLayout.setVisibility(View.GONE);
                }
                // When Http response code other than 404, 500
                else {
                    try {
                        System.out.println(errorResponse);

                        if (errorResponse == null) {
                            /*CommonUtilities.customToast(context,"Sorry for inconvenience. Please, Try again.");
                            btntv_Retry.setVisibility(View.VISIBLE);*/
                            postData();

                            return;
                        }

                        prgDlg.setVisibility(View.GONE);
                        rlProgressLayout.setVisibility(View.GONE);

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
