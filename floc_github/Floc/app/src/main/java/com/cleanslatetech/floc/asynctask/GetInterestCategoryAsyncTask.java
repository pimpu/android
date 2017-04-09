package com.cleanslatetech.floc.asynctask;


import android.app.ProgressDialog;
import android.content.Context;
import android.widget.GridView;

import com.cleanslatetech.floc.R;
import com.cleanslatetech.floc.adapter.InterestAdapter;
import com.cleanslatetech.floc.sharedprefrencehelper.SetSharedPreference;
import com.cleanslatetech.floc.utilities.CommonUtilities;
import com.cleanslatetech.floc.utilities.CommonVariables;
import com.cleanslatetech.floc.utilities.RestClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by pimpu on 1/24/2017.
 */
public class GetInterestCategoryAsyncTask {
    private ProgressDialog progressBar;
    private Context context;
    private GridView selectInterestGridview;
    private InterestAdapter adapterInterest;
    private JSONArray userInterest;

    public GetInterestCategoryAsyncTask(Context context, GridView selectInterestGridview, JSONArray userInterest, ProgressDialog progressBar) {
        this.context = context;
        this.selectInterestGridview = selectInterestGridview;
        this.userInterest = userInterest;
        this.progressBar = progressBar;

        invokeWS();
    }

    private void invokeWS() {

        // Make RESTful webservice call using AsyncHttpClient object
        RestClient.get(CommonVariables.GET_INTEREST_CATEGORY_SERVER_URL, null, new JsonHttpResponseHandler() {
            // When the response returned by REST has Http response code '200'

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
//                selectInterestGridview.setVisibility(View.VISIBLE);
                try{
                    System.out.println(json);

                    // manipulate gride view
                    populateGridview(json.getJSONArray("GetCategory"));

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

                            invokeWS();
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

    private void populateGridview(JSONArray getCategory) {
        // set all categories which are fetch from server.
        new SetSharedPreference(context).setString(context.getResources().getString(R.string.shrdAllCategoryList), getCategory.toString());

        adapterInterest = new InterestAdapter(context, getCategory, userInterest);
        selectInterestGridview.setAdapter(adapterInterest);

        progressBar.dismiss();
    }

    public List getSelectedCategoryArray() {
        return adapterInterest.iArraySelectedPositions;
    }
}
