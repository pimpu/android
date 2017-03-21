package com.cleanslatetech.floc.asynctask;

import android.app.ProgressDialog;
import android.content.Context;

import com.cleanslatetech.floc.R;
import com.cleanslatetech.floc.activities.UserNameFeedActivity;
import com.cleanslatetech.floc.utilities.CommonUtilities;
import com.cleanslatetech.floc.utilities.CommonVariables;
import com.cleanslatetech.floc.utilities.RestClient;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

import static com.cleanslatetech.floc.utilities.CommonUtilities.handleIntentWhenSignIn;

/**
 * Created by pimpu on 3/21/2017.
 */

public class RegisterSocialAsyncTask {
    private Context context;
    private String user, email, provider, providerKey;
    private ProgressDialog prgDialog;

    public RegisterSocialAsyncTask(Context context, String user, String email, String provider, String providerKey) {
        this.context = context;
        this.user = user;
        this.email = email;
        this.provider = provider;
        this.providerKey = providerKey;
    }

    public void postData() {
        // Instantiate Progress Dialog object
        prgDialog = new ProgressDialog(context);
        // Set Progress Dialog Text
        prgDialog.setMessage("Signing in ...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);

        RequestParams params;
        params = new RequestParams();
        params.put("ExtUsername", user);
        params.put("Email", email);
        params.put("Provider", provider);
        params.put("ProviderKey", providerKey);

        invokeWS(context, params);
    }

    private void invokeWS(final Context context, RequestParams params) {
        // Show Progress Dialog
        prgDialog.show();

        // Make RESTful webservice call using AsyncHttpClient object
        RestClient.post(CommonVariables.REGISTER_SOCIAL_SERVER_URL, params, new JsonHttpResponseHandler() {
            // When the response returned by REST has Http response code '200'

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
                prgDialog.cancel();
                try{
                    System.out.println(json);

                    Boolean error = json.getBoolean(CommonVariables.TAG_ERROR);

                    if (error) {
                        JSONArray jsonArray = json.getJSONArray(CommonVariables.TAG_MESSAGE);
                        CommonUtilities.customToast(context, jsonArray.getJSONObject(0).getString(CommonVariables.TAG_MESSAGE_OBJ));
                        System.out.println("Error: "+jsonArray.getJSONObject(0).getString(CommonVariables.TAG_MESSAGE_OBJ));

                        if(provider.equals("Facebook")) {
                            LoginManager.getInstance().logOut();
                        }
                        else if(provider.equals("Google")) {
                            Auth.GoogleSignInApi.signOut( ((UserNameFeedActivity)context).mGoogleApiClient).setResultCallback(
                                    new ResultCallback<Status>() {
                                        @Override
                                        public void onResult(Status status) {
                                        }
                                    });
                        }

                    } else {
                        String userName = json.getString("UserName");
                        int userId = json.getInt("Id");

                        String loginType = null;

                        if(provider.equals("Facebook")) {
                            loginType = context.getResources().getString(R.string.facebookLogin);
                        }
                        else if(provider.equals("Google")) {
                            loginType = context.getResources().getString(R.string.googleLogin);
                        }
                        handleIntentWhenSignIn(context, loginType, true, userName,
                                email, userId);
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
                prgDialog.cancel();
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
