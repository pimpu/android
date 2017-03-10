package com.cleanslatetech.floc.asynctask;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cleanslatetech.floc.R;
import com.cleanslatetech.floc.adapter.AllFlocRecyclerAdapter;
import com.cleanslatetech.floc.adapter.CustomSpinnerAdapter;
import com.cleanslatetech.floc.sharedprefrencehelper.GetSharedPreference;
import com.cleanslatetech.floc.utilities.CommonUtilities;
import com.cleanslatetech.floc.utilities.CommonVariables;
import com.cleanslatetech.floc.utilities.RestClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by pimpu on 1/31/2017.
 */
public class GetFlocAsyncTask implements AdapterView.OnItemSelectedListener {
    private ProgressBar progressBar;
    private AppCompatButton btnRefresh;
    private Context context;
    private RecyclerView recyclerRunningFloc, recyclerCompletedFloc, recyclerPauseFloc, recyclerCancelFloc,
            recyclerRequestedFloc, recyclerMyFloc;
    private AppCompatSpinner spinnerFlocName;
    public static int iEventId;
    public JSONObject jsonObjectResult;

    public GetFlocAsyncTask(
            final Context context,
            RecyclerView recyclerRunningFloc,
            RecyclerView recyclerCompletedFloc,
            RecyclerView recyclerPauseFloc,
            RecyclerView recyclerCancelFloc,
            RecyclerView recyclerRequestedFloc,
            RecyclerView recyclerMyFloc,
            ProgressBar progressBar,
            final AppCompatButton btnRefresh,
            AppCompatSpinner spinnerFlocName) {

        this.context = context;
        this.recyclerRunningFloc = recyclerRunningFloc;
        this.recyclerCompletedFloc = recyclerCompletedFloc;
        this.recyclerPauseFloc = recyclerPauseFloc;
        this.recyclerCancelFloc = recyclerCancelFloc;
        this.recyclerRequestedFloc = recyclerRequestedFloc;
        this.recyclerMyFloc = recyclerMyFloc;
        this.spinnerFlocName = spinnerFlocName;

        // Instantiate Progress Dialog object
        this.progressBar = progressBar;
        this.progressBar.getIndeterminateDrawable().setColorFilter(
                context.getResources().getColor(R.color.white),
                android.graphics.PorterDuff.Mode.MULTIPLY);

        this.btnRefresh = btnRefresh;
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnRefresh.setVisibility(View.GONE);
                getData();
            }
        });

    }

    public void getData() {
        progressBar.setVisibility(View.VISIBLE);

        RequestParams params;
        params = new RequestParams();
        params.put("UserId", new GetSharedPreference(context).getInt(context.getResources().getString(R.string.shrdLoginId)));
//        params.put("UserId", 2);

        invokeWS(context, params);
    }

    private void invokeWS(final Context context, RequestParams params) {
        // Make RESTful webservice call using AsyncHttpClient object
        RestClient.get(CommonVariables.FLOC_LIST_SERVER_URL, params, new JsonHttpResponseHandler() {
            // When the response returned by REST has Http response code '200'

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
                progressBar.setVisibility(View.GONE);
                ((AppCompatActivity)context).findViewById(R.id.layout_floc_data_panel).setVisibility(View.VISIBLE);

                System.out.println(json);

                jsonObjectResult = json;
                populateAllEventRecyclerview();
            }



            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                progressBar.setVisibility(View.GONE);
                System.out.println("status code: "+statusCode);
                System.out.println("responseString: "+responseString);
                CommonUtilities.customToast(context, "Error "+statusCode+" : "+responseString);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                progressBar.setVisibility(View.GONE);
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
                            CommonUtilities.customToast(context,"Sorry for inconvenience. Please, Try again.");

                            btnRefresh.setVisibility(View.VISIBLE);

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

    private void populateAllEventRecyclerview() {
        try {

            JSONArray runningObj = jsonObjectResult.getJSONArray("Running");
            JSONArray completedObj = jsonObjectResult.getJSONArray("Completed");
            JSONArray pauseObj = jsonObjectResult.getJSONArray("Paused");
            JSONArray cancelObj = jsonObjectResult.getJSONArray("Cancelled");
            JSONArray requestObj = jsonObjectResult.getJSONArray("JEVM");
            JSONArray myFlocObj = jsonObjectResult.getJSONArray("EBVM");

            List<String> stringArrayFlocName = new ArrayList<String>();

            for(int j = 0 ; j < runningObj.length(); j++ ) {
                stringArrayFlocName.add(runningObj.getJSONObject(j).getString("EventName"));
            }

            AllFlocRecyclerAdapter allFlocRecyclerAdapter = new AllFlocRecyclerAdapter(context, runningObj);
            recyclerRunningFloc.setAdapter(allFlocRecyclerAdapter);
            recyclerRunningFloc.setLayoutManager(new LinearLayoutManager(context));

            allFlocRecyclerAdapter = new AllFlocRecyclerAdapter(context, completedObj);
            recyclerCompletedFloc.setAdapter(allFlocRecyclerAdapter);
            recyclerCompletedFloc.setLayoutManager(new LinearLayoutManager(context));

            allFlocRecyclerAdapter = new AllFlocRecyclerAdapter(context, pauseObj);
            recyclerPauseFloc.setAdapter(allFlocRecyclerAdapter);
            recyclerPauseFloc.setLayoutManager(new LinearLayoutManager(context));

            allFlocRecyclerAdapter = new AllFlocRecyclerAdapter(context, cancelObj);
            recyclerCancelFloc.setAdapter(allFlocRecyclerAdapter);
            recyclerCancelFloc.setLayoutManager(new LinearLayoutManager(context));

            allFlocRecyclerAdapter = new AllFlocRecyclerAdapter(context, requestObj);
            recyclerRequestedFloc.setAdapter(allFlocRecyclerAdapter);
            recyclerRequestedFloc.setLayoutManager(new LinearLayoutManager(context));

            allFlocRecyclerAdapter = new AllFlocRecyclerAdapter(context, myFlocObj);
            recyclerMyFloc.setAdapter(allFlocRecyclerAdapter);
            recyclerMyFloc.setLayoutManager(new LinearLayoutManager(context));

            CustomSpinnerAdapter adapterInterest = new CustomSpinnerAdapter(context, android.R.layout.simple_spinner_item, stringArrayFlocName);
            adapterInterest.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerFlocName.setAdapter(adapterInterest);
            spinnerFlocName.setOnItemSelectedListener(this);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        /*recyclerFloc.addOnItemTouchListener(
                new RecyclerItemClickListener(context, recyclerFloc ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        try {
                            Intent intentFlocDesc = new Intent(context, FlocDescriptionActivity.class);
                            intentFlocDesc.putExtra("floc_data", getEvents.getJSONObject(position).toString());
                            context.startActivity(intentFlocDesc);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );*/
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        TextView selectedText = (TextView) parent.getChildAt(0);
        if (selectedText != null) {
            selectedText.setTextColor(context.getResources().getColor(R.color.white));
        }

        try {
            iEventId = jsonObjectResult.getJSONArray("Running").getJSONObject(position).getInt("EventId");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
