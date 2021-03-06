package com.cleanslatetech.floc.asynctask;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import com.cleanslatetech.floc.activities.CreateFlocActivity;
import com.cleanslatetech.floc.models.ChannelModel;
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
 * Created by pimpu on 4/18/2017.
 */

class CreatePlatformAsyncTask {
    private ProgressDialog prgDialog;
    private Context context;
    private ChannelModel channelModel;

    public CreatePlatformAsyncTask(Context context, ChannelModel channelModel, ProgressDialog prgDialog) {
        this.context = context;
        this.channelModel = channelModel;
        this.prgDialog = prgDialog;
    }

    public void postData() {
        RequestParams params = new RequestParams();

        params.put("ChannelName", channelModel.getChannelName());
        params.put("ChannelOwner", channelModel.getChannelOwner());
        params.put("CreateDate", channelModel.getCreateDate());
        params.put("ChannelAbout", channelModel.getChannelAbout());
        params.put("ChannelImage", channelModel.getChannelImage());
        params.put("PlatformType", channelModel.getPlatformType());
        params.put("ChannelType", channelModel.getChannelType());
        params.put("ChannelPassword", channelModel.getChannelPassword());
        params.put("ChannelStatus", channelModel.getChannelStatus());
        params.put("ChannelLink", "");

        invokeWS(context, params);
    }

    private void invokeWS(final Context context, RequestParams params) {
        // Make RESTful webservice call using AsyncHttpClient object
        RestClient.post(CommonVariables.CREATE_PLATOFRM_SERVER_URL, params, new JsonHttpResponseHandler() {
            // When the response returned by REST has Http response code '200'

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
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
                        prgDialog.dismiss();

                        // finish second/latest page of create floc activity.
                        ((AppCompatActivity)context).finish();
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
//                            CommonUtilities.customToast(context,"Sorry for inconvenience. Please, Try again.");
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
