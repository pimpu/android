package com.cleanslatetech.floc.asynctask;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cleanslatetech.floc.R;
import com.cleanslatetech.floc.activities.SettingActivity;
import com.cleanslatetech.floc.sharedprefrencehelper.GetSharedPreference;
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
 * Created by pimpu on 3/18/2017.
 */

public class ChangePwdAsyncTask {
    private Context context;
    private String oldText, newText, cnfrmText;
    private ProgressBar progressBar;
    private TextView textview;
    private Button pButton, nbutton;

    public ChangePwdAsyncTask(Context context, String oldText, String newText, String cnfrmText, ProgressBar progressBar, TextView textview, Button pButton, Button nbutton) {
        this.context = context;
        this.oldText = oldText;
        this.newText  = newText;
        this.cnfrmText = cnfrmText;

        this.progressBar = progressBar;
        this.textview = textview;
        this.pButton = pButton;
        this.nbutton = nbutton;
    }

    public void postData() {

        progressBar.setVisibility(View.VISIBLE);
        textview.setVisibility(View.GONE);
        pButton.setEnabled(false);
        nbutton.setEnabled(false);

        RequestParams params = new RequestParams();
        params.put("Email", new GetSharedPreference(context).getString(context.getResources().getString(R.string.shrdUserEmail)));
        params.put("OldPassword", oldText);
        params.put("NewPassword", newText);
        params.put("ConfirmPassword", cnfrmText);

        invokeWS(context, params);
    }

    private void invokeWS(final Context context, RequestParams params) {
        // Make RESTful webservice call using AsyncHttpClient object
        RestClient.post(CommonVariables.CHANGE_PASSWORD_SERVER_URL, params, new JsonHttpResponseHandler() {

            // When the response returned by REST has Http response code '200'
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
                progressBar.setVisibility(View.GONE);
                nbutton.setEnabled(true);
                pButton.setEnabled(true);

                System.out.println("change Pwd: "+json);

                try {
                    if( json.getBoolean(CommonVariables.TAG_ERROR) ) {
                        textview.setVisibility(View.VISIBLE);
                        textview.setText(json.getJSONArray(CommonVariables.TAG_MESSAGE).getJSONObject(0).getString(CommonVariables.TAG_MESSAGE_OBJ));
                    }
                    else {
                        nbutton.performClick();
                        CommonUtilities.handleIntentWhenSignOut(context, false);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                progressBar.setVisibility(View.VISIBLE);
                nbutton.setEnabled(true);
                pButton.setEnabled(true);

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

                    progressBar.setVisibility(View.VISIBLE);
                    nbutton.setEnabled(true);
                    pButton.setEnabled(true);
                }
                // When Http response code is '500'
                else if (statusCode == 500) {
                    System.out.println("Something went wrong at server end");
                    CommonUtilities.customToast(context, "Something went wrong at server end");

                    progressBar.setVisibility(View.VISIBLE);
                    nbutton.setEnabled(true);
                    pButton.setEnabled(true);
                }
                // When Http response code other than 404, 500
                else {
                    try {
                        System.out.println(errorResponse);

                        if (errorResponse == null) {
                            postData();
                            return;
                        }

                        progressBar.setVisibility(View.VISIBLE);
                        nbutton.setEnabled(true);
                        pButton.setEnabled(true);

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
