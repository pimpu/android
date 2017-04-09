package com.cleanslatetech.floc.asynctask;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.GridView;

import com.cleanslatetech.floc.activities.SelectInterestActivity;
import com.cleanslatetech.floc.utilities.CommonUtilities;
import com.cleanslatetech.floc.utilities.CommonVariables;
import com.cleanslatetech.floc.utilities.PopulateFloDescData;
import com.cleanslatetech.floc.utilities.RestClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.Set;

import cz.msebera.android.httpclient.Header;

import static com.cleanslatetech.floc.R.id.tvBtnSaveInterest;

/**
 * Created by pimpu on 4/8/2017.
 */

public class GetUserInterestAsyncTask {
    private Context context;
    private GridView gridLinearLayout;
    private int userId;
    private AppCompatTextView tvBtnSaveInterest;
    private ProgressDialog progressBar;

    public GetUserInterestAsyncTask(Context context, GridView gridLinearLayout, int userId, AppCompatTextView tvBtnSaveInterest) {

        this.context = context;
        this.userId = userId;
        this.gridLinearLayout = gridLinearLayout;
        this.tvBtnSaveInterest = tvBtnSaveInterest;

        progressBar = new ProgressDialog(context);
        progressBar.setMessage("getting interests. Please wait ... ");
        progressBar.setCancelable(false);
    }

    public void getData() {
        RequestParams params;
        params = new RequestParams();
        params.put("UserId", userId);

        invokeWS(context, params);
    }

    private void invokeWS(final Context context, RequestParams params) {
        progressBar.show();

        // Make RESTful webservice call using AsyncHttpClient object
        RestClient.get(CommonVariables.GET_USER_INTEREST_SERVER_URL, params, new JsonHttpResponseHandler() {

            // When the response returned by REST has Http response code '200'
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
                System.out.println(json);
                try {
                    JSONArray userInterest = json.getJSONArray("UserInterest");

                    final GetInterestCategoryAsyncTask getInterestCategoryAsyncTask = new GetInterestCategoryAsyncTask(
                            context, gridLinearLayout, userInterest, progressBar);

                    tvBtnSaveInterest.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Set<String> hs = new HashSet<>();
                            hs.addAll(getInterestCategoryAsyncTask.getSelectedCategoryArray());
                            getInterestCategoryAsyncTask.getSelectedCategoryArray().clear();
                            getInterestCategoryAsyncTask.getSelectedCategoryArray().addAll(hs);
                            /*new SetInterestAsyncTask(
                                    context,
                                    getInterestCategoryAsyncTask.getSelectedCategoryArray()).postData();*/

                            System.out.println(getInterestCategoryAsyncTask.getSelectedCategoryArray());
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                progressBar.dismiss();

                System.out.println("status code: "+statusCode);
                System.out.println("responseString: "+responseString);
                CommonUtilities.customToast(context, "Error "+statusCode+" : "+responseString);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

                // When Http response code is '404'
                if (statusCode == 404) {
                    progressBar.dismiss();

                    System.out.println("Requested resource not found");
                    CommonUtilities.customToast(context, "Requested resource not found");
                }
                // When Http response code is '500'
                else if (statusCode == 500) {
                    progressBar.dismiss();

                    System.out.println("Something went wrong at server end");
                    CommonUtilities.customToast(context, "Something went wrong at server end");
                }
                // When Http response code other than 404, 500
                else {
                    try {
                        System.out.println(errorResponse);

                        if (errorResponse == null) {
                            getData();
                            return;
                        }

                        progressBar.dismiss();

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
