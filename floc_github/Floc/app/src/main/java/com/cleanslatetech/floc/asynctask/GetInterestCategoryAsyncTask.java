package com.cleanslatetech.floc.asynctask;


import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.cleanslatetech.floc.R;
import com.cleanslatetech.floc.adapter.InterestAdapter;
import com.cleanslatetech.floc.sharedprefrencehelper.SetSharedPreference;
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
    private final ProgressBar prgDialogLayout;
    private final AppCompatButton btnRefresh;
    private Context context;
    private GridView selectInterestGridview;
    private InterestAdapter adapterInterest;

    public GetInterestCategoryAsyncTask(Context context, GridView selectInterestGridview) {
        this.context = context;
        this.selectInterestGridview = selectInterestGridview;

        // Instantiate Progress Dialog object
        prgDialogLayout = (ProgressBar) ((AppCompatActivity)context).findViewById(R.id.getCategoryProgress);
        this.prgDialogLayout.getIndeterminateDrawable().setColorFilter(
                context.getResources().getColor(R.color.white),
                android.graphics.PorterDuff.Mode.MULTIPLY);

        btnRefresh = (AppCompatButton) ((AppCompatActivity) context).findViewById(R.id.btnRefreshgetCategory);
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnRefresh.setVisibility(View.GONE);
                postData();
            }
        });

        postData();
    }

    private void postData() {
        prgDialogLayout.setVisibility(View.VISIBLE);
        invokeWS(context);
    }

    private void invokeWS(final Context context) {

        // Make RESTful webservice call using AsyncHttpClient object
        RestClient.get(CommonVariables.GET_INTEREST_CATEGORY_SERVER_URL, null, new JsonHttpResponseHandler() {
            // When the response returned by REST has Http response code '200'

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
                prgDialogLayout.setVisibility(View.GONE);
                selectInterestGridview.setVisibility(View.VISIBLE);
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
                prgDialogLayout.setVisibility(View.GONE);
                System.out.println("status code: "+statusCode);
                System.out.println("responseString: "+responseString);
                Toast.makeText(context, "Error "+statusCode+" : "+responseString, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                prgDialogLayout.setVisibility(View.GONE);
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
                        System.out.println(errorResponse);

                        if (errorResponse == null) {
                            Toast.makeText(context,"Sorry for inconvenience. Please, Try again.",Toast.LENGTH_LONG).show();

                            btnRefresh.setVisibility(View.VISIBLE);

                            return;
                        }

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

    private void populateGridview(final JSONArray getCategory) {
        // set all categories which are fetch from server.
        new SetSharedPreference(context).setString(context.getResources().getString(R.string.shrdAllCategoryList), getCategory.toString());

        adapterInterest = new InterestAdapter(context, getCategory);
        selectInterestGridview.setAdapter(adapterInterest);
    }

    public List getSelectedCategoryArray() {
        return adapterInterest.iArraySelectedPositions;
    }
}
