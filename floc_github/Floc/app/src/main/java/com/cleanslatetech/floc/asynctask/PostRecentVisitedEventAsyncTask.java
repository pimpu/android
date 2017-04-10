package com.cleanslatetech.floc.asynctask;

import android.content.Context;

import com.cleanslatetech.floc.utilities.CommonUtilities;
import com.cleanslatetech.floc.utilities.CommonVariables;
import com.cleanslatetech.floc.utilities.RestClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by pimpu on 4/10/2017.
 */

public class PostRecentVisitedEventAsyncTask {

    private Context context;
    private int iUSerId, iEventId, iCategoryId;

    public PostRecentVisitedEventAsyncTask(Context context, int iUSerId, int iEventId, int iCategoryId) {
        this.context = context;
        this.iUSerId = iUSerId;
        this.iEventId = iEventId;
        this.iCategoryId = iCategoryId;

    }

    public void postData() {
        RequestParams params = new RequestParams();

        params.put("id", "");
        params.put("UserId", iUSerId);
        params.put("EventId", iEventId);
        params.put("CategoryId", iCategoryId);
        params.put("Date", "");

        invokeWS(params);
    }

    private void invokeWS(RequestParams params) {
        // Make RESTful webservice call using AsyncHttpClient object
        RestClient.post(CommonVariables.POST_RECENT_VISITED_SERVER_URL, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
                System.out.println("PostRecentVisitedEventAsyncTask: "+json);

                new GetAllRecentEventAsyncTask(context, null).getData();
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
                            postData();
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
