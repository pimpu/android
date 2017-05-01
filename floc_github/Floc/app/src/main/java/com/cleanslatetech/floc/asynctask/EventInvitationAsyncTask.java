package com.cleanslatetech.floc.asynctask;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cleanslatetech.floc.R;
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
 * Created by pimpu on 3/4/2017.
 */
public class EventInvitationAsyncTask {
    private Context context;
    private int iUSerId, iEventId;
    private String email;
    private ProgressDialog prgDialog;
    private ProgressBar progressBar;
    private Button nbutton;
    private TextView textview;

    public EventInvitationAsyncTask(Context context, int iUSerId, String email, int iEventId) {
        this.context = context;
        this.iUSerId = iUSerId;
        this.email = email;
        this.iEventId = iEventId;

        // Instantiate Progress Dialog object
        prgDialog = new ProgressDialog(context);
        // Set Progress Dialog Text
        prgDialog.setMessage("Invitation sending ...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);
    }

    public EventInvitationAsyncTask(Context context, String email, ProgressBar progressBar,
                                    Button nbutton, int iUSerId, int iEventId, TextView textview) {
        this.context = context;
        this.iUSerId = iUSerId;
        this.email = email;
        this.iEventId = iEventId;
        this.progressBar = progressBar;
        this.nbutton = nbutton;
        this.textview = textview;
        this.textview.setVisibility(View.GONE);
    }


    public void postData() {

        RequestParams params;
        params = new RequestParams();
        params.put("UserId", iUSerId);
        params.put("Emailid", email);
        params.put("EventId", iEventId);

        invokeWS(context, params);

    }

    private void invokeWS(final Context context, RequestParams params) {
        // Show Progress Dialog
        if (prgDialog != null) {
            prgDialog.show();

        } else {
            progressBar.setVisibility(View.VISIBLE);
        }

        // Make RESTful webservice call using AsyncHttpClient object
        RestClient.post(CommonVariables.EVENT_INVITATION_SERVER_URL, params, new JsonHttpResponseHandler() {
            // When the response returned by REST has Http response code '200'

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
                if (prgDialog != null) {
                    prgDialog.cancel();

                } else {
                    progressBar.setVisibility(View.GONE);
                }

                try{
                    System.out.println(json);

                    Boolean error = json.getBoolean(CommonVariables.TAG_ERROR);
                    JSONArray jsonArray = json.getJSONArray(CommonVariables.TAG_MESSAGE);

                    if (error) {
                        if (prgDialog != null) {
                            for( int i = 0 ; i < jsonArray.length(); i++) {
                                String msg = jsonArray.getJSONObject(i).getString(CommonVariables.TAG_MESSAGE_OBJ);
                                System.out.println(msg);
                                CommonUtilities.customToast(context, msg);
                            }
                        } else {
                            textview.setVisibility(View.VISIBLE);
                            String msg = jsonArray.getJSONObject(0).getString(CommonVariables.TAG_MESSAGE_OBJ);
                            textview.setText(msg);
                        }

                    } else {
                        if( nbutton != null) {
                            nbutton.performClick();
                        } else {
                            ((AppCompatEditText) ((AppCompatActivity)context).findViewById(R.id.id_txt_friend_to_invite)).setText("");
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                if (prgDialog != null) {
                    prgDialog.cancel();

                } else {
                    progressBar.setVisibility(View.GONE);
                }
                System.out.println("status code: "+statusCode);
                System.out.println("responseString: "+responseString);
                CommonUtilities.customToast(context, "Error "+statusCode+" : "+responseString);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

                // When Http response code is '404'
                if (statusCode == 404) {
                    if (prgDialog != null) {
                        prgDialog.cancel();

                    } else {
                        progressBar.setVisibility(View.GONE);
                    }
                    System.out.println("Requested resource not found");
                    CommonUtilities.customToast(context, "Requested resource not found");
                }
                // When Http response code is '500'
                else if (statusCode == 500) {
                    if (prgDialog != null) {
                        prgDialog.cancel();

                    } else {
                        progressBar.setVisibility(View.GONE);
                    }

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
                        if (prgDialog != null) {
                            prgDialog.cancel();

                        } else {
                            progressBar.setVisibility(View.GONE);
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
