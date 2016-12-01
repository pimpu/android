package com.alchemistdigital.buxa.asynctask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alchemistdigital.buxa.activities.EmailConformationActivity;
import com.alchemistdigital.buxa.utilities.CommonVariables;
import com.alchemistdigital.buxa.utilities.DateHelper;
import com.alchemistdigital.buxa.utilities.RestClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Pimpu on 11/29/2016.
 */
public class ConformEmailAsyncTask {
    Context context;
    private String company, name, mobile, email, pwd, currentTime, OTP;
    private ProgressDialog dialog;
    LinearLayout layout_noConnection;
    TextView errorMessage;

    public ConformEmailAsyncTask(Context context, String company, String name, String mobile, String email, String pwd, String currentTime, LinearLayout layout_noConnection, TextView errorMessage) {
        this.context = context;
        this.company = company;
        this.name = name;
        this.mobile = mobile;
        this.email = email;
        this.pwd = pwd;
        this.currentTime = currentTime;
        this.layout_noConnection = layout_noConnection;
        this.errorMessage = errorMessage;
    }

    public void conformEmail() {
        dialog = new ProgressDialog(context);
        dialog.setMessage("Loading ...");
        dialog.setIndeterminate(false);
        dialog.setCancelable(false);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        Date dates = new Date();

        String minute = DateHelper.getMinute(dates.getTime());
        String second = DateHelper.getSecond(dates.getTime());

        RequestParams params;
        params = new RequestParams();

        OTP = minute + second;

        params.put("otp", OTP);
        params.put("email", email );
        params.put("name", name );

        invokeWS(context, params);
    }

    private void invokeWS(final Context context, RequestParams params) {
        // Show Progress Dialog
        dialog.show();

        // Make RESTful webservice call using AsyncHttpClient object
        RestClient.post(CommonVariables.VERIFY_EMAIL_SERVER_URL, params, new JsonHttpResponseHandler() {
            // When the response returned by REST has Http response code '200'

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
                dialog.dismiss();
                try{
                    Boolean error = json.getBoolean(CommonVariables.TAG_ERROR);

                    if (error) {
//                        Toast.makeText(context, json.getString(CommonVariables.TAG_MESSAGE), Toast.LENGTH_LONG).show();
                        System.out.println(json.getString(CommonVariables.TAG_MESSAGE));

                        layout_noConnection.setVisibility(View.VISIBLE);
                        errorMessage.setText(json.getString(CommonVariables.TAG_MESSAGE));
                    }
                    else {
                        System.out.println(json.getString(CommonVariables.TAG_MESSAGE));

                        Intent goToConformEmailAct = new Intent(context, EmailConformationActivity.class);
                        goToConformEmailAct.putExtra("company", company);
                        goToConformEmailAct.putExtra("uname", name);
                        goToConformEmailAct.putExtra("mobile", mobile);
                        goToConformEmailAct.putExtra("email", email);
                        goToConformEmailAct.putExtra("password", pwd);
                        goToConformEmailAct.putExtra("create_time", currentTime);
                        goToConformEmailAct.putExtra("otp", OTP);
                        context.startActivity(goToConformEmailAct);
                        ((Activity)context).finish();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                dialog.dismiss();

                System.out.println("status code: "+statusCode);
                System.out.println("responseString: "+responseString);
                Toast.makeText(context, "Error "+statusCode+" : "+responseString, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                dialog.dismiss();

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
