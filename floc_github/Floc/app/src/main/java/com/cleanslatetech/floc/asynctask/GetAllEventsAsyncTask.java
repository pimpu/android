package com.cleanslatetech.floc.asynctask;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.cleanslatetech.floc.R;
import com.cleanslatetech.floc.adapter.AllEventsRecyclerViewAdapter;
import com.cleanslatetech.floc.utilities.CommonVariables;
import com.cleanslatetech.floc.utilities.RestClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

import static android.R.attr.progressDrawable;

/**
 * Created by pimpu on 1/31/2017.
 */
public class GetAllEventsAsyncTask {
    private ProgressBar progressBar;
    private RelativeLayout refreshBtnPageLayout;
    private Context context;
    private RecyclerView allEventRecyclerView;

    public GetAllEventsAsyncTask(Context context) {
        this.context = context;
        allEventRecyclerView = (RecyclerView) ((AppCompatActivity)context).findViewById(R.id.all_events_recycler);

        // Instantiate Progress Dialog object
        progressBar = (ProgressBar) ((AppCompatActivity)context).findViewById(R.id.eventHeaderProgress);

        refreshBtnPageLayout = (RelativeLayout) ((AppCompatActivity)context).findViewById(R.id.refreshAllEventPage);
        AppCompatButton btnRefresh = (AppCompatButton) ((AppCompatActivity) context).findViewById(R.id.btnRefreshAllEventPage);
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshBtnPageLayout.setVisibility(View.GONE);
                getData();
            }
        });

    }

    public void getData() {
        progressBar.setVisibility(View.VISIBLE);
        invokeWS(context);
    }

    private void invokeWS(final Context context) {
        // Make RESTful webservice call using AsyncHttpClient object
        RestClient.get(CommonVariables.GET_ALL_EVENTS_SERVER_URL, null, new JsonHttpResponseHandler() {
            // When the response returned by REST has Http response code '200'

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
                progressBar.setVisibility(View.GONE);
                allEventRecyclerView.setVisibility(View.VISIBLE);
                try{
                    System.out.println(json);

                    // manipulate gride view
                    populateAllEventRecyclerview(json.getJSONArray("Event"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }



            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                progressBar.setVisibility(View.GONE);
                System.out.println("status code: "+statusCode);
                System.out.println("responseString: "+responseString);
                Toast.makeText(context, "Error "+statusCode+" : "+responseString, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                progressBar.setVisibility(View.GONE);
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

                            refreshBtnPageLayout.setVisibility(View.VISIBLE);

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

    private void populateAllEventRecyclerview(JSONArray getEvents) {
        // set adapter for interests grid view
        RecyclerView.Adapter allEventRecyclerAdapter = new AllEventsRecyclerViewAdapter(context, getEvents);
        allEventRecyclerView.setAdapter(allEventRecyclerAdapter);
        allEventRecyclerView.setLayoutManager(new LinearLayoutManager(context));

        /*// video play when video thumbnail get click.
        final SelectInterestAdapter finalImageLoadAdapter = adapter;

        selectInterestGridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                // used for change background resources i.e. border to selected item
                iArraySelectedPositions = finalImageLoadAdapter.iArraySelectedPositions;

                try {
                    int eventCategoryId = getCategory.getJSONObject(position).getInt("EventCategoryId");
                    if( iArraySelectedPositions.contains(eventCategoryId) && (iCounter >= 0 ) ) {
                        iArraySelectedPositions.remove(iArraySelectedPositions.indexOf(eventCategoryId));
                        iCounter++;
                        setPickupInterestText(iCounter);
                    }
                    else if(iCounter <= 5 && iCounter > 0 ) {
                        iCounter--;
                        setPickupInterestText(iCounter);
                        iArraySelectedPositions.add(eventCategoryId);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                finalImageLoadAdapter.notifyDataSetChanged();
            }
        });*/
    }
}
