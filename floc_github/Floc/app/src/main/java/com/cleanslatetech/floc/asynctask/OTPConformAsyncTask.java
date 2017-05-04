package com.cleanslatetech.floc.asynctask;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;

import com.cleanslatetech.floc.activities.OTPActivity;
import com.cleanslatetech.floc.utilities.CommonUtilities;
import com.cleanslatetech.floc.utilities.CommonVariables;
import com.cleanslatetech.floc.utilities.DateHelper;
import com.cleanslatetech.floc.utilities.RestClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import cz.msebera.android.httpclient.Header;

/**
 * Created by pimpu on 1/20/2017.
 */
public class OTPConformAsyncTask {
    private ProgressDialog prgDialog;
    private Context context;
    private String email, pwd, cnfrmPwd, OTP;
    private String response;

    public OTPConformAsyncTask(Context context, String email, String pwd, String cnfrmPwd) {
        this.context = context;
        this.email = email;
        this.pwd = pwd;
        this.cnfrmPwd = cnfrmPwd;

        Date dates = new Date();

        String minute = DateHelper.getMinute(dates.getTime());
        String second = DateHelper.getSecond(dates.getTime());
        OTP = minute + second;

        // Instantiate Progress Dialog object
        prgDialog = new ProgressDialog(context);
        // Set Progress Dialog Text
        prgDialog.setMessage("Wait ...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);

    }

    public void postData() {


        RequestParams params;
        params = new RequestParams();
        params.put("UserName", email);
        params.put("EmailId", email);
        params.put("OTP", OTP);

        invokeWS(context, params);
    }

    private void invokeWS(final Context context, RequestParams params) {
        // Show Progress Dialog
        prgDialog.show();

        // Make RESTful webservice call using AsyncHttpClient object
        RestClient.get(CommonVariables.OTP_CONFIRM_SERVER_URL, params, new JsonHttpResponseHandler() {
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
                        Intent intent = new Intent(context, OTPActivity.class);
                        intent.putExtra("email", email);
                        intent.putExtra("pwd", pwd);
                        intent.putExtra("cnfrmPwd", cnfrmPwd);
                        intent.putExtra("OTP", OTP);
                        context.startActivity(intent);
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
