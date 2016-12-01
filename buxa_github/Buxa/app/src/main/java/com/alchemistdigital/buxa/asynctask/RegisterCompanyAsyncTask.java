package com.alchemistdigital.buxa.asynctask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alchemistdigital.buxa.DBHelper.DatabaseClass;
import com.alchemistdigital.buxa.R;
import com.alchemistdigital.buxa.activities.RegisterActivity;
import com.alchemistdigital.buxa.activities.WelcomeActivity;
import com.alchemistdigital.buxa.sharedprefrencehelper.SetSharedPreference;
import com.alchemistdigital.buxa.utilities.CommonUtilities;
import com.alchemistdigital.buxa.utilities.CommonVariables;
import com.alchemistdigital.buxa.utilities.RestClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Pimpu on 11/29/2016.
 */
public class RegisterCompanyAsyncTask {
    Context context;
    private String company, uname, mobile, email, password, create_time;
    private ProgressDialog dialog;
    LinearLayout layout_msg;
    TextView tvErrConfrm;

    public RegisterCompanyAsyncTask(Context context, String company, String uname, String mobile, String email, String password, String create_time, LinearLayout layout_msg, TextView tvErrConfrm) {
        this.context = context;
        this.company = company;
        this.uname = uname;
        this.mobile = mobile;
        this.email = email;
        this.password = password;
        this.create_time = create_time;
        this.layout_msg = layout_msg;
        this.tvErrConfrm = tvErrConfrm;

        dialog = new ProgressDialog(context);
        dialog.setMessage("Registering ...");
        dialog.setIndeterminate(false);
        dialog.setCancelable(false);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    }

    public void registerCompany() {
        // initialised parameter object for server call
        RequestParams params;
        params = new RequestParams();

        params.put("company", company);
        params.put("uname", uname);
        params.put("mobile", mobile);
        params.put("email", email);
        params.put("password", password);
        params.put("create_time", create_time);

        invokeWS(params);
    }

    private void invokeWS(RequestParams params) {
        // Make RESTful webservice call using AsyncHttpClient object
        RestClient.post(CommonVariables.COMPANY_REGISTER_SERVER_URL, params, new JsonHttpResponseHandler() {
            // When the response returned by REST has Http response code '200'

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
//                    JSONObject json = new JSONObject(response);

                    Boolean error = response.getBoolean(CommonVariables.TAG_ERROR);
                    if (error) {
                        layout_msg.setVisibility(View.VISIBLE);
                        tvErrConfrm.setText(response.getString(CommonVariables.TAG_MESSAGE));
                    } else {
                        layout_msg.setVisibility(View.GONE);
                        SetSharedPreference setSharedPreference = new SetSharedPreference(context);

                        setSharedPreference.setLoginId(context.getString(R.string.loginId), response.getInt("id"));
                        setSharedPreference.setApiKey(context.getString(R.string.apikey), response.getString("api_key"));
                        setSharedPreference.setLoginEmail(context.getString(R.string.loginEmail), response.getString("email"));
                        setSharedPreference.setLoginName(context.getString(R.string.loginName), response.getString("loginName"));
                        setSharedPreference.setCompanyName(context.getString(R.string.companyName), response.getString("companyName"));

                        // create shortcut when user Register with this app.
                        CommonUtilities.addShortcut(context);

                        DatabaseClass dbHelper = new DatabaseClass(context);
                        if (dbHelper.numberOfComodityRows() <= 0 ) {
                            ((Activity)context).setContentView(R.layout.activity_splash_screen);
                            new InsertInternationalDestinationPort(context).execute();
                        }
                        else {
                            Intent intent = new Intent(context, WelcomeActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            ((Activity)context).finish();
                            context.startActivity(intent);
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
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
                    System.out.println("Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]");
                    Toast.makeText(context, "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

}
