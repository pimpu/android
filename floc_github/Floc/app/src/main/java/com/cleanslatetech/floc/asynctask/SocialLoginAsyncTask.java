package com.cleanslatetech.floc.asynctask;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.cleanslatetech.floc.R;
import com.cleanslatetech.floc.activities.SignupOptionActivity;
import com.cleanslatetech.floc.activities.UserNameFeedActivity;
import com.cleanslatetech.floc.sharedprefrencehelper.SetSharedPreference;
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

public class SocialLoginAsyncTask {
    private ProgressDialog progressDialog;
    private String email, provider, providerKey, profile_pic;
    private Context context;

    public SocialLoginAsyncTask(Context context, String email, String provider, String providerKey, String profile_pic) {
        this.email = email;
        this.provider = provider;
        this.providerKey = providerKey;
        this.profile_pic = profile_pic;
        this.context = context;

//        ((AppCompatActivity)context).setContentView(R.layout.activity_splash_screen);
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Sign In");
        progressDialog.show();
    }

    public void postData() {
        RequestParams params;
        params = new RequestParams();
        params.put("EmailId", email);
        params.put("Provider", provider);
        params.put("ProviderKey", providerKey);

        invokeWS(context, params);

    }

    private void invokeWS(final Context context, RequestParams params) {
        // Make RESTful webservice call using AsyncHttpClient object
        RestClient.post(CommonVariables.CHECK_SOCIAL_SERVER_URL, params, new JsonHttpResponseHandler() {
            // When the response returned by REST has Http response code '200'

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
                progressDialog.dismiss();
                try{
                    System.out.println(json);

                    Boolean error = json.getBoolean(CommonVariables.TAG_ERROR);

                    if (error) {
                        JSONArray jsonArray = json.getJSONArray(CommonVariables.TAG_MESSAGE);
                        CommonUtilities.customToast(context, jsonArray.getJSONObject(0).getString(CommonVariables.TAG_MESSAGE_OBJ));

                        if(provider.equals("Facebook")) {
                            LoginManager.getInstance().logOut();
                        }
                        else if(provider.equals("Google")) {
                            Auth.GoogleSignInApi.signOut( ((SignupOptionActivity)context).mGoogleApiClient).setResultCallback(
                                    new ResultCallback<Status>() {
                                        @Override
                                        public void onResult(Status status) {
                                        }
                                    });
                        }

                        /*Handler handler = new Handler();
                        Runnable r = new Runnable() {
                            public void run() {
                                ((AppCompatActivity)context).recreate();
                                ((AppCompatActivity)context).overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                            }
                        };
                        handler.postDelayed(r, 500);*/

                    } else {
                        String userName = json.getString("UserName");
                        int userId = json.getInt("Id");
                        new SetSharedPreference(context).setString("social_profilePic", profile_pic);

                        if (userName.length() > 0 ) {
                            String loginType = null;
                            if(provider.equals("Facebook")) {
                                loginType = context.getResources().getString(R.string.facebookLogin);
                            }
                            else if(provider.equals("Google")) {
                                loginType = context.getResources().getString(R.string.googleLogin);
                            }

                            handleIntentWhenSignIn(context, loginType, userName, email, userId);

                        }
                        else {
                            /*context.startActivity(new Intent(context, UserNameFeedActivity.class)
                                    .putExtra("email", email)
                                    .putExtra("provider", provider)
                                    .putExtra("providerKey", providerKey));*/

//                            ((AppCompatActivity) context).finish();

                            new RegisterSocialAsyncTask(
                                    context,
                                    email,
                                    provider,
                                    providerKey ).postData();

                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                progressDialog.dismiss();

                System.out.println("status code: "+statusCode);
                System.out.println("responseString: "+responseString);
                CommonUtilities.customToast(context, "Error "+statusCode+" : "+responseString);

                /*Handler handler = new Handler();
                Runnable r = new Runnable() {
                    public void run() {
                        ((AppCompatActivity)context).recreate();
                        ((AppCompatActivity)context).overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    }
                };
                handler.postDelayed(r, 500);*/
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                // When Http response code is '404'
                if (statusCode == 404) {
                    progressDialog.dismiss();
                    System.out.println("Requested resource not found");
                    CommonUtilities.customToast(context, "Requested resource not found");
                }
                // When Http response code is '500'
                else if (statusCode == 500) {
                    progressDialog.dismiss();
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

                        progressDialog.dismiss();
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

                /*Handler handler = new Handler();
                Runnable r = new Runnable() {
                    public void run() {
                        ((AppCompatActivity)context).recreate();
                        ((AppCompatActivity)context).overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    }
                };
                handler.postDelayed(r, 500);*/
            }
        });
    }

}
